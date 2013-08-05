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

import org.geomajas.smartgwt.client.map.cache.tile.TileFunction;
import org.geomajas.smartgwt.client.map.cache.tile.VectorTile;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.map.feature.LazyLoadCallback;
import org.geomajas.smartgwt.client.spatial.Bbox;

/**
 * <p>
 * General interface of a store containing features for a {@link org.geomajas.smartgwt.client.map.layer.VectorLayer}.
 * A {@link VectorLayerStore} is meant
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
	 * Get a feature by it's identifier.
	 *
	 * @param id
	 *            The feature's identifier.
	 * @param featureIncludes what data should be available in the features
	 * See {@link org.geomajas.global.GeomajasConstant}.
	 * @param callback callback which gets the features
	 */
	void getFeature(String id, int featureIncludes, LazyLoadCallback callback);

	/**
	 * Get a feature by it's identifier. If it can't be found, null is returned. The feature may require additional
	 * lazy loading.
	 *
	 * @param id
	 *            The feature's identifier.
	 * @return feature which may require additional lazy loading
	 */
	Feature getPartialFeature(String id);

	/**
	 * Return a set of all features currently in the store.
	 *
	 * @param featureIncludes what data should be available in the features. 
	 * See {@link org.geomajas.global.GeomajasConstant}.
	 * @param callback callback which gets the features
	 */
	void getFeatures(int featureIncludes, LazyLoadCallback callback);

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
	 * @return number of features in the store
	 */
	int size();

	/**
	 * Make a certain query on the store. The cache will go over all tiles intersecting the given bounding box. If some
	 * of these tiles are not yet present in the cache, they will be fetched from the server with the given filter.
	 *
	 * @param bbox
	 *            The bounding box wherein we are to search for tiles.
	 * @param filter
	 *            Additional filter which can be applied in the query. This is usually null, but could for example be
	 *            used when a node is missing.
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
