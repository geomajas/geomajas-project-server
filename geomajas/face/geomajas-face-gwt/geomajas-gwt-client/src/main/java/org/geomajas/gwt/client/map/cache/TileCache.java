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

package org.geomajas.gwt.client.map.cache;

import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.rendering.tile.TileCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ???
 *
 * @author check subversion
 */
public class TileCache implements SpatialCache {

	protected VectorLayer layer;

	protected Bbox layerBounds;

	protected int maximumTileLevel;

	protected Map<String, VectorTile> tiles;

	protected Map<String, Feature> features;

	private int currentTileLevel; // is never set... can't be right???

	private int currentMinX;

	private int currentMinY;

	private int currentMaxX;

	private int currentMaxY;

	protected List<TileCode> currentTileCodes;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * The default <code>SpatialCache</code> implementation. Caches <code>VectorTile</code>s, based on bounds.
	 *
	 * @param layer
	 *            Reference to the <code>VectorLayer</code> object.
	 * @param layerBounds
	 *            The maximum bounds for this cache.
	 * @param maximumTileLevel
	 *            The maximum tiling depth allowed in this cache.
	 */
	public TileCache(VectorLayer layer, Bbox layerBounds, int maximumTileLevel) {
		this.layer = layer;
		this.layerBounds = layerBounds;
		this.maximumTileLevel = maximumTileLevel;
		tiles = new HashMap<String, VectorTile>();
		features = new HashMap<String, Feature>();
		currentMinX = 0;
		currentMinY = 0;
		currentMaxX = 0;
		currentMaxY = 0;
	}

	// -------------------------------------------------------------------------
	// SpatialCache implementation:
	// -------------------------------------------------------------------------

	/**
	 * Adds the tile with the specified code to the cache or simply returns the tile if it's already in the cache.
	 *
	 * @param tileCode
	 *            A {@link TileCode} instance.
	 */
	public VectorTile addTile(TileCode tileCode) {
		VectorTile tile = (VectorTile) tiles.get(tileCode.toString());
		if (tile == null) {
			tile = new VectorTile(tileCode, calcBoundsForTileCode(tileCode), this);
			tiles.put(tileCode.toString(), tile);
		}
		return tile;
	}

	public List<Feature> getFeaturesByCode(TileCode code) {
		if (code != null) {
			VectorTile tile = (VectorTile) tiles.get(code.toString());
			if (tile != null) {
				return tile.getFeatures();
			}
		}
		return null;
	}

	public VectorLayer getLayer() {
		return layer;
	}

	// -------------------------------------------------------------------------
	// VectorLayerStore implementation:
	// -------------------------------------------------------------------------

	public boolean contains(String id) {
		return features.containsKey(id);
	}

	public Feature getFeature(String id) {
		return features.get(id);
	}

	public Collection<Feature> getFeatures() {
		return features.values();
	}

	public boolean addFeature(Feature feature) {
		if (feature != null && !features.containsKey(feature.getId())) {
			features.put(feature.getId(), feature);
		}
		return false;
	}

	public Feature removeFeature(String id) {
		return features.remove(id);
	}

	public int size() {
		return features.size();
	}

	public Feature newFeature() {
		return new Feature(getLayer());
	}

	public void query(Bbox bbox, TileFunction callback) {
		List<VectorTile> setTiles = new ArrayList<VectorTile>();

		// Create the full list of tile on which to operate:
		List<TileCode> tileCodes = calcCodesForBounds(bbox);
		for (TileCode tileCode : tileCodes) {
			if (tiles.containsKey(tileCode.toString())) {
				VectorTile tile = (VectorTile) tiles.get(tileCode.toString());
				if (!setTiles.contains(tile)) {
					setTiles.add(tile);
				}
				// Also process tiles of features that partly lie in this tile but were assigned to another tile.
				List<TileCode> codesExtraFeatures = tile.getCodes();
				for (TileCode extraCode : codesExtraFeatures) {
					VectorTile extraTile = (VectorTile) tiles.get(extraCode.toString());
					if (!setTiles.contains(extraTile)) {
						setTiles.add(extraTile);
					}
				}
			}
		}

		// Now execute the SpatialNodeFunction function on the entire list:
		for (VectorTile tile : setTiles) {
			callback.execute(tile);
		}
	}

	public void queryAndSync(Bbox bbox, String filter, TileFunction onDelete, TileFunction onUpdate) {
		if (!layer.getMapModel().getMapView().isPanning() || isClear()) {
			// Delete all tiles
			for (VectorTile tile : tiles.values()) {
				tile.cancel();
				onDelete.execute(tile);
			}
			tiles.clear();
		}

		// Only fetch when inside the layer bounds:
		if (bbox.intersects(layerBounds)) {
			// Check tile level:
			int bestLevel = calculateTileLevel(bbox);
			if (bestLevel != currentTileLevel) {
				currentTileLevel = bestLevel;
			}

			// Find needed tile codes:
			List<TileCode> tileCodes = calcCodesForBounds(bbox);

			// Make a clone, as we are going to modify the actual node map:
			Map<String, VectorTile> currentNodes = new HashMap<String, VectorTile>(tiles);
			for (TileCode tileCode : tileCodes) {
				if (!currentNodes.containsKey(tileCode.toString())) {
					// Add the node:
					VectorTile tile = addTile(tileCode);
					tile.fetch(filter, onUpdate);
					tile.applyConnected(filter, onUpdate);
				} else {
					VectorTile tile = currentNodes.get(tileCode.toString());
					tile.apply(onUpdate);
					tile.applyConnected(filter, onUpdate);
				}
			}
		}
	}

	// -------------------------------------------------------------------------
	// LayerStore implementation:
	// -------------------------------------------------------------------------

	public void clear() {
		features = new HashMap<String, Feature>();
	}

	public boolean isClear() {
		return features.isEmpty();
	}

	public Collection<VectorTile> getTiles() {
		return tiles.values();
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Calculate the best tile level to use for a certain view-bounds.
	 */
	protected int calculateTileLevel(Bbox bounds) {
		int tileLevel = 0;
		double baseX = layerBounds.getWidth();
		double baseY = layerBounds.getHeight();
		while (baseX > bounds.getWidth() && baseY > bounds.getHeight() && tileLevel < maximumTileLevel) {
			tileLevel++;
			baseX /= 2.0;
			baseY /= 2.0;
		}
		return tileLevel;
	}

	/**
	 * Calculate the exact bounding box for a tile, given it's tile-code:
	 */
	protected Bbox calcBoundsForTileCode(TileCode tileCode) {
		// Calculate tile width and height for tileLevel=tileCode.getTileLevel()
		double div = Math.pow(2, tileCode.getTileLevel());
		double scale = layer.getMapModel().getMapView().getCurrentScale();
		double tileWidth = Math.ceil((scale * layerBounds.getWidth()) / div) / scale;
		double tileHeight = Math.ceil((scale * layerBounds.getHeight()) / div) / scale;

		// Now calculate indexes, and return bbox:
		double x = layerBounds.getX() + tileCode.getX() * tileWidth;
		double y = layerBounds.getY() + tileCode.getY() * tileHeight;
		return new Bbox(x, y, tileWidth, tileHeight);
	}

	/**
	 * @private Saves the complete array of TileCode objects for the given bounds (and the current scale).
	 */
	protected List<TileCode> calcCodesForBounds(Bbox bounds) {
		// Calculate tile width and height for tileLevel=currentTileLevel
		double div = Math.pow(2, currentTileLevel); // tile level must be correct!
		double scale = layer.getMapModel().getMapView().getCurrentScale();
		double tileWidth = Math.ceil((scale * layerBounds.getWidth()) / div) / scale;
		double tileHeight = Math.ceil((scale * layerBounds.getHeight()) / div) / scale;

		// For safety (to prevent division by 0):
		List<TileCode> codes = new ArrayList<TileCode>();
		if (tileWidth == 0 || tileHeight == 0) {
			return codes;
		}

		// Calculate bounds relative to extents:
		Bbox clippedBounds = bounds.intersection(layerBounds);
		if (clippedBounds == null) {
			// TODO throw error? If this is null, then the server configuration is incorrect.
			return codes;
		}
		double relativeBoundX = Math.abs(clippedBounds.getX() - layerBounds.getX());
		double relativeBoundY = Math.abs(clippedBounds.getY() - layerBounds.getY());
		currentMinX = (int) Math.floor(relativeBoundX / tileWidth);
		currentMinY = (int) Math.floor(relativeBoundY / tileHeight);
		currentMaxX = (int) Math.ceil((relativeBoundX + clippedBounds.getWidth()) / tileWidth) - 1;
		currentMaxY = (int) Math.ceil((relativeBoundY + clippedBounds.getHeight()) / tileHeight) - 1;

		// Now fill the list with the correct codes:
		for (int x = currentMinX; x <= currentMaxX; x++) {
			for (int y = currentMinY; y <= currentMaxY; y++) {
				codes.add(new TileCode(currentTileLevel, x, y));
			}
		}
		return codes;
	}
}
