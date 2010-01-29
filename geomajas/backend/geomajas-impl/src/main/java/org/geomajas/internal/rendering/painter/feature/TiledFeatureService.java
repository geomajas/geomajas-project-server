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

package org.geomajas.internal.rendering.painter.feature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.geometry.Bbox;
import org.geomajas.internal.layer.feature.VectorFeature;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.RenderedFeature;
import org.geomajas.rendering.tile.RenderedTile;
import org.geomajas.rendering.tile.TileCode;
import org.geomajas.service.BboxService;
import org.geotools.geometry.jts.JTS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * This service puts featues in a tile. In that case, not all features which overlap the tile are included as-is.
 * </p>
 * <p>
 * For example, when features are way too big, they are clipped. (note: since
 * the normal <code>RenderedFeature</code> object does not support clipped
 * features, an extension, called <code>VectorFeature</code> is used instead).
 * </p>
 * <p>
 * It also keeps track of dependency
 * between tiles. Tiles in Geomajas are dependent in the sense that each feature
 * lies in only 1 tile, even if it's geometry crosses the bounds of the tile. To
 * discern what tile a feature belongs to, the position of the first coordinate
 * is used. The other tiles that the geometry in question spans, are considered
 * dependent tiles.
 * </p>
 *
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
@Component
public class TiledFeatureService {

	@Autowired
	private BboxService bboxService;

	/**
	 * Helps determine when a feature is too big and must therefore be clipped.
	 */
	private static int MAXIMUM_TILE_COORDINATE = 10000;

	/**
	 * The tile's maximum bounds in screen space. Needed for clipping
	 * calculations.
	 */
	private Bbox maxScreenBbox;

	/**
	 * Paint an individual feature. In other words transform the generic feature
	 * object into a <code>VectorFeature</code>, and prepare it for a certain
	 * tile.
	 *
	 * @param tile tile to put features in
	 * @param features features to include
	 * @param layer layer
	 * @param code tile code
	 * @param scale scale
	 * @param panOrigin When panning on the client, only this parameter changes. So we need to be
	 * aware of it as we calculate the maxScreenEnvelope.
	 */
	public void fillTile(RenderedTile tile, List<RenderedFeature> features, VectorLayer layer, TileCode code,
			double scale, Coordinate panOrigin) {
		for (RenderedFeature feature : features) {
			Geometry geometry = feature.getGeometry();

			if (!bboxService.contains(tile.getBbox(layer), geometry.getCoordinate())) {
				addTileCode(tile, layer, geometry);
			} else {
				// clip feature if necessary
				if (exceedsScreenDimensions(feature, scale)) {
					VectorFeature vectorFeature = new VectorFeature(feature);
					tile.setClipped(true);
					vectorFeature.setClipped(true);
					Geometry clipped =
							JTS.toGeometry(bboxService.toEnvelope(getMaxScreenBbox(layer, code, scale, panOrigin))).
									intersection(feature.getGeometry());
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
	 * @param tile tile in which to add dependent tile
	 * @param layer layer
	 * @param geometry geometry
	 */
	private void addTileCode(RenderedTile tile, VectorLayer layer, Geometry geometry) {
		Coordinate c = geometry.getCoordinate();
		if (c != null && bboxService.contains(layer.getLayerInfo().getMaxExtent(), c)) {
			int i = (int) ((c.x - layer.getLayerInfo().getMaxExtent().getX()) / tile.getTileWidth());
			int j = (int) ((c.y - layer.getLayerInfo().getMaxExtent().getY()) / tile.getTileHeight());
			int level = tile.getCode().getTileLevel();
			tile.addCode(level, i, j);
		}
	}

	/**
	 * The test that checks if clipping is needed.
	 *
	 * @param f feature to test
	 * @param scale scale
	 * @return true if clipping is needed
	 */
	private boolean exceedsScreenDimensions(RenderedFeature f, double scale) {
		Bbox env = f.getBounds();
		if (env.getWidth() * scale > MAXIMUM_TILE_COORDINATE) {
			return true;
		} else {
			return env.getHeight() * scale > MAXIMUM_TILE_COORDINATE;
		}
	}

	/**
	 * What is the maximum bounds in screen space? Needed for correct clipping calculation.
	 *
	 * @param layer layer
	 * @param code tile code
	 * @param scale scale
	 * @param panOrigin pan origin
	 * @return max screen bbox
	 */
	private Bbox getMaxScreenBbox(VectorLayer layer, TileCode code, double scale, Coordinate panOrigin) {
		if (maxScreenBbox == null) {
			Bbox max = layer.getLayerInfo().getMaxExtent();
			double div = Math.pow(2, code.getTileLevel());
			int tileWidthPx = (int) Math.ceil(scale * max.getWidth() / div);
			double tileWidth = tileWidthPx / scale;
			int tileHeightPx = (int) Math.ceil(scale * max.getWidth() / div);
			double tileHeight = tileHeightPx / scale;
			int nrOfTilesX = Math.max(1, MAXIMUM_TILE_COORDINATE / tileWidthPx);
			int nrOfTilesY = Math.max(1, MAXIMUM_TILE_COORDINATE / tileHeightPx);
			maxScreenBbox = new Bbox(panOrigin.x - nrOfTilesX * tileWidth, panOrigin.y - nrOfTilesY * tileHeight,
					nrOfTilesX * tileWidth * 2, nrOfTilesY * tileHeight * 2);
		}
		return maxScreenBbox;
	}
}