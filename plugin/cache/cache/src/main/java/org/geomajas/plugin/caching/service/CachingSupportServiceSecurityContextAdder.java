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

import org.geomajas.annotation.Api;

/**
 * {@link CachingSupportServiceContextAdder} added for handling the security context.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CachingSupportServiceSecurityContextAdder extends CachingSupportServiceContextAdder {

	/**
	 * Puts the cached security context in the thread local.
	 *
	 * @param context the cache context
	 */
	void restoreSecurityContext(CacheContext context);

}
