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

import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.map.store.VectorLayerStore;
import org.geomajas.layer.tile.TileCode;

import java.util.List;

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
	 * Adds the tile with the specified code to the cache or simply returns the tile if it's already in the cache.
	 *
	 * @param tileCode
	 *            A {@link TileCode} instance.
	 */
	VectorTile addTile(TileCode tileCode);

	/**
	 * Return an array of all features in a certain {@link org.geomajas.gwt.client.map.cache.tile.AbstractVectorTile}.
	 * This node is selected through it's unique {@link SpatialCode}.
	 *
	 * @param code
	 *            A {@link SpatialCode} instance. If the node with this code is not present, an empty array will be
	 *            returned.
	 */
	List<Feature> getFeaturesByCode(TileCode code);

	/**
	 * Return the <code>VectorLayer</code> reference.
	 */
	VectorLayer getLayer();
}
