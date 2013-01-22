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
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
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
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.dto.RasterLayerComponentInfo;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geomajas.service.DispatcherUrlService;
import org.geomajas.service.GeoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Envelope;


/**
 * Sub component of a map responsible for rendering raster layer.
 * 
 * @author Jan De Moerloose
 */
@Component()
@Scope(value = "prototype")
public class RasterLayerComponentImpl extends BaseLayerComponentImpl<RasterLayerComponentInfo> {

	protected static final int DOWNLOAD_MAX_ATTEMPTS = 2;

	protected static final int DOWNLOAD_MAX_THREADS = 5;

	protected static final long DOWNLOAD_TIMEOUT = 120000; // millis

	protected static final long DOWNLOAD_TIMEOUT_ONE_TILE = 100; // millis

	protected static final Font ERROR_FONT = new Font("SansSerif", Font.PLAIN, 6); //$NON-NLS-1$

	private static final String BUNDLE_NAME = "org/geomajas/extension/printing/rasterlayercomponent"; //$NON-NLS-1$

	// do not make this static, different requests might need different bundles
	@XStreamOmitField
	private final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

	/** The calculated bounds. */
	@XStreamOmitField
	protected Envelope bbox;

	/** List of the tile images. */
	@XStreamOmitField
	protected List<RasterTile> tiles;

	/** The raster scale, may be different from map ppunit. */
	@XStreamOmitField
	protected double rasterScale;

	/** To fetch images. */
	@XStreamOmitField
	private HttpClient httpClient;

	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(RasterLayerComponentImpl.class);

	@Autowired
	@XStreamOmitField
	private RasterLayerService rasterLayerService;

	@Autowired
	@XStreamOmitField
	private PrintConfigurationService configurationService;

	@Autowired
	@XStreamOmitField
	private GeoService geoService;
	
	@Autowired
	@XStreamOmitField
	private DispatcherUrlService dispatcherUrlService;

	private float opacity = 1.0f;

	/** Constructor. */
	public RasterLayerComponentImpl() {
		getConstraint().setAlignmentX(LayoutConstraint.JUSTIFIED);
		getConstraint().setAlignmentY(LayoutConstraint.JUSTIFIED);
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setMaxConnectionsPerHost(10);
		httpClient = new HttpClient(manager);
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
	}

