/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.plugin.printing.component.impl;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.RasterLayerService;
import org.geomajas.layer.tile.RasterTile;
import org.geomajas.plugin.printing.component.LayoutConstraint;
import org.geomajas.plugin.printing.component.PdfContext;
import org.geomajas.plugin.printing.component.PrintComponentVisitor;
import org.geomajas.plugin.printing.component.RasterLayerComponent;
import org.geomajas.plugin.printing.component.service.PrintConfigurationService;
import org.geotools.referencing.CRS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Sub component of a map responsible for rendering raster layer.
 * 
 * @author Jan De Moerloose
 */
public class RasterLayerComponentImpl extends BaseLayerComponentImpl implements RasterLayerComponent {

	protected static final int DOWNLOAD_MAX_ATTEMPTS = 2;

	protected static final int DOWNLOAD_MAX_THREADS = 5;

	protected static final long DOWNLOAD_TIMEOUT = 120000; // millis

	protected static final long DOWNLOAD_TIMEOUT_ONE_TILE = 100; // millis

	protected static final Font ERROR_FONT = new Font("SansSerif", Font.PLAIN, 6); //$NON-NLS-1$

	private static final String BUNDLE_NAME = "org/geomajas/extension/printing/rasterlayercomponent"; //$NON-NLS-1$

	// do not make this static, different requests might need different bundles
	@XStreamOmitField
	private final ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);

	/** The calculated bounds */
	@XStreamOmitField
	protected Envelope bbox;

	/** List of the tile images */
	@XStreamOmitField
	protected List<RasterTile> images;

	/** The raster scale, may be different from map ppunit */
	@XStreamOmitField
	protected double rasterScale;

	/** to fetch images */
	@XStreamOmitField
	private HttpClient httpClient;

	@XStreamOmitField
	private final Logger log = LoggerFactory.getLogger(RasterLayerComponentImpl.class);

	private RasterLayerService rasterLayerService;

	private PrintConfigurationService configurationService;

	public RasterLayerComponentImpl(RasterLayerService rasterLayerService,
			PrintConfigurationService configurationService) {
		this.rasterLayerService = rasterLayerService;
		this.configurationService = configurationService;
		getConstraint().setAlignmentX(LayoutConstraint.JUSTIFIED);
		getConstraint().setAlignmentY(LayoutConstraint.JUSTIFIED);
		MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
		manager.setMaxConnectionsPerHost(10);
		httpClient = new HttpClient(manager);
	}

	/**
	 * Call back visitor.
	 * 
	 * @param visitor
	 */
	public void accept(PrintComponentVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void render(PdfContext context) {
		if (isVisible()) {
			rasterScale = getMap().getPpUnit() * getMap().getRasterResolution() / 72;
			bbox = createBbox();
			try {
				if (log.isDebugEnabled()) {
					log.debug("rendering" + getLayerId() + " to [" + bbox.getMinX() + " " + bbox.getMinY() + " "
							+ bbox.getWidth() + " " + bbox.getHeight() + "]");
				}
				ClientMapInfo map = configurationService.getMapInfo(getMap().getMapId(), getMap().getApplicationId());
				this.images = rasterLayerService.getTiles(getLayerId(), CRS.decode(map.getCrs()), bbox, rasterScale);
			} catch (Throwable e) {
				log.error("could not paint raster layer " + getLayerId(), e);
				this.images = new ArrayList<RasterTile>();
			}

			Collection<Callable<ImageResult>> callables = new ArrayList<Callable<ImageResult>>(images.size());

			// Build the image downloading threads
			for (RasterTile image : images) {
				RasterImageDownloadCallable downloadThread = new RasterImageDownloadCallable(DOWNLOAD_MAX_ATTEMPTS,
						image);
				callables.add(downloadThread);
			}

			// Loop until all images are downloaded or timeout is reached
			long totalTimeout = DOWNLOAD_TIMEOUT + DOWNLOAD_TIMEOUT_ONE_TILE * images.size();
			log.debug("=== total timeout (millis): {}", totalTimeout);
			ExecutorService service = Executors.newFixedThreadPool(DOWNLOAD_MAX_THREADS);
			try {
				List<Future<ImageResult>> futures = service.invokeAll(callables, totalTimeout, TimeUnit.MILLISECONDS);
				// Add downloaded images to the pdf
				for (Future<ImageResult> future : futures) {
					if (future.isDone()) {
						try {
							ImageResult result = future.get();
							addImage(context, result);
						} catch (ExecutionException e) {
							addLoadError(context, (ImageException) (e.getCause()));
						}
					}
				}
			} catch (InterruptedException e) {
				// interrupted, should not happen
				log.error("raster loading interrupted", e);
			}
		}
	}

	public void addImage(PdfContext context, ImageResult imageResult) {
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
		context.drawImage(imageResult.getImage(), new Rectangle(x, y, x + width, y + height), getSize());
	}

	public void addLoadError(PdfContext context, ImageException e) {
		Bbox imageBounds = e.getRasterImage().getBounds();
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

		private Image image;

		private RasterTile rasterImage;

		public ImageResult(RasterTile rasterImage) {
			this.rasterImage = rasterImage;
		}

		public Image getImage() {
			return image;
		}

		public void setImage(Image image) {
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
					result.setImage(Image.getInstance(outputStream.toByteArray()));
					return result;
				} catch (Exception e) {
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

	public String getNlsString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

}
