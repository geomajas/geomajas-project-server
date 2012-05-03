/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.annotation.Api;
import org.geomajas.layer.Layer;

/**
 * Service which manages the handling of the layer and type specific caches.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CacheManagerService {

	/**
	 * Put an object into the cache.
	 * <p/>
	 * This will overwrite any pre-existing object for the key.
	 *
	 * @param layer layer which contains the object
	 * @param category cache category
	 * @param key key for the object
	 * @param object object
	 * @param envelope envelope for object
	 */
	void put(Layer layer, CacheCategory category, String key, Object object, Envelope envelope);

	/**
	 * Get the object with given key from the cache.
	 *
	 * @param layer layer which contains the object
	 * @param category cache category
	 * @param key key for object
	 * @return object with requested key or null if the object does not exist
	 */
	Object get(Layer layer, CacheCategory category, String key);

	/**
	 * Get the object with given key and given type from the cache.
	 *
	 * @param layer layer which contains the object
	 * @param category cache category
	 * @param key key for object
	 * @param type type for the result
	 * @return object with requested key and type or null if the object does not exist or has a different type
	 */
	<TYPE> TYPE get(Layer layer, CacheCategory category, String key, Class<TYPE> type);

	/**
	 * Remove the cached object with given key for the given layer and cache category.
	 *
	 * @param layer layer
	 * @param category cache category
	 * @param key cache key
	 */
	void remove(Layer layer, CacheCategory category, String key);

	/**
	 * Drop the cache for the specific category and layer.
	 *
	 * @param layer layer for which a category needs to be dropped
	 * @param category cache category to drop
	 */
	void drop(Layer layer, CacheCategory category);

	/**
	 * Drop all caches for the given layer.
	 *
	 * @param layer layer for which the caches need to be dropped
	 */
	void drop(Layer layer);

	/**
	 * Invalidate the cached objects for a specific geometry in a cache category.
	 *
	 * @param layer layer for which cached objects need to be invalidated
	 * @param category cache category
	 * @param envelope envelope for which overlapping objects need to be invalidated
	 */
	void invalidate(Layer layer, CacheCategory category, Envelope envelope);

	/**
	 * Invalidate the cached objects for a specific geometry in a cache category.
	 *
	 * @param layer layer for which cached objects need to be invalidated
	 * @param envelope envelope for which overlapping objects need to be invalidated
	 */
	void invalidate(Layer layer, Envelope envelope);

	/**
	 * Invalidate all the cached objects for a specific layer.
	 *
	 * @param layer layer for which cached objects need to be invalidated
	 */
	void invalidate(Layer layer);
}