	@Override
	public void render(PdfContext context) {
		if (isVisible()) {
			rasterScale = getMap().getPpUnit() * getMap().getRasterResolution() / 72;
			bbox = createBbox();
			if (log.isDebugEnabled()) {
				log.debug("rendering" + getLayerId() + " to [" + bbox.getMinX() + " " + bbox.getMinY() + " "
						+ bbox.getWidth() + " " + bbox.getHeight() + "]");
			}
			ClientMapInfo map = configurationService.getMapInfo(getMap().getMapId(), getMap().getApplicationId());
			try {
				tiles = rasterLayerService.getTiles(getLayerId(), geoService.getCrs2(map.getCrs()), bbox, rasterScale);
				if (tiles.size() > 0) {
					Collection<Callable<ImageResult>> callables = new ArrayList<Callable<ImageResult>>(tiles.size());
					// Build the image downloading threads
					for (RasterTile tile : tiles) {
						RasterImageDownloadCallable downloadThread = new RasterImageDownloadCallable(
								DOWNLOAD_MAX_ATTEMPTS, tile);
						callables.add(downloadThread);
					}
					// Loop until all images are downloaded or timeout is reached
					long totalTimeout = DOWNLOAD_TIMEOUT + DOWNLOAD_TIMEOUT_ONE_TILE * tiles.size();
					log.debug("=== total timeout (millis): {}", totalTimeout);
					ExecutorService service = Executors.newFixedThreadPool(DOWNLOAD_MAX_THREADS);
					List<Future<ImageResult>> futures = service.invokeAll(callables, totalTimeout,
							TimeUnit.MILLISECONDS);
					// determine the pixel bounds of the mosaic
					Bbox pixelBounds = getPixelBounds(tiles);
					int imageWidth = configurationService.getRasterLayerInfo(getLayerId()).getTileWidth();
					int imageHeight = configurationService.getRasterLayerInfo(getLayerId()).getTileHeight();
					// create the images for the mosaic
					List<RenderedImage> images = new ArrayList<RenderedImage>();
					for (Future<ImageResult> future : futures) {
						if (future.isDone()) {
							try {
								ImageResult result;
								result = future.get();
								// create a rendered image
								RenderedImage image = JAI.create("stream", new ByteArraySeekableStream(result
										.getImage()));
								// convert to common direct colormodel (some images have their own indexed color model)
								// Sprint-51  If the image source is not available the java.awt.image will throw
								// a runtime error causing the printing to fail.  If this happens handle the error
								// and and allow the print process to continue.
								// convert to common direct colormodel (some images have their own indexed color model)
								RenderedImage colored = null;
								try {
									colored = toDirectColorModel(image);
								} catch (Exception e) {
									continue;
								}

								// translate to the correct position in the tile grid
								double xOffset = result.getRasterImage().getCode().getX() * imageWidth
										- pixelBounds.getX();
								double yOffset = 0;
								// TODO: in some cases, the y-index is up (e.g. WMS), should be down for
								// all layers !!!!
								if (isYIndexUp(tiles)) {
									yOffset = result.getRasterImage().getCode().getY() * imageHeight
											- pixelBounds.getY();
								} else {
									yOffset = (float) (pixelBounds.getMaxY() - (result.getRasterImage().getCode()
											.getY() + 1)
											* imageHeight);
								}
								log.debug("adding to(" + xOffset + "," + yOffset + "), url = "
										+ result.getRasterImage().getUrl());
								RenderedImage translated = TranslateDescriptor.create(colored, (float) xOffset,
										(float) yOffset, new InterpolationNearest(), null);
								images.add(translated);
							} catch (ExecutionException e) {
								addLoadError(context, (ImageException) (e.getCause()));
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
						ImageLayout imageLayout = new ImageLayout(0, 0, (int) pixelBounds.getWidth(), (int) pixelBounds
								.getHeight());
						imageLayout.setTileWidth(imageWidth);
						imageLayout.setTileHeight(imageHeight);

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
							addImage(context, mosaicResult);
						} catch (IOException e) {
							log.warn("could not write mosaic image " + e.getMessage());
						} catch (BadElementException e) {
							log.warn("could not write mosaic image " + e.getMessage());
						}
					}
				}
			} catch (GeomajasException e) {
				log.warn("rendering" + getLayerId() + " to [" + bbox.getMinX() + " " + bbox.getMinY() + " "
						+ bbox.getWidth() + " " + bbox.getHeight() + "] failed : " + e.getMessage());
			} catch (InterruptedException e) {
				log.warn("rendering" + getLayerId() + " to [" + bbox.getMinX() + " " + bbox.getMinY() + " "
						+ bbox.getWidth() + " " + bbox.getHeight() + "] failed : " + e.getMessage());
			}
		}
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
		int imageWidth = configurationService.getRasterLayerInfo(getLayerId()).getTileWidth();
		int imageHeight = configurationService.getRasterLayerInfo(getLayerId()).getTileHeight();
		for (RasterTile tile : tiles) {
			Bbox tileBounds = new Bbox(tile.getCode().getX() * imageWidth, tile.getCode().getY() * imageHeight,
					imageWidth, imageHeight);
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

	/**
	 * Get the layer opacity.
	 *
	 * @return layer opacity
	 */
	public float getOpacity() {
		return opacity;
	}

	/**
	 * Set the layer opacity.
	 *
	 * @param opacity layer opacity
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	@Override
	public void fromDto(RasterLayerComponentInfo rasterInfo) {
		super.fromDto(rasterInfo);
		String style = rasterInfo.getStyle();
		if (rasterInfo.getStyle() != null) {
			String match = style;
			// could be 'opacity:0.5;' or '0.5'
			if (style.contains("opacity:")) {
				match = style.substring(style.indexOf("opacity:") + 8);
			}
			if (match.contains(";")) {
				match = match.substring(0, match.indexOf(";"));
			}
			try {
				setOpacity(Float.valueOf(match));
			} catch (NumberFormatException nfe) {
				log.warn("Could not parse opacity " + style + "of raster layer " + getLayerId());
			}
		}
	}

	/**
	 * Add image in the document.
	 *
	 * @param context PDF context
	 * @param imageResult image
	 * @throws BadElementException PDF construction problem
	 * @throws IOException PDF construction problem
	 */
	protected void addImage(PdfContext context, ImageResult imageResult) throws BadElementException, IOException {
		Bbox imageBounds = imageResult.getRasterImage().getBounds();
		float scaleFactor = (float) (72 / getMap().getRasterResolution());
		float width = (float) imageBounds.getWidth() * scaleFactor;
		float height = (float) imageBounds.getHeight() * scaleFactor;
		// subtract screen position of lower-left corner
		float x = (float) (imageBounds.getX() - rasterScale * bbox.getMinX()) * scaleFactor;
		// shift y to lowerleft corner, flip y to user space and subtract
		// screen position of lower-left
		// corner
		float y = (float) (-imageBounds.getY() - imageBounds.getHeight() - rasterScale * bbox.getMinY()) * scaleFactor;
		if (log.isDebugEnabled()) {
			log.debug("adding image, width=" + width + ",height=" + height + ",x=" + x + ",y=" + y);
		}
		// opacity
		log.debug("before drawImage");
		context.drawImage(Image.getInstance(imageResult.getImage()), new Rectangle(x, y, x + width, y + height),
				getSize(), getOpacity());
		log.debug("after drawImage");
	}

	/**
	 * Add image with a exception message in the PDF document.
	 *
	 * @param context PDF context
	 * @param e exception to put in image
	 */
	protected void addLoadError(PdfContext context, ImageException e) {
		Bbox imageBounds = e.getRasterImage().getBounds();
		float scaleFactor = (float) (72 / getMap().getRasterResolution());
		float width = (float) imageBounds.getWidth() * scaleFactor;
		float height = (float) imageBounds.getHeight() * scaleFactor;
		// subtract screen position of lower-left corner
		float x = (float) (imageBounds.getX() - rasterScale * bbox.getMinX()) * scaleFactor;
		// shift y to lower left corner, flip y to user space and subtract
		// screen position of lower-left
		// corner
		float y = (float) (-imageBounds.getY() - imageBounds.getHeight() - rasterScale * bbox.getMinY()) * scaleFactor;
		if (log.isDebugEnabled()) {
			log.debug("adding failed message=" + width + ",height=" + height + ",x=" + x + ",y=" + y);
		}
		float textHeight = context.getTextSize("failed", ERROR_FONT).getHeight() * 3f;
		Rectangle rec = new Rectangle(x, y, x + width, y + height);
		context.strokeRectangle(rec, Color.RED, 0.5f);
		context.drawText(getNlsString("RasterLayerComponent.loaderror.line1"), ERROR_FONT, new Rectangle(x, y
				+ textHeight, x + width, y + height), Color.RED);
		context.drawText(getNlsString("RasterLayerComponent.loaderror.line2"), ERROR_FONT, rec, Color.RED);
		context.drawText(getNlsString("RasterLayerComponent.loaderror.line3"), ERROR_FONT, new Rectangle(x, y
				- textHeight, x + width, y + height), Color.RED);
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
	 * Image Exception
	 */
	private class ImageException extends Exception {

		private static final long serialVersionUID = 151L;

		private final RasterTile rasterImage;

		/**
		 * Constructor.
		 *
		 * @param rasterImage image for which the exception occurred
		 * @param cause cause exception
		 *
		 * */
		public ImageException(RasterTile rasterImage, Throwable cause) {
			super(cause);
			this.rasterImage = rasterImage;
		}

		/**
		 * Get image for which the exception occurred.
		 *
		 * @return image for which the exception occurred
		 */
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
		
		private String url;

		public RasterImageDownloadCallable(int retries, RasterTile rasterImage) {
			this.result = new ImageResult(rasterImage);
			this.retries = retries;
			String externalUrl = rasterImage.getUrl();
			url = dispatcherUrlService.localize(externalUrl);
		}

		public ImageResult call() throws Exception {
			log.debug("Fetching image: {}", url);
			int triesLeft = retries;
			while (true) {
				try {
					GetMethod get = new GetMethod(url);
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
				} catch (Exception e) { // NOSONAR
					triesLeft--;
					if (triesLeft == 0) {
						throw new ImageException(result.getRasterImage(), e);
					} else {
						log.debug("Fetching image: retrying ", url);
					}
				}
			}
		}

	}

	// converts an image to a RGBA direct color model using a workaround via buffered image
	// directly calling the ColorConvert operation fails for unknown reasons ?!

	/**
	 * Converts an image to a RGBA direct color model using a workaround via buffered image
	 * directly calling the ColorConvert operation fails for unknown reasons ?!
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
	 * Lookup error message for internationalization bundle.
	 *
	 * @param key key to lookup
	 * @return internationalized value
	 */
	public String getNlsString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
