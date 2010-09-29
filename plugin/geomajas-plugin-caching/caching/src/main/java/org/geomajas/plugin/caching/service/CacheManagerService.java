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
import org.geomajas.layer.Layer;

/**
 * Service which manages the handling of the layer and type specific caches.
 *
 * @author Joachim Van der Auwera
 */
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
	 * @param geometry geometry for object
	 */
	void put(Layer layer, CacheCategory category, String key, Object object, Geometry geometry);

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
	 * @param geometry geometry for which overlapping objects need to be invalidated
	 */
	void invalidate(Layer layer, CacheCategory category, Geometry geometry);

	/**
	 * Invalidate the cached objects for a specific geometry in a cache category.
	 *
	 * @param layer layer for which cached objects need to be invalidated
	 * @param geometry geometry for which overlapping objects need to be invalidated
	 */
	void invalidate(Layer layer, Geometry geometry);
}
