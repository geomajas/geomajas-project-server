/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.map.cache;

import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.layer.tile.TileCode;

/**
 * <p>
 * General interface of a spatial cache. A cache is meant to store the necessary features for a specific layer, and even
 * sync with the server if needed. All caches will make use of spatial tiles, by whom to divide the space and more
 * efficiently retrieve the necessary information.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface SpatialCache extends VectorLayerStore {

	/**
	 * Adds the tile with the specified code to the cache or returns the tile if it's already in the cache.
	 *
	 * @param tileCode
	 *            A {@link TileCode} instance.
	 * @return the previous tile with same code from the cache if any
	 */
	VectorTile addTile(TileCode tileCode);

	/**
	 * Return the <code>VectorLayer</code> for this cache.
	 *
	 * @return vector layer
	 */
	VectorLayer getLayer();
	
	/**
	 * Return the layer bounds for this cache.
	 * 
	 * @return the bounding box of the layer
	 */
	Bbox getLayerBounds();
}
