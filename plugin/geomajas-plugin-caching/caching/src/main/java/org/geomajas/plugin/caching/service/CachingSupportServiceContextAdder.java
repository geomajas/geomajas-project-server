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

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

/**
 * Implementation of this interface can add additional context when building cache context for
 * {@link CachingSupportService}.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface CachingSupportServiceContextAdder {

	/**
	 * Hook to allow additional data to be added in the cache context by subclasses.
	 *
	 * @param cacheContext cache context
	 */
	void addMoreContext(CacheContext cacheContext);

}
