/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering.strategy;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.global.GeomajasException;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geotools.geometry.jts.JTS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final Logger log = LoggerFactory.getLogger(TiledFeatureService.class);

	/**
	 * Helps determine when a feature is too big and must therefore be clipped.
	 */
	private static final int MAXIMUM_TILE_COORDINATE = 10000;

	private static final double ROUNDING_TOLERANCE = .0000000005;

	/**
	 * Put features in a tile. This will assure all features are only added in one tile. When a feature's unique tile
	 * is different from this one a link is added in the tile.
	 *
	 * @param tile
	 *            tile to put features in
	 * @param maxTileExtent
	 *            the maximum tile extent
	 * @throws GeomajasException oops
	 */
	public void fillTile(InternalTile tile, Envelope maxTileExtent)
			throws GeomajasException {
		List<InternalFeature> origFeatures = tile.getFeatures();
		tile.setFeatures(new ArrayList<InternalFeature>());
		for (InternalFeature feature : origFeatures) {
			if (!addTileCode(tile, maxTileExtent, feature.getGeometry())) {
				log.debug("add feature");
				tile.addFeature(feature);
			}
		}
	}

	/**
	 * Apply clipping to the features in a tile. The tile and its features should already be in map space.
	 *
	 * @param tile
	 *            tile to put features in
	 * @param scale
	 *            scale
	 * @param panOrigin
	 *            When panning on the client, only this parameter changes. So we need to be aware of it as we calculate
	 *            the maxScreenEnvelope.
	 * @throws GeomajasException oops
	 */
	public void clipTile(InternalTile tile, double scale, Coordinate panOrigin) throws GeomajasException {
		log.debug("clipTile before {}", tile);
		List<InternalFeature> orgFeatures = tile.getFeatures();
		tile.setFeatures(new ArrayList<InternalFeature>());
		Geometry maxScreenBbox = null; // The tile's maximum bounds in screen space. Used for clipping.
		for (InternalFeature feature : orgFeatures) {
			// clip feature if necessary
			if (exceedsScreenDimensions(feature, scale)) {
				log.debug("feature {} exceeds screen dimensions", feature);
				InternalFeatureImpl vectorFeature = (InternalFeatureImpl) feature.clone();
				tile.setClipped(true);
				vectorFeature.setClipped(true);
				if (null == maxScreenBbox) {
					maxScreenBbox = JTS.toGeometry(getMaxScreenEnvelope(tile, panOrigin));
				}
				Geometry clipped = maxScreenBbox.intersection(feature.getGeometry());
				vectorFeature.setClippedGeometry(clipped);
				tile.addFeature(vectorFeature);
			} else {
				tile.addFeature(feature);
			}
		}
		log.debug("clipTile after {}", tile);
	}


	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Add dependent tiles for this geometry.
	 * <p/>
	 * It checks the correct tile for the first coordinate in the geometry which is inside the tile bounds. When no
	 * coordinates are inside the tile bounds, the feature is put in tile 0-0 (as this means the feature is bigger
	 * than the tiles).
	 *
	 * @param tile
	 *            tile in which to add dependent tile
	 * @param tileBounds tile bounds in map coordinates
	 * @param geometry geometry for feature
	 * @return true when tilecode was added and feature will be contained in another tile
	 */
	private boolean addTileCode(InternalTile tile, Envelope tileBounds, Geometry geometry) {
		if (log.isDebugEnabled()) {
			log.debug("addTileCode {} {}", tileBounds, geometry);
		}
		TileCode tileCode = tile.getCode();
		int tileX = tileCode.getX();
		int tileY = tileCode.getY();
		for (Coordinate coordinate : geometry.getCoordinates()) {
			if (tileBounds.contains(coordinate)) {
				// We jump through some hoops to (try to) avoid rounding problems.
				// This may result in having the feature in two adjacent tiles, but that should still be better than
				// loosing the feature. Just hope the tolerance is small enough.
				double xd = ((coordinate.x - tileBounds.getMinX()) / tile.getTileWidth());
				double yd = ((coordinate.y - tileBounds.getMinY()) / tile.getTileHeight());
				int x1 = (int) (xd);
				int x2 = (int) (xd + ROUNDING_TOLERANCE);
				int y1 = (int) (yd);
				int y2 = (int) (yd + ROUNDING_TOLERANCE);
				if (log.isDebugEnabled()) {
					log.debug("feature in tile " + x1 + "-" + y1 + " or " + x2 + "-" + y2);
				}

				// check for possible rounding problems, when i,j is "this" tile
				if ((x1 == tileX || x2 == tileX) && (y1 == tileY || y2 == tileY)) {
					return false;
				}

				int level = tile.getCode().getTileLevel();
				tile.addCode(level, x1, y1);
				return true;
			}
		}
		// all points of the geometry are outside all tiles. Should be put in tile 0,0
		if (0 == tileX && 0 == tileY) {
			// this is tile 0,0, so leave it here
			return false;
		} else {
			int level = tile.getCode().getTileLevel();
			tile.addCode(level, 0, 0);
			return true;
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
		return (env.getWidth() * scale > MAXIMUM_TILE_COORDINATE) ||
				(env.getHeight() * scale > MAXIMUM_TILE_COORDINATE);
	}

	/**
	 * What is the maximum bounds in screen space? Needed for correct clipping calculation.
	 *
	 * @param tile
	 *            tile
	 * @param panOrigin
	 *            pan origin
	 * @return max screen bbox
	 */
	private Envelope getMaxScreenEnvelope(InternalTile tile, Coordinate panOrigin) {
		int nrOfTilesX = Math.max(1, MAXIMUM_TILE_COORDINATE / tile.getScreenWidth());
		int nrOfTilesY = Math.max(1, MAXIMUM_TILE_COORDINATE / tile.getScreenHeight());

		double x1 = panOrigin.x - nrOfTilesX * tile.getTileWidth();
		// double x2 = x1 + (nrOfTilesX * tileWidth * 2);
		double x2 = panOrigin.x + nrOfTilesX * tile.getTileWidth();
		double y1 = panOrigin.y - nrOfTilesY * tile.getTileHeight();
		// double y2 = y1 + (nrOfTilesY * tileHeight * 2);
		double y2 = panOrigin.y + nrOfTilesY * tile.getTileHeight();
		return new Envelope(x1, x2, y1, y2);
	}
}