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

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.application.tile.VectorTileJG;
import org.geomajas.internal.rendering.painter.tile.VectorTilePainter;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.PaintFactory;
import org.geomajas.rendering.painter.feature.FeaturePainter;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.rendering.strategy.RenderingStrategy;
import org.geomajas.rendering.tile.RenderedTile;
import org.geomajas.rendering.tile.TileMetadata;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.FilterCreator;
import org.geomajas.service.GeoService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Rendering strategy that creates either SVG or VML. It is an extension of the <code>AbstractRenderingStrategy</code>,
 * so the rendering of features can be easily done by calling the abstraction's only method. At that point we still have
 * to create the SVG/VML. To do this, a <code>VectorTilePainter</code> is used.
 * </p>
 *
 * @author Pieter De Graef
 */
@Component()
public class VectorRendering implements RenderingStrategy {

	@Autowired
	private ApplicationService runtime;

	@Autowired
	private GeoService geoService;

	@Autowired
	private FilterCreator filterCreator;

	@Autowired
	private PaintFactory paintFactory;

	/**
	 * Paint a tile! This class uses the {@link org.geomajas.internal.rendering.painter.feature.TiledFeaturePainter}
	 * to paint the features, then the {@link VectorTilePainter} to paint the tiles.
	 *
	 * @param metadata
	 *            The object that holds all the spatial and styling information for a tile.
	 * @param application
	 *            The application in which this tile is to be rendered.
	 * @return The fully rendered tile! The different implementations of this <code>RenderedTile</code> will contain
	 *         different rendering formats.
	 * @throws RenderException
	 *             Sometimes things go wrong...
	 */
	public RenderedTile paint(TileMetadata metadata, ApplicationInfo application) throws RenderException {
		try {
			// Get the map and layer objects:
			VectorLayer vLayer = runtime.getVectorLayer(metadata.getLayerId());
			CoordinateReferenceSystem crs = runtime.getCrs(metadata.getCrs());

			// Prepare the tile:
			VectorTileJG tile = new VectorTileJG(metadata.getCode(), vLayer, metadata.getScale());

			// Prepare any filtering:
			String geomName = vLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
			Filter filter = filterCreator.createBboxFilter(crs.getIdentifiers().iterator().next().toString(), tile
					.getBbox(vLayer), geomName);
			if (metadata.getFilter() != null) {
				try {
					filter = filterCreator.createLogicFilter(CQL.toFilter(metadata.getFilter()), "and", filter);
				} catch (CQLException e) {
					throw new RenderException(ExceptionCode.RENDER_FILTER_PARSE_PROBLEM, e);
				}
			}

			// Create a FeaturePainter and paint the features:
			Coordinate panOrigin = new Coordinate(metadata.getPanOrigin().getX(), metadata.getPanOrigin().getY());
			FeaturePainter painter = paintFactory.createTiledFeaturePainter(tile, vLayer, metadata.getCode(),
					metadata.getScale(), panOrigin);
			vLayer.paint(painter, filter, metadata.getStyleDefs(), crs);

			// At this point, we have a tile with rendered features.
			// Now we need to paint the tile itself:
			TilePainter tilePainter = new VectorTilePainter(vLayer, metadata.getRenderer(), metadata.getScale(),
					panOrigin, geoService);
			tilePainter.setPaintGeometries(metadata.isPaintGeometries());
			tilePainter.setPaintLabels(metadata.isPaintLabels());
			return tilePainter.paint(tile);
		} catch (LayerException me) {
			throw new RenderException(ExceptionCode.RENDER_MAP_PROBLEM, me);
		}
	}
}