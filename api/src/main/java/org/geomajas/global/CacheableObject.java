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

package org.geomajas.global;

import org.geomajas.annotation.Api;

/**
 * Interface to implement for cache-context friendly objects.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public interface CacheableObject {

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash
	 * codes (like MD5, SHA1, SHA2 etc) of (collections of) objects.
	 * <p/>
	 * It is hoped that identical object (or objects which could result in identical results) have the save cacheId.
	 * <p/>
	 * This is similar to {@link Object#toString()} but with additional guarantees. This is similar to
	 * {@link Object#hashCode()} but using String type.
	 *
	 * @return id which can be used for caching purposes
	 * @since 1.8.0
	 */
	String getCacheId();
}
