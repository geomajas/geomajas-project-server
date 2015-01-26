/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Indexed cache which delegates to the caching and indexing services when necessary.
 *
 * @author Joachim Van der Auwera
 */
public class IndexedCache {

	private final Logger log = LoggerFactory.getLogger(IndexedCache.class);

	private final CacheService cache;
	private final CacheIndexService index;

	public IndexedCache(CacheService cache, CacheIndexService index) {
		this.cache = cache;
		this.index = index;
	}

	/**
	 * Put a spatial object in the cache and index it.
	 *
	 * @param key key for object
	 * @param object object itself
	 * @param envelope envelope for object
	 */
	public void put(String key, Object object, Envelope envelope) {
		index.put(key, envelope);
		cache.put(key, object);
	}

	/**
	 * Get the spatial object from the cache.
	 *
	 * @param key key to get object for
	 * @param type type of object which should be returned
	 * @return object for key or null if object does not exist or is a different type
	 */
	public <TYPE> TYPE get(String key, Class<TYPE> type) {
		return cache.get(key, type);
	}

	/**
	 * Remove a specific spatial object from the cache.
	 *
	 * @param key key for object to remove
	 */
	public void remove(String key) {
		cache.remove(key);
		index.remove(key);
	}

	/**
	 * Remove all entries from the cache.
	 */
	public void clear() {
		cache.clear();
		index.clear();
	}

	/**
	 * Drop the entire cache.
	 */
	public void drop() {
		cache.drop();
		index.drop();
	}

	/**
	 * Invalidate all entries which (may) overlap with the given geometry.
	 *
	 * @param envelope envelope to test
	 */
	public void invalidate(Envelope envelope) {
		List<String> keys = index.getOverlappingKeys(envelope);
		if (CacheIndexService.ALL_KEYS == keys) {
			log.debug("clear all keys from cache");
			clear();
		} else {
			log.debug("invalidate keys {}", keys);
			for (String key : keys) {
				remove(key);
			}
		}
	}

	/**
	 * Get underlying {@link CacheService} for testing purposes.
	 *
	 * @return CacheService implementation
	 */
	CacheService getCacheForTesting() {
		return cache;
	}
}
