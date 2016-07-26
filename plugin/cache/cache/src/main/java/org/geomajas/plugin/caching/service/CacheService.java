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

package org.geomajas.plugin.caching.service;

import org.geomajas.annotation.Api;

/**
 * A cache which is managed using the {@link org.geomajas.plugin.caching.service.CacheManagerService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CacheService {

	/**
	 * Put an object into the cache.
	 * <p/>
	 * This will overwrite any pre-existing object for the key.
	 * 
	 * @param key key for the object
	 * @param object object
	 */
	void put(String key, Object object);

	/**
	 * Get the object with given key from the cache.
	 *
	 * @param key key for object
	 * @return object with requested key or null if the object does not exist
	 */
	Object get(String key);

	/**
	 * Get the object with given key and given type from the cache.
	 *
	 * @param key key for object
	 * @param type type for the result
	 * @return object with requested key and type or null if the object does not exist or has a different type
	 */
	<TYPE> TYPE get(String key, Class<TYPE> type);

	/**
	 * Remove a spatial object from the cache.
	 *
	 * @param key key to remove
	 */
	void remove(String key);

	/**
	 * Remove all objects from this cache.
	 */
	void clear();

	/**
	 * Clean up cache to prepare for removal.
	 */
	void drop();
}
