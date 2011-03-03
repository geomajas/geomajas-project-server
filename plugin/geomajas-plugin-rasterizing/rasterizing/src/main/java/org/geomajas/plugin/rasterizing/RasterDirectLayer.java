/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.tile.RasterTile;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.media.jai.codec.ByteArraySeekableStream;

/**
 * Layer responsible for rendering raster layers. Most of the code is copied from the printing plugin.
 * 
 * @author Jan De Moerloose
 * 
 */
public class RasterDirectLayer extends DirectLayer {

	protected static final int DOWNLOAD_MAX_ATTEMPTS = 2;

	protected static final int DOWNLOAD_MAX_THREADS = 5;

	protected static final long DOWNLOAD_TIMEOUT = 120000; // millis

	protected static final long DOWNLOAD_TIMEOUT_ONE_TILE = 100; // millis

	protected static final Font ERROR_FONT = new Font("SansSerif", Font.PLAIN, 6); //$NON-NLS-1$

	private static final String BUNDLE_NAME = "org/geomajas/plugin/rasterizing/rasterizing"; //$NON-NLS-1$

	private List<RasterTile> tiles = new ArrayList<RasterTile>();

	private final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

	private HttpClient httpClient;

	private final Logger log = LoggerFactory.getLogger(RasterDirectLayer.class);

	private int tileWidth;

	private int tileHeight;

	private String style;

	public RasterDirectLayer(List<RasterTile> tiles, int tileWidth, int tileHeight, String style) {
		this.tiles = tiles;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.style = style;
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setMaxConnectionsPerHost(10);
		httpClient = new HttpClient(manager);
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
					if (future.isDone()) {
						try {
							ImageResult result;
							result = future.get();
							// create a rendered image
							RenderedImage image = JAI.create("stream", new ByteArraySeekableStream(result.getImage()));
							// convert to common direct colormodel (some images have their own indexed color model)
							RenderedImage colored = toDirectColorModel(image);

							// translate to the correct position in the tile grid
							double xOffset = result.getRasterImage().getCode().getX() * tileWidth - pixelBounds.getX();
							double yOffset = 0;
							// TODO: in some cases, the y-index is up (e.g. WMS), should be down for
							// all layers !!!!
							if (isYIndexUp(tiles)) {
								yOffset = result.getRasterImage().getCode().getY() * tileHeight - pixelBounds.getY();
							} else {
								yOffset = (pixelBounds.getMaxY() - (result.getRasterImage().getCode().getY() + 1)
										* tileHeight);
							}
							log.debug("adding to(" + xOffset + "," + yOffset + "), url = "
									+ result.getRasterImage().getUrl());
							RenderedImage translated = TranslateDescriptor.create(colored, (float) xOffset,
									(float) yOffset, new InterpolationNearest(), null);
							images.add(translated);
						} catch (ExecutionException e) {
							addLoadError(graphics, (ImageException) (e.getCause()), viewport);
						} catch (InterruptedException e) {
							log.warn("missing tile in mosaic " + e.getMessage());
						} catch (MalformedURLException e) {
							log.warn("missing tile in mosaic " + e.getMessage());
						} catch (IOException e) {
							log.warn("missing tile in mosaic " + e.getMessage());
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
		Bbox imageBounds = imageResult.getRasterImage().getBounds();
		ReferencedEnvelope viewBounds = viewport.getBounds();
		double rasterScale = viewport.getScreenArea().getWidth() / viewport.getBounds().getWidth();
		double width = imageBounds.getWidth();
		double height = imageBounds.getHeight();
		// subtract screen position of lower-left corner
		double x = imageBounds.getX() - rasterScale * viewBounds.getMinX();
		// shift y to lowerleft corner, flip y to user space and subtract
		// screen position of lower-left
		// corner
		double y = -imageBounds.getY() - imageBounds.getHeight() - rasterScale * viewBounds.getMinY();
		if (log.isDebugEnabled()) {
			log.debug("adding image, width=" + width + ",height=" + height + ",x=" + x + ",y=" + y);
		}
		// opacity
		log.debug("before drawImage");
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageResult.getImage()));
		graphics.drawImage(image, (int) x, (int) y, (int) width, (int) height, null);
		log.debug("after drawImage");
	}

	protected void addLoadError(Graphics2D graphics, ImageException imageResult, MapViewport viewport) {
		Bbox imageBounds = imageResult.getRasterImage().getBounds();
		ReferencedEnvelope viewBounds = viewport.getBounds();
		double rasterScale = viewport.getScreenArea().getWidth() / viewport.getBounds().getWidth();
		double width = imageBounds.getWidth();
		double height = imageBounds.getHeight();
		// subtract screen position of lower-left corner
		double x = imageBounds.getX() - rasterScale * viewBounds.getMinX();
		// shift y to lowerleft corner, flip y to user space and subtract
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
	 * ???
	 */
	private class ImageResult {

		private byte[] image;

		private RasterTile rasterImage;

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
	 * ???
	 */
	private class ImageException extends Exception {

		private static final long serialVersionUID = 151L;

		private RasterTile rasterImage;

		public ImageException(RasterTile rasterImage) {
			this.rasterImage = rasterImage;
		}

		public RasterTile getRasterImage() {
			return rasterImage;
		}
	}

	/**
	 * ???
	 */
	private class RasterImageDownloadCallable implements Callable<ImageResult> {

		private ImageResult result;

		private int retries;

		public RasterImageDownloadCallable(int retries, RasterTile rasterImage) {
			this.result = new ImageResult(rasterImage);
			this.retries = retries;
		}

		public ImageResult call() throws Exception {
			log.debug("Fetching image: {}", result.getRasterImage().getUrl());
			int triesLeft = retries;
			while (true) {
				try {
					GetMethod get = new GetMethod(result.getRasterImage().getUrl());
					httpClient.executeMethod(get);
					InputStream inputStream = get.getResponseBodyAsStream();
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
					byte[] bytes = new byte[1024];
					int readBytes;
					while ((readBytes = inputStream.read(bytes)) > 0) {
						outputStream.write(bytes, 0, readBytes);
					}
					inputStream.close();
					outputStream.flush();
					outputStream.close();
					result.setImage(outputStream.toByteArray());
					return result;
				} catch (Exception e) {
					if (log.isDebugEnabled()) {
						log.error("Fetching image: error loading " + result.getRasterImage().getUrl(), e);
					}
					triesLeft--;
					if (triesLeft == 0) {
						throw new ImageException(result.getRasterImage());
					} else {
						log.debug("Fetching image: retrying ", result.getRasterImage().getUrl());
					}
				}
			}
		}

	}

	// converts an image to a RGBA direct color model using a workaround via buffered image
	// directly calling the ColorConvert operation fails for unknown reasons ?!
	public PlanarImage toDirectColorModel(RenderedImage img) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		BufferedImage source = new BufferedImage(img.getColorModel(), (WritableRaster) img.getData(), img
				.getColorModel().isAlphaPremultiplied(), null);
		ColorConvertOp op = new ColorConvertOp(null);
		op.filter(source, dest);
		return PlanarImage.wrapRenderedImage(dest);
	}

}