/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.layer;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.media.jai.ImageLayout;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.MosaicDescriptor;
import javax.media.jai.operator.TranslateDescriptor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.tile.RasterTile;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Layer responsible for rendering raster layers. Most of the code is copied from the printing plugin.
 * 
 * @author Jan De Moerloose
 */
public class RasterDirectLayer extends DirectLayer {

	protected static final int DOWNLOAD_MAX_ATTEMPTS = 2;

	protected static final int DOWNLOAD_MAX_THREADS = 5;

	protected static final long DOWNLOAD_TIMEOUT = 120000; // milliseconds

	protected static final long DOWNLOAD_TIMEOUT_ONE_TILE = 100; // milliseconds

	protected static final int RETRY_WAIT = 100; // milliseconds

	private static final String BUNDLE_NAME = "org/geomajas/plugin/rasterizing/rasterizing"; //$NON-NLS-1$

	private static final String MISSING_TILE_IN_MOSAIC = "missing tile in mosaic ";

	private static final String OPACITY = "opacity:";

	private static final int DEFAULT_IMAGE_BUFFER_SIZE = 1024;

	private final List<RasterTile> tiles;

	private final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

	private final HttpClient httpClient;

	private final Logger log = LoggerFactory.getLogger(RasterDirectLayer.class);

	private final int tileWidth;

	private final int tileHeight;

	private final String style;

	private double tileScale = -1;

	public RasterDirectLayer(List<RasterTile> tiles, int tileWidth, int tileHeight, double tileScale, String style) {
		super();
		this.tileScale = tileScale;
		this.tiles = tiles;
		if (tileWidth < 1) {
			tileWidth = 1;
		}
		this.tileWidth = tileWidth;
		if (tileHeight < 1) {
			tileHeight = 1;
		}
		this.tileHeight = tileHeight;
		this.style = style;
		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager();
		manager.setDefaultMaxPerRoute(10);
		httpClient = new DefaultHttpClient(manager);
	}

	public RasterDirectLayer(List<RasterTile> tiles, int tileWidth, int tileHeight, String style) {
		this(tiles, tileWidth, tileHeight, -1.0, style);
	}

