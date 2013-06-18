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
package org.geomajas.service;

import org.geomajas.annotation.Api;


/**
 * Service to register objects in a cache store. Objects are stored in different caches identified by a cache id.
 * 
 * @author Oliver May
 * @since 1.13.0
 */
@Api
public interface CacheService {

	/**
	 * Default time to live.
	 */
	long DEFAULT_TTL = 86400 * 1000;
	
	/**
	 * Put an object in the cache, the default lifetime is 24 hours.
	 * 
	 * @param cache the cache id
	 * @param key the key
	 * @param value the object
	 */
	void put(String cache, Object key, Object value);
	
	
	/**
	 * Put an object in the cache for a given limetolive.
	 * 
	 * @param cache the cache id
	 * @param key the key
	 * @param value the object
	 * @param timeToLive the minimum amount of time the item should stay in the cache
	 */
	void put(String cache, Object key, Object value, long timeToLive);
	
	/**
	 * Retrieve an object from a cache.
	 * 
	 * @param cache the cache id
	 * @param key the key
	 * @return the object
	 */
	Object get(String cache, Object key);
	
	/**
	 * Get the object with the given key and given type from the cache.
	 * 
	 * @param <TYPE> the type of the result
	 * @param cache the cache id
	 * @param key the key
	 * @param type the type of the result
	 * @return the object with the requested type or null if the object doesn't exist or is of the wrong type
	 */
	<TYPE> TYPE get(String cache, Object key, Class<TYPE> type);
	
	/**
	 * Remove an object from the cache.
	 * 
	 * @param cache the cache id
	 * @param key the key
	 */
	void remove(String cache, Object key);
	
	/**
	 * Remove all objects from the cache.
	 * 
	 * @param cache the cache id
	 */
	void clear(String cache);
	
}
