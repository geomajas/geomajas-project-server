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

package org.geomajas.smartgwt.client.map.store;

import org.geomajas.smartgwt.client.map.cache.tile.RasterTile;
import org.geomajas.smartgwt.client.map.cache.tile.TileFunction;
import org.geomajas.smartgwt.client.map.layer.RasterLayer;
import org.geomajas.smartgwt.client.spatial.Bbox;

/**
 * <p> General interface of a store containing features for a {@link RasterLayer}. A <code>RasterLayerStore</code> keeps
 * track of the raster tile cache for a specific layer, and synchronizes with the server. All caches will make use of
 * raster tiles, by whom to divide the space and more efficiently retrieve the necessary information. </p>
 *
 * @author Jan De Moerloose
 */
public interface RasterLayerStore extends LayerStore<RasterTile> {

	/**
	 * Synchronize the node-list (tiles) by applying a certain bounding box. This synchronization will result in some
	 * deleting, and some updating of spatial tiles. This method then applies functions on these spatial tiles.
	 *
	 * @param bounds The bounds wherein to look for tiles.
	 * @param onDelete A function that should be applied on every tile that is deleted while synchronizing with the
	 * server.
	 * @param onUpdate A function that should be applied on every tile that is updated while synchronizing with the
	 * server.
	 */
	void applyAndSync(Bbox bounds, TileFunction<RasterTile> onDelete, TileFunction<RasterTile> onUpdate);

	RasterLayer getLayer();
}