	@Override
	public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
		try {
			if (tiles.size() > 0) {
				Collection<Callable<ImageResult>> callables = new ArrayList<Callable<ImageResult>>(tiles.size());
				// Build the image downloading threads
				for (RasterTile tile : tiles) {
					RasterImageDownloadCallable downloadThread = new RasterImageDownloadCallable(DOWNLOAD_MAX_ATTEMPTS,
							tile);
					callables.add(downloadThread);
				}
				// Loop until all images are downloaded or timeout is reached
				long totalTimeout = DOWNLOAD_TIMEOUT + DOWNLOAD_TIMEOUT_ONE_TILE * tiles.size();
				log.debug("=== total timeout (millis): {}", totalTimeout);
				ExecutorService service = Executors.newFixedThreadPool(DOWNLOAD_MAX_THREADS);
				List<Future<ImageResult>> futures = service.invokeAll(callables, totalTimeout, TimeUnit.MILLISECONDS);
				// determine the pixel bounds of the mosaic
				Bbox pixelBounds = getPixelBounds(tiles);
				// create the images for the mosaic
				List<RenderedImage> images = new ArrayList<RenderedImage>();
				for (Future<ImageResult> future : futures) {
					ImageResult result = null;
					if (future.isDone()) {
						try {
							result = future.get();
							// create a rendered image
							if (result.getImage() != null && result.getImage().length > 0) {
								RenderedImage image = JAI.create("stream",
										new ByteArraySeekableStream(result.getImage()));
								// convert to common direct color model (some images have their own indexed color model)
								RenderedImage colored = toDirectColorModel(image);

								// translate to the correct position in the tile grid
								double xOffset = result.getRasterImage().getCode().getX() * tileWidth
										- pixelBounds.getX();
								double yOffset;
								// TODO: in some cases, the y-index is up (e.g. WMS), should be down for
								// all layers !!!!
								if (isYIndexUp(tiles)) {
									yOffset = result.getRasterImage().getCode().getY() * tileHeight
											- pixelBounds.getY();
								} else {
									yOffset = (pixelBounds.getMaxY() - (result.getRasterImage().getCode().getY() + 1)
											* tileHeight);
								}
								log.debug("adding to(" + xOffset + "," + yOffset + "), url = "
										+ result.getRasterImage().getUrl());
								RenderedImage translated = TranslateDescriptor.create(colored, (float) xOffset,
										(float) yOffset, new InterpolationNearest(), null);
								images.add(translated);
							}
						} catch (ExecutionException e) {
							addLoadError(graphics, (ImageException) (e.getCause()), viewport);
							log.warn(MISSING_TILE_IN_MOSAIC + e.getMessage());
						} catch (Exception e) {
							log.warn("Missing tile " + result.getRasterImage().getUrl());
							log.warn(MISSING_TILE_IN_MOSAIC + e.getMessage());
						}
					}
				}

				if (images.size() > 0) {
					ImageLayout imageLayout = new ImageLayout(0, 0, (int) pixelBounds.getWidth(),
							(int) pixelBounds.getHeight());
					imageLayout.setTileWidth(tileWidth);
					imageLayout.setTileHeight(tileHeight);

					// create the mosaic image
					ParameterBlock pbMosaic = new ParameterBlock();
					pbMosaic.add(MosaicDescriptor.MOSAIC_TYPE_OVERLAY);
					for (RenderedImage renderedImage : images) {
						pbMosaic.addSource(renderedImage);
					}
					RenderedOp mosaic = JAI.create("mosaic", pbMosaic, new RenderingHints(JAI.KEY_IMAGE_LAYOUT,
							imageLayout));
					try {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						log.debug("rendering to buffer...");
						ImageIO.write(mosaic, "png", baos);
						log.debug("rendering done, size = " + baos.toByteArray().length);
						RasterTile mosaicTile = new RasterTile();
						mosaicTile.setBounds(getWorldBounds(tiles));
						log.info("application bounds = " + mosaicTile.getBounds());
						ImageResult mosaicResult = new ImageResult(mosaicTile);
						mosaicResult.setImage(baos.toByteArray());
						addImage(graphics, mosaicResult, viewport);
					} catch (IOException e) {
						log.warn("could not write mosaic image " + e.getMessage());
					}
				}
			}
		} catch (InterruptedException e) {
			log.warn("rendering {} to {} failed : ", getTitle(), viewport.getBounds());
		}
	}

	protected void addImage(Graphics2D graphics, ImageResult imageResult, MapViewport viewport) throws IOException {
		Rectangle screenArea = viewport.getScreenArea();
		ReferencedEnvelope worldBounds = viewport.getBounds();
		// convert map bounds to application bounds
		double printScale = screenArea.getWidth() / worldBounds.getWidth();
		if (tileScale < 0) {
			tileScale = printScale;
		}
		Envelope applicationBounds = new Envelope((worldBounds.getMinX()) * printScale, (worldBounds.getMaxX())
				* printScale, -(worldBounds.getMinY()) * printScale, -(worldBounds.getMaxY()) * printScale);
		Bbox imageBounds = imageResult.getRasterImage().getBounds();
		// find transform between image bounds and application bounds
		double tx = (imageBounds.getX() * printScale / tileScale - applicationBounds.getMinX());
		double ty = (imageBounds.getY() * printScale / tileScale - applicationBounds.getMinY());
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageResult.getImage()));
		double scaleX = imageBounds.getWidth() / image.getWidth() * printScale / tileScale;
		double scaleY = imageBounds.getHeight() / image.getHeight() * printScale / tileScale;
		AffineTransform transform = new AffineTransform();
		transform.translate(tx, ty);
		transform.scale(scaleX, scaleY);
		if (log.isDebugEnabled()) {
			log.debug("adding image, width=" + image.getWidth() + ",height=" + image.getHeight() + ",x=" + tx + ",y="
					+ ty);
		}
		// opacity
		log.debug("before drawImage");
		// create a copy to apply transform
		Graphics2D g = (Graphics2D) graphics.create();
		// apply opacity to image off-graphics to avoid interference with whatever opacity model is used by graphics
		BufferedImage opaqueCopy = makeOpaque(image);
		g.drawImage(opaqueCopy, transform, null);
		log.debug("after drawImage");
	}

	private BufferedImage makeOpaque(BufferedImage image) {
		BufferedImage opaqueCopy = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		Graphics2D g1 = opaqueCopy.createGraphics();
		g1.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getOpacity()));
		g1.drawImage(image, null, 0, 0);
		g1.dispose();
		return opaqueCopy;
	}

	private float getOpacity() {
		String match = style;
		// could be 'opacity:0.5;' or '0.5'
		if (style.contains(OPACITY)) {
			match = style.substring(style.indexOf(OPACITY) + OPACITY.length());
		}
		int semiColonPosition = match.indexOf(';');
		if (semiColonPosition >= 0) {
			match = match.substring(0, semiColonPosition);
		}
		try {
			return Float.valueOf(match);
		} catch (NumberFormatException nfe) {
			log.warn("Could not parse opacity " + style + "of raster layer " + getTitle());
			return 1f;
		}
	}

	protected void addLoadError(Graphics2D graphics, ImageException imageResult, MapViewport viewport) {
		Bbox imageBounds = imageResult.getRasterImage().getBounds();
		ReferencedEnvelope viewBounds = viewport.getBounds();
		double rasterScale = viewport.getScreenArea().getWidth() / viewport.getBounds().getWidth();
		double width = imageBounds.getWidth();
		double height = imageBounds.getHeight();
		// subtract screen position of lower-left corner
		double x = imageBounds.getX() - rasterScale * viewBounds.getMinX();
		// shift y to lower left corner, flip y to user space and subtract
		// screen position of lower-left
		// corner
		double y = -imageBounds.getY() - imageBounds.getHeight() - rasterScale * viewBounds.getMinY();
		if (log.isDebugEnabled()) {
			log.debug("adding image, width=" + width + ",height=" + height + ",x=" + x + ",y=" + y);
		}
		// opacity
		log.debug("before drawImage");
		graphics.drawString(getNlsString("loaderror.line1"), (int) x, (int) y);
	}

	@Override
	public ReferencedEnvelope getBounds() {
		return null;
	}

	private boolean isYIndexUp(List<RasterTile> tiles) {
		RasterTile first = tiles.iterator().next();
		for (RasterTile tile : tiles) {
			if (tile.getCode().getY() > first.getCode().getY()) {
				return tile.getBounds().getY() > first.getBounds().getY();
			} else if (tile.getCode().getY() < first.getCode().getY()) {
				return tile.getBounds().getY() < first.getBounds().getY();
			}
		}
		return false;
	}

	private Bbox getPixelBounds(List<RasterTile> tiles) {
		Bbox bounds = null;
		for (RasterTile tile : tiles) {
			Bbox tileBounds = new Bbox(tile.getCode().getX() * tileWidth, tile.getCode().getY() * tileHeight,
					tileWidth, tileHeight);
			if (bounds == null) {
				bounds = new Bbox(tileBounds.getX(), tileBounds.getY(), tileBounds.getWidth(), tileBounds.getHeight());

			} else {
				double minx = Math.min(tileBounds.getX(), bounds.getX());
				double maxx = Math.max(tileBounds.getMaxX(), bounds.getMaxX());
				double miny = Math.min(tileBounds.getY(), bounds.getY());
				double maxy = Math.max(tileBounds.getMaxY(), bounds.getMaxY());
				bounds = new Bbox(minx, miny, maxx - minx, maxy - miny);
			}
		}
		return bounds;
	}

	private Bbox getWorldBounds(List<RasterTile> tiles) {
		Bbox bounds = null;
		for (RasterTile tile : tiles) {
			Bbox tileBounds = new Bbox(tile.getBounds().getX(), tile.getBounds().getY(), tile.getBounds().getWidth(),
					tile.getBounds().getHeight());
			if (bounds == null) {
				bounds = new Bbox(tileBounds.getX(), tileBounds.getY(), tileBounds.getWidth(), tileBounds.getHeight());
			} else {
				double minx = Math.min(tileBounds.getX(), bounds.getX());
				double maxx = Math.max(tileBounds.getMaxX(), bounds.getMaxX());
				double miny = Math.min(tileBounds.getY(), bounds.getY());
				double maxy = Math.max(tileBounds.getMaxY(), bounds.getMaxY());
				bounds = new Bbox(minx, miny, maxx - minx, maxy - miny);
			}
		}
		return bounds;
	}

	public String getNlsString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	/**
	 * Converts an image to a RGBA direct color model using a workaround via buffered image directly calling the
	 * ColorConvert operation fails for unknown reasons ?!
	 * 
	 * @param img image to convert
	 * @return converted image
	 */
	public PlanarImage toDirectColorModel(RenderedImage img) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		BufferedImage source = new BufferedImage(img.getColorModel(), (WritableRaster) img.getData(), img
				.getColorModel().isAlphaPremultiplied(), null);
		ColorConvertOp op = new ColorConvertOp(null);
		op.filter(source, dest);
		return PlanarImage.wrapRenderedImage(dest);
	}

	/**
	 * Image result.
	 * 
	 * @author Jan De Moerloose
	 */
	private static class ImageResult {

		private byte[] image;

		private final RasterTile rasterImage;

		public ImageResult(RasterTile rasterImage) {
			this.rasterImage = rasterImage;
		}

		public byte[] getImage() {
			return image;
		}

		public void setImage(byte[] image) {
			this.image = image;
		}

		public RasterTile getRasterImage() {
			return rasterImage;
		}
	}

	/**
	 * Image Exception
	 * 
	 * @author Jan De Moerloose
	 */
	private static class ImageException extends Exception {

		private static final long serialVersionUID = 100L;

		private final RasterTile rasterImage;

		public ImageException(RasterTile rasterImage, Throwable cause) {
			super(cause);
			this.rasterImage = rasterImage;
		}

		public RasterTile getRasterImage() {
			return rasterImage;
		}
	}

	/**
	 * Download image with a couple of retries.
	 * 
	 * @author Jan De Moerloose
	 */
	private class RasterImageDownloadCallable implements Callable<ImageResult> {

		private final ImageResult result;

		private final int retries;

		public RasterImageDownloadCallable(int retries, RasterTile rasterImage) {
			this.result = new ImageResult(rasterImage);
			this.retries = retries;
		}

		public ImageResult call() throws ImageException {
			log.debug("Fetching image: {}", result.getRasterImage().getUrl());
			int triesLeft = retries;
			while (true) {
				try {
					HttpGet get = new HttpGet(result.getRasterImage().getUrl());
					HttpResponse response = httpClient.execute(get);
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream(DEFAULT_IMAGE_BUFFER_SIZE);
					response.getEntity().writeTo(outputStream);
					result.setImage(outputStream.toByteArray());
					return result;
				} catch (Exception e) { // NOSONAR
					if (log.isDebugEnabled()) {
						log.error("Fetching image: error loading " + result.getRasterImage().getUrl(), e);
					}
					triesLeft--;
					if (triesLeft == 0) {
						throw new ImageException(result.getRasterImage(), e);
					} else {
						log.debug("Fetching image: retrying ", result.getRasterImage().getUrl());
						try {
							Thread.sleep(RETRY_WAIT); // give server some time to recover
						} catch (InterruptedException ie) {
							// NOSONAR just ignore
						}
					}
				}
			}
		}

	}

}