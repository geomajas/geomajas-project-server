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

import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.spatial.Bbox;

import java.util.Collection;

/**
 * <p>
 * General interface of a store containing features for a {@link org.geomajas.gwt.client.map.layer.VectorLayer}.
 * A {@link org.geomajas.gwt.client.map.store.VectorLayerStore} is meant
 * to store the necessary features for a specific layer, and even synchronize with the server if needed. All caches will
 * make use of spatial tiles, by whom to divide the space and more efficiently retrieve the necessary information.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface VectorLayerStore extends LayerStore<VectorTile> {

	/**
	 * Does this store currently contain the feature with given identifier?
	 *
	 * @param id
	 *            A feature's identifier.
	 * @return Return true or false.
	 */
	boolean contains(String id);

	/**
	 * Get a feature by it's identifier. If it can't be found, null is returned.
	 *
	 * @param id
	 *            The feature's identifier.
	 * @return Returns the feature or null.
	 */
	Feature getFeature(String id);

	/**
	 * Return a set of all features currently in the store.
	 */
	Collection<Feature> getFeatures();

	/**
	 * Add a new feature to the underlying data structure.
	 *
	 * @param feature
	 *            Feature object.
	 * @return Was the method successful in adding the feature or not?
	 */
	boolean addFeature(Feature feature);

	/**
	 * Remove a feature from the underlying data structure. (if it's in there)
	 *
	 * @param id
	 *            The feature's identifier.
	 * @return Returns the feature if it was found. Null otherwise.
	 */
	Feature removeFeature(String id);

	/**
	 * Return the current size of the store. How many features are stored here?
	 *
	 * @return
	 */
	int size();

	/**
	 * Make a certain query on the store. The cache will go over all tiles intersecting the given bounding box. If some
	 * of these tiles are not yet present in the cache, they will be fetched from the server with the given filter.
	 *
	 * @param bbox
	 *            The bounding box wherein we are to search for tiles.
	 * @param filter
	 *            In case a node is missing, it is fetched with this extra filter. Is usually null though. TODO: check
	 *            if this is true.
	 * @param onDelete
	 *            The function to be applied on the tiles deleted by this sync. This function should take a node as
	 *            parameter.
	 * @param onUpdate
	 *            The function to be applied on the tiles intersecting the given bounding box. This function should take
	 *            a node as parameter.
	 */
	void queryAndSync(Bbox bbox, String filter, TileFunction<VectorTile> onDelete,
			TileFunction<VectorTile> onUpdate);

	/**
	 * Make a certain query on the cache. The cache will go over all tiles intersecting the given bounding box. No
	 * synchronization with the server is performed.
	 *
	 * @param bbox
	 *            The bounding box wherein we are to search for tiles, and apply the function.
	 * @param callback
	 *            The function to be applied on the tiles intersecting the given bounding box. This function should take
	 *            a node as parameter.
	 */
	void query(Bbox bbox, TileFunction<VectorTile> callback);

	/**
	 * Creates a new feature instance. This does not add the feature to the store yet !
	 * @return the new feature
	 */
	Feature newFeature();
}
