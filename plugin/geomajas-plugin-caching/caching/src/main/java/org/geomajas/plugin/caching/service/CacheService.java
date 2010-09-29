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

import org.geomajas.global.Api;

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
	void drop();
}
