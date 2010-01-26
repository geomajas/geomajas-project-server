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

package org.geomajas.internal.rendering.strategy;

import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.application.tile.RasterTileJG;
import org.geomajas.internal.rendering.painter.feature.DefaultFeaturePainter;
import org.geomajas.internal.rendering.painter.tile.RasterTilePainter;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.FeatureFactory;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.image.RasterUrlBuilder;
import org.geomajas.rendering.painter.feature.FeaturePainter;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.rendering.strategy.RenderingStrategy;
import org.geomajas.rendering.tile.RenderedTile;
import org.geomajas.rendering.tile.TileCode;
import org.geomajas.rendering.tile.TileMetadata;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.FilterCreator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * <p>
 * This rendering strategy will render images instead of SVG/VML. These images can be fetched from a specific servlet,
 * designed to generate them. What this class does, is return the URL at which that image can be fetched.
 * </p>
 * <p>
 * Internally this class contains a private class that implements the {@link RasterUrlBuilder} interface to create an
 * implementation that is able to create url's that refer to the {@link RetrieveImageServlet}. This class is added to
 * the {@link RasterTileJG} objects so that the {@link RasterTilePainter} can use it to actually paint the raster tiles.
 * </p>
 *
 * @author Pieter De Graef
 */
@Component()
public class ImageRendering implements RenderingStrategy {

	@Autowired
	private ApplicationService runtime;

	@Autowired
	private FilterCreator filterCreator;

	@Autowired
	private FeatureFactory featureFactory;

	/**
	 * Paint the tile! This function will create a <code>RasterTile</code> extension of the <code>RenderedTile</code>
	 * class. This <code>RasterTile</code> not only holds all the drawn features and tiling info, but also an URL
	 * pointing to the image that represents the rendered tile.
	 *
	 * @param metadata
	 *            The command that holds all the spatial and styling information.
	 * @param application
	 *            The application in which this tile is to be rendered.
	 * @return Returns a completely rendered <code>RasterTile</code>.
	 */
	public RenderedTile paint(TileMetadata metadata, ApplicationInfo application) throws RenderException {
		try {
			// Get the map and layer objects:
			VectorLayer vLayer = runtime.getVectorLayer(metadata.getLayerId());
			CoordinateReferenceSystem crs = runtime.getCrs(metadata.getCrs());

			// Prepare the tile:
			RasterTileJG tile = new RasterTileJG(metadata.getCode(), vLayer, metadata.getScale());
			RasterUrlBuilder urlBuilder = new InternalUrlBuilder(metadata, application);
			tile.setUrlBuilder(urlBuilder);

			// Prepare any filtering:
			String geomName = vLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
			Filter filter = filterCreator.createBboxFilter(crs.getIdentifiers().iterator().next()
					.toString(), tile.getBbox(vLayer), geomName);
			if (metadata.getFilter() != null) {
				filter = filterCreator.createLogicFilter(CQL.toFilter(metadata.getFilter()), "and", filter);
			}

			// Create a FeaturePainter and paint the features:
			FeaturePainter featurePainter = new DefaultFeaturePainter(featureFactory);
			vLayer.paint(featurePainter, filter, metadata.getStyleDefs(), crs);

			// At this point, we have a tile with rendered features.
			// Now we need to paint the tile itself:
			tile.setFeatures(featurePainter.getFeatures());
			TilePainter tilePainter = new RasterTilePainter(vLayer.getLayerInfo().getId());
			tilePainter.setPaintGeometries(metadata.isPaintGeometries());
			tilePainter.setPaintLabels(metadata.isPaintLabels());
			return tilePainter.paint(tile);
		} catch (LayerException layerException) {
			throw new RenderException(ExceptionCode.IMAGE_RENDERING_LAYER_PROBLEM, layerException);
		} catch (CQLException cqlException) {
			throw new RenderException(ExceptionCode.IMAGE_RENDERING_FILTER_PROBLEM, cqlException);
		}
	}

	// -------------------------------------------------------------------------
	// Private stuff:
	// -------------------------------------------------------------------------

	/**
	 * <p>
	 * A private class that builds a URL to the servlet that will serve the URL to the tile's image.
	 * </p>
	 *
	 * @author Pieter De Graef
	 */
	private class InternalUrlBuilder implements RasterUrlBuilder {

		private TileMetadata metadata;

		private ApplicationInfo application;

		private boolean paintGeometries = true;

		private boolean paintLabels;

		public InternalUrlBuilder(TileMetadata metadata, ApplicationInfo application) {
			this.metadata = metadata;
			this.application = application;
		}

		public String getImageUrl() {
			TileCode c = metadata.getCode();
			Coordinate p = metadata.getPanOrigin();
			return "tile/" + UUID.randomUUID().toString() + ".png?appId=" + application.getId()
					+ "&layerId=" + metadata.getLayerId() + "&x=" + c.getX() + "&y=" + c.getY() + "&tileLevel="
					+ c.getTileLevel() + "&scale=" + metadata.getScale() + "&origX=" + p.getX() + "&origY=" + p.getY()
					+ "&filter=" + metadata.getFilter() + "&setPaintGeometries=" + paintGeometries
					+ "&setPaintLabels=" + paintLabels;
		}

		public void paintGeometries(boolean enable) {
			paintGeometries = enable;
		}

		public void paintLabels(boolean enable) {
			paintLabels = enable;
		}
	}
}