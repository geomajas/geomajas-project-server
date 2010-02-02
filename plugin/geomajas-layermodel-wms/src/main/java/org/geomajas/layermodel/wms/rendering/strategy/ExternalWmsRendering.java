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

package org.geomajas.layermodel.wms.rendering.strategy;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.configuration.StyleInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.layermodel.wms.WmsLayer;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.image.RasterUrlBuilder;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.rendering.strategy.RenderingStrategy;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.FilterService;
import org.geomajas.service.VectorLayerService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * This rendering strategy will render images instead of SVG/VML. In order to get the correct images, it will simply ask
 * them from a WMS server. This WMS server in turn, must be available as a RasterLayerFactory in the XML configuration.
 * </p>
 * <p>
 * To use this rendering strategy it is important that you provide 2 parameters:
 * <ul>
 * <li>factory: The ID of the WMS RasterLayerFactory you wish to use.</li>
 * <li>layerName: The name of the layer known to the WMS server.</li>
 * </ul>
 * </p>
 * <p>
 * Internally this class contains a private class that implements the {@link RasterUrlBuilder} interface to create an
 * implementation that is able to create WMS url's. This class is added to the <code>RasterTileJG</code> objects so that
 * the <code>RasterTilePainter</code> can use it to actually paint the raster tiles.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component()
public class ExternalWmsRendering implements RenderingStrategy {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private FilterService filterCreator;

	@Autowired
	private PaintFactory paintFactory;

	@Autowired
	private VectorLayerService layerService;

	/**
	 * Holds the value of the WMS_LAYERNAME parameter.
	 */
	private String layerName;

	// -------------------------------------------------------------------------
	// RenderingStrategy implementation:
	// -------------------------------------------------------------------------

	/**
	 * Paint the tile! This function will create a <code>RasterTile</code> extension of the <code>RenderedTile</code>
	 * class. This <code>RasterTile</code> not only holds all the drawn features and tiling info, but also an URL
	 * pointing to the image that represents the rendered tile.
	 * 
	 * @param metadata
	 *            The object that holds all the spatial and styling information for a tile.
	 * @param application
	 *            The application in which this tile is to be rendered.
	 * @return Returns a completely rendered <code>RasterTile</code>.
	 */
	public InternalTile paint(TileMetadata metadata, ApplicationInfo application) throws RenderException {
		try {
			// Get the map and layer objects:
			VectorLayer vLayer = applicationService.getVectorLayer(metadata.getLayerId());
			CoordinateReferenceSystem crs = applicationService.getCrs(metadata.getCrs());

			// Prepare the tile:
			InternalTile tile = paintFactory.createRasterTile(metadata.getCode(), vLayer, metadata.getScale());

			// Prepare any filtering:
			String geomName = vLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
			Filter filter = filterCreator.createBboxFilter(crs.getIdentifiers().iterator().next().toString(), tile
					.getBbox(vLayer), geomName);
			if (metadata.getFilter() != null) {
				filter = filterCreator.createLogicFilter(CQL.toFilter(metadata.getFilter()), "and", filter);
			}

			// Create a FeaturePainter and paint the features:
			List<StyleInfo> styleDefinitions = new ArrayList<StyleInfo>();
			Collections.addAll(styleDefinitions, metadata.getStyleDefs());
			List<InternalFeature> features = layerService.getFeatures(metadata.getLayerId(), crs, filter,
					styleDefinitions, VectorLayerService.FEATURE_INCLUDE_ALL);

			// At this point, we have a tile with rendered features.
			// Now we need to paint the tile itself:
			tile.setFeatures(features);
			TilePainter tilePainter = paintFactory.createRasterTilePainter(new WmsUrlBuilder(tile, vLayer, layerName));
			return tilePainter.paint(tile);
		} catch (CQLException cqle) {
			throw new RenderException(ExceptionCode.RENDER_FILTER_PARSE_PROBLEM, cqle);
		} catch (RenderException re) {
			throw re;
		} catch (GeomajasException ge) {
			throw new RenderException(ge, ExceptionCode.RENDER_MAP_PROBLEM);
		}
	}

	/**
	 * Set the required parameters for this rendering strategy. As said before, this rendering strategy requires 2
	 * parameters: "factory" and "layerName".
	 */
	/*
	 * public void setParameters(List<Parameter> parameters) { for (Parameter parameter : parameters) { if
	 * (parameter.getDataSourceName().equalsIgnoreCase(WMS_FACTORY)) { factory = parameter.getValue(); } else if
	 * (parameter.getDataSourceName().equalsIgnoreCase(WMS_LAYERNAME)) { layerName = parameter.getValue(); } } }
	 */

	// -------------------------------------------------------------------------
	// Private stuff:
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * A private class that builds a WMS getMap request URL. This is the URL that will holds the tile's image.
	 * </p>
	 * 
	 * @author Pieter De Graef
	 */
	private class WmsUrlBuilder implements RasterUrlBuilder {

		private DecimalFormat decimalFormat = new DecimalFormat();

		private VectorLayer layer;

		private InternalTile tile;

		private String layerName;

		public WmsUrlBuilder(InternalTile tile, VectorLayer layer, String layerName) {
			this.tile = tile;
			this.layer = layer;
			this.layerName = layerName;

			decimalFormat.setDecimalSeparatorAlwaysShown(false);
			decimalFormat.setGroupingUsed(false);
			decimalFormat.setMinimumFractionDigits(0);
			decimalFormat.setMaximumFractionDigits(100);
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			decimalFormat.setDecimalFormatSymbols(symbols);
		}

		/**
		 * Create a WMS url.
		 */
		public String getImageUrl() {
			WmsLayer wmsLayer = (WmsLayer) applicationService.getLayer(layerName);

			String url = wmsLayer.getBaseWmsUrl();
			int pos = url.lastIndexOf('?');
			if (pos > 0) {
				url += "&SERVICE=WMS";
			} else {
				url += "?SERVICE=WMS";
			}
			url += "&request=GetMap";
			url += "&layers=" + layerName;
			url += "&srs=" + wmsLayer.getLayerInfo().getCrs();
			url += "&width=" + tile.getScreenWidth();
			url += "&height=" + tile.getScreenHeight();

			Bbox env = tile.getBbox(this.layer);
			url += "&bbox=" + decimalFormat.format(env.getX()) + "," + decimalFormat.format(env.getY()) + ","
					+ decimalFormat.format(env.getMaxX()) + "," + decimalFormat.format(env.getMaxY());
			url += "&format=" + wmsLayer.getFormat();
			url += "&version=" + wmsLayer.getVersion();
			url += "&styles=";
			return url;
		}

		/**
		 * Empty function....it's WMS, it's out of our hands.
		 */
		public void paintGeometries(boolean enable) {
		}

		/**
		 * Empty function....it's WMS, it's out of our hands.
		 */
		public void paintLabels(boolean enable) {
		}
	}
}