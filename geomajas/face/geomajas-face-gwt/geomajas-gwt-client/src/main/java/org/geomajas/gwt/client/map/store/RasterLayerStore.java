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

package org.geomajas.gwt.client.map.store;

import org.geomajas.gwt.client.map.cache.tile.RasterTile;
import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.spatial.Bbox;

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
