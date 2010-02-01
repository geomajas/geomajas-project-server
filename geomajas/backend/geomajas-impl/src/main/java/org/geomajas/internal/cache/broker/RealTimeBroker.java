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

package org.geomajas.internal.cache.broker;

import org.geomajas.cache.CacheException;
import org.geomajas.cache.CacheService;
import org.geomajas.cache.broker.Broker;
import org.geomajas.cache.store.RenderContent;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.internal.rendering.DefaultLayerPaintContext;
import org.geomajas.internal.rendering.DefaultTilePaintContext;
import org.geomajas.internal.rendering.painter.image.GeometryImagePainter;
import org.geomajas.internal.rendering.painter.image.LabelImagePainter;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.rendering.image.TileImageCreator;
import org.geomajas.rendering.painter.LayerPaintContext;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.rendering.painter.TilePaintContext;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * This broker gets it's content the old fashioned way: it creates the content in real-time. There is no underlying
 * caching mechanism here, just the default rendering algorithms.
 * </p>
 *
 * @author Pieter De Graef
 */
@Component()
public class RealTimeBroker implements Broker {

	private final Logger log = LoggerFactory.getLogger(RealTimeBroker.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ApplicationService runtime;

	@Autowired
	private ApplicationInfo application;

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private PaintFactory paintFactory;

	@Autowired
	private GeoService geoService;

	/**
	 * Try and get the content for a specific empty <code>RenderContent</code> object.
	 *
	 * @param renderContent
	 *            An empty renderContent. This function will create the content, using a <code>MapImageCreator</code>.
	 * @return Since this function does not rely on a caching mechanism, it can always create the content. Therefore
	 *         this function will always return "true" Unless something goes terribly wrong, in which case an exception
	 *         is thrown.
	 * @throws org.geomajas.cache.CacheException
	 */
	public boolean read(RenderContent renderContent) throws CacheException {

		// 1: Get all the necessary parameters:
		Map<String, Object> params = renderContent.getParameters();
		String layerId = (String) params.get(CacheService.PARAM_LAYER_ID);
		String crs = (String) params.get(CacheService.PARAM_CRS);
		double scale = (Double) params.get(CacheService.PARAM_SCALE);
		int tileLevel = (Integer) params.get(CacheService.PARAM_TILELEVEL);
		int x = (Integer) params.get(CacheService.PARAM_X);
		int y = (Integer) params.get(CacheService.PARAM_Y);
		String f = (String) params.get(CacheService.PARAM_FILTER);
		boolean paintGeometries = (Boolean) params.get(CacheService.PARAM_PAINT_GEOMETRIES);
		boolean paintLabels = (Boolean) params.get(CacheService.PARAM_PAINT_LABELS);

		// 2: Get the Application/Map/Layer objects:
		VectorLayer vLayer = runtime.getVectorLayer(layerId);
		Filter filter = null;
		if (f != null) {
			if (!"null".equalsIgnoreCase(f)) {
				try {
					filter = CQL.toFilter(f);
				} catch (CQLException e) {
					return false;
				}
			}
		}

		// 4: Create a context for rendering the image:
		TilePaintContext tileContext = new DefaultTilePaintContext();
		try {
			tileContext.setCrs(CRS.decode(crs));
		} catch (NoSuchAuthorityCodeException e) {
			throw new CacheException(ExceptionCode.CRS_DECODE_FAILURE_FOR_MAP, e, crs);
		} catch (FactoryException e) {
			throw new CacheException(ExceptionCode.CRS_NO_DEFAULT, e, crs);
		}
		LayerPaintContext layerContext = new DefaultLayerPaintContext(vLayer, filter);
		tileContext.add(layerContext);
		tileContext.setScale(scale);

		// 5: Create the vector tile:
		TileCode code = new TileCode(tileLevel, x, y);
		InternalTile tile = new InternalTileImpl(code, vLayer, scale);
		Bbox areaOfInterest = tile.getBbox(vLayer);
		tileContext.setAreaOfInterest(areaOfInterest);
		Rectangle paintArea = new Rectangle(0, 0, tile.getScreenWidth(), tile.getScreenHeight());

		// 6: Image creator for creating images:
		TileImageCreator imageCreator = paintFactory.createTileImageCreator(tile, true);
		//imageCreator.setRenderingHints(vLayer.getLayerInfo().getRenderingStrategies().getRenderingStrategy().get(0)
		//        .getParameterMap().getParameters());
		if (paintGeometries) {
			imageCreator.registerPainter(new GeometryImagePainter(geoService));
		}
		if (paintLabels) {
			imageCreator.registerPainter(new LabelImagePainter(vLayer.getLayerInfo().getLabelAttribute(), geoService));
		}
		RenderedImage image = imageCreator.paint(paintArea, tileContext);

		// 7: Set the image's content in the RenderContent:
		renderContent.setContent(createImage(image));
		return true;
	}

	/**
	 * This function is empty. There is no need for it, since this Broker can always produce the required result anyway.
	 */
	public void update(RenderContent renderContent) throws CacheException {
	}

	/**
	 * The writing to a ByteArrayOutputStream must be synchronized to avoid error in the JVM.
	 *
	 * @param image image to write (including meta-data)
	 * @return image data
	 * @throws CacheException when IOException occurred while writing
	 */
	private synchronized byte[] createImage(RenderedImage image) throws CacheException {
		byte[] result;

		// Write to a ByteArrayOutputStream:
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "PNG", out);
			out.flush();
			result = out.toByteArray();
			out.close();
		} catch (IOException e) {
			throw new CacheException(ExceptionCode.CACHE_UNEXPECTED_ERROR_WHILE_WRITING, e);
		}
		return result;
	}
}