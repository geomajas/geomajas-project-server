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

package org.geomajas.plugin.caching.service;

import java.io.Serializable;

/**
 * Context for a cached object, used to assure identical keys denote identical objects.
 *
 * @author Joachim Van der Auwera
 */
public interface CacheContext extends Serializable {

	/**
	 * Key for storing the security context in the cache.
	 */
	String SECURITY_CONTEXT_KEY = "$securityContext"; // SavedAuthorization

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
