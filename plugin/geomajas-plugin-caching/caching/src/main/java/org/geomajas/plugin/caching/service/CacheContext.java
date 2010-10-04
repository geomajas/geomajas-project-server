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

/**
 * Context for a cached object, used to assure identical keys denote identical objects.
 *
 * @author Joachim Van der Auwera
 */
public interface CacheContext {

	/**
	 * Put an object in the context.
	 *
	 * @param key object key
	 * @param object object value
	 */
	void put(String key, Object object);

	/**
	 * Get an object from the context.
	 *
	 * @param key object key
	 * @return value from context or null
	 */
	Object get(String key);

	/**
	 * Get an object from the context.
	 *
	 * @param key object key
	 * @param type type for the object
	 * @param <TYPE> type for the object
	 * @return value from context or null when no value or value has wrong type
	 */
	<TYPE> TYPE get(String key, Class<TYPE> type);
}
