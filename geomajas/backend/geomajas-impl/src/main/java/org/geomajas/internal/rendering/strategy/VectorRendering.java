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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geomajas.configuration.ApplicationInfo;
import org.geomajas.configuration.StyleInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.internal.rendering.painter.tile.StringContentTilePainter;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.layer.tile.VectorTile.VectorTileContentType;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.painter.tile.TilePainter;
import org.geomajas.rendering.strategy.RenderingStrategy;
import org.geomajas.service.ApplicationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.service.VectorLayerService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * <p>
 * Rendering strategy that creates either SVG or VML. It is an extension of the <code>AbstractRenderingStrategy</code>,
 * so the rendering of features can be easily done by calling the abstraction's only method. At that point we still have
 * to create the SVG/VML. To do this, a <code>VectorTilePainter</code> is used.
 * </p>
 * <p>
 * This puts the features in a tile. Not all features are included as-is. For example, when features are way too big,
 * they are clipped. (note: since the normal <code>InternalFeature</code> object does not support clipped features, an
 * extension, called <code>VectorFeature</code> is used instead).
 * </p>
 * <p>
 * When features are put in a VectorTile, it also keeps track of dependency between tiles. Tiles in Geomajas are
 * dependent in the sense that each feature lies in only 1 tile, even if it's geometry crosses the bounds of the tile.
 * To discern what tile a feature belongs to, the position of the first coordinate is used. The other tiles that the
 * geometry in question spans, are considered dependent tiles.
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
	private FilterService filterCreator;

	@Autowired
	private VectorLayerService layerService;

	@Autowired
	private TiledFeatureService tiledFeatureService;

	@Autowired
	private DtoConverterService converterService;

	/**
	 * Paint a tile! This class uses the {@link TiledFeatureService} to paint the features, then the
	 * {@link StringContentTilePainter} to paint the tiles.
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
	public InternalTile paint(TileMetadata metadata, ApplicationInfo application) throws RenderException {
		try {
			// Get the map and layer objects:
			VectorLayer vLayer = runtime.getVectorLayer(metadata.getLayerId());
			CoordinateReferenceSystem crs = runtime.getCrs(metadata.getCrs());

			// Prepare the tile:
			InternalTileImpl tile = new InternalTileImpl(metadata.getCode(), vLayer, metadata.getScale(),
					converterService);
			tile.setContentType(VectorTileContentType.STRING_CONTENT);

			// Prepare any filtering:
			String geomName = vLayer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
			Filter filter = filterCreator.createBboxFilter(crs.getIdentifiers().iterator().next().toString(), tile
					.getBbox(vLayer), geomName);
			if (metadata.getFilter() != null) {
				try {
					filter = filterCreator.createLogicFilter(CQL.toFilter(metadata.getFilter()), "and", filter);
				} catch (CQLException e) {
					throw new RenderException(e, ExceptionCode.FILTER_PARSE_PROBLEM, metadata.getFilter());
				}
			}

			// Get the features (always needs to include the geometry !)
			List<StyleInfo> styleDefinitions = new ArrayList<StyleInfo>();
			Collections.addAll(styleDefinitions, metadata.getStyleDefs());
			List<InternalFeature> features = layerService.getFeatures(metadata.getLayerId(), crs, filter,
					styleDefinitions, VectorLayerService.FEATURE_INCLUDE_ALL);

			Coordinate panOrigin = new Coordinate(metadata.getPanOrigin().getX(), metadata.getPanOrigin().getY());
			tiledFeatureService.fillTile(tile, features, vLayer, metadata.getCode(), metadata.getScale(), panOrigin);

			// At this point, we have a tile with rendered features.
			// Now we need to paint the tile itself:
			TilePainter tilePainter = new StringContentTilePainter(vLayer, metadata.getRenderer(), metadata.getScale(),
					panOrigin, geoService);
			tilePainter.setPaintGeometries(metadata.isPaintGeometries());
			tilePainter.setPaintLabels(metadata.isPaintLabels());
			return tilePainter.paint(tile);
		} catch (RenderException re) {
			throw re;
		} catch (GeomajasException ge) {
			throw new RenderException(ge, ExceptionCode.IMAGE_RENDERING_LAYER_PROBLEM);
		}
	}
}