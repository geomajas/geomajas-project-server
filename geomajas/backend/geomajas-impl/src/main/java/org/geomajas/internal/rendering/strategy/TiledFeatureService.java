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

import java.util.List;

import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.service.DtoConverterService;
import org.geotools.geometry.jts.JTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * This service puts features in a tile. In that case, not all features which overlap the tile are included as-is.
 * </p>
 * <p>
 * For example, when features are way too big, they are clipped. (note: since the normal <code>InternalFeature</code>
 * object does not support clipped features, an extension, called <code>VectorFeature</code> is used instead).
 * </p>
 * <p>
 * It also keeps track of dependency between tiles. Tiles in Geomajas are dependent in the sense that each feature lies
 * in only 1 tile, even if it's geometry crosses the bounds of the tile. To discern what tile a feature belongs to, the
 * position of the first coordinate is used. The other tiles that the geometry in question spans, are considered
 * dependent tiles.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
@Component
public class TiledFeatureService {

	@Autowired
	private DtoConverterService converterService;

	/**
	 * Helps determine when a feature is too big and must therefore be clipped.
	 */
	private static int MAXIMUM_TILE_COORDINATE = 10000;

	/**
	 * The tile's maximum bounds in screen space. Needed for clipping calculations.
	 */
	private Envelope maxScreenBbox;

	/**
	 * Paint an individual feature. In other words transform the generic feature object into a
	 * <code>VectorFeature</code>, and prepare it for a certain tile.
	 * 
	 * @param tile
	 *            tile to put features in
	 * @param features
	 *            features to include
	 * @param layer
	 *            layer
	 * @param code
	 *            tile code
	 * @param scale
	 *            scale
	 * @param panOrigin
	 *            When panning on the client, only this parameter changes. So we need to be aware of it as we calculate
	 *            the maxScreenEnvelope.
	 */
	public void fillTile(InternalTile tile, List<InternalFeature> features, VectorLayer layer, TileCode code,
			double scale, Coordinate panOrigin) {
		for (InternalFeature feature : features) {
			Geometry geometry = feature.getGeometry();

			if (!tile.getBbox(layer).contains(geometry.getCoordinate())) {
				addTileCode(tile, layer, geometry);
			} else {
				// clip feature if necessary
				if (exceedsScreenDimensions(feature, scale)) {
					InternalFeatureImpl vectorFeature = new InternalFeatureImpl(feature);
					tile.setClipped(true);
					vectorFeature.setClipped(true);
					Geometry clipped = JTS.toGeometry(getMaxScreenBbox(layer, code, scale, panOrigin)).intersection(
							feature.getGeometry());
					vectorFeature.setClippedGeometry(clipped);
					tile.addFeature(vectorFeature);
				} else {
					tile.addFeature(feature);
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Add dependent tiles for this geometry.
	 * 
	 * @param tile
	 *            tile in which to add dependent tile
	 * @param layer
	 *            layer
	 * @param geometry
	 *            geometry
	 */
	private void addTileCode(InternalTile tile, VectorLayer layer, Geometry geometry) {
		Coordinate c = geometry.getCoordinate();
		Envelope max = converterService.toInternal(layer.getLayerInfo().getMaxExtent());
		if (c != null && max.contains(c)) {
			int i = (int) ((c.x - layer.getLayerInfo().getMaxExtent().getX()) / tile.getTileWidth());
			int j = (int) ((c.y - layer.getLayerInfo().getMaxExtent().getY()) / tile.getTileHeight());
			int level = tile.getCode().getTileLevel();
			tile.addCode(level, i, j);
		}
	}

	/**
	 * The test that checks if clipping is needed.
	 * 
	 * @param f
	 *            feature to test
	 * @param scale
	 *            scale
	 * @return true if clipping is needed
	 */
	private boolean exceedsScreenDimensions(InternalFeature f, double scale) {
		Envelope env = f.getBounds();
		if (env.getWidth() * scale > MAXIMUM_TILE_COORDINATE) {
			return true;
		} else {
			return env.getHeight() * scale > MAXIMUM_TILE_COORDINATE;
		}
	}

	/**
	 * What is the maximum bounds in screen space? Needed for correct clipping calculation.
	 * 
	 * @param layer
	 *            layer
	 * @param code
	 *            tile code
	 * @param scale
	 *            scale
	 * @param panOrigin
	 *            pan origin
	 * @return max screen bbox
	 */
	private Envelope getMaxScreenBbox(VectorLayer layer, TileCode code, double scale, Coordinate panOrigin) {
		if (maxScreenBbox == null) {
			Envelope max = converterService.toInternal(layer.getLayerInfo().getMaxExtent());
			double div = Math.pow(2, code.getTileLevel());
			int tileWidthPx = (int) Math.ceil(scale * max.getWidth() / div);
			double tileWidth = tileWidthPx / scale;
			int tileHeightPx = (int) Math.ceil(scale * max.getWidth() / div);
			double tileHeight = tileHeightPx / scale;
			int nrOfTilesX = Math.max(1, MAXIMUM_TILE_COORDINATE / tileWidthPx);
			int nrOfTilesY = Math.max(1, MAXIMUM_TILE_COORDINATE / tileHeightPx);

			double x1 = panOrigin.x - nrOfTilesX * tileWidth;
			double x2 = x1 + (nrOfTilesX * tileWidth * 2);
			double y1 = panOrigin.y - nrOfTilesY * tileHeight;
			double y2 = y1 + (nrOfTilesY * tileHeight * 2);
			maxScreenBbox = new Envelope(x1, x2, y1, y2);
		}
		return maxScreenBbox;
	}
}