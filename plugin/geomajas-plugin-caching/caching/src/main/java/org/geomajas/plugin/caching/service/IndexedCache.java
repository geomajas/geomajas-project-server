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

package org.geomajas.plugin.caching.service;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Indexed cache which delegates to the caching and indexing services when necessary.
 *
 * @author Joachim Van der Auwera
 */
public class IndexedCache {

	private CacheService cache;
	private CacheIndexService index;

	public IndexedCache(CacheService cache, CacheIndexService index) {
		this.cache = cache;
		this.index = index;
	}

	/**
	 * Put a spatial object in the cache and index it.
	 *
	 * @param key key for object
	 * @param object object itself
	 * @param geometry geometry for object
	 */
	public void put(String key, Object object, Geometry geometry) {
		index.put(key, geometry);
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
	 * Drop the entire cache.
	 */
	public void drop() {
		cache.drop();
	}

	/**
	 * Invalidate all entries which (may) overlap with the given geometry.
	 *
	 * @param geometry geometry to test
	 */
	public void invalidate(Geometry geometry) {
		for (String key : index.getOverlappingKeys(geometry)) {
			remove(key);
		}
	}
}
