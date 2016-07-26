/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CachingSupportServiceSecurityContextAdder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract caching interceptor which includes the security context in the cache container.
 *
 * @param <T> pipeline result type
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public class AbstractSecurityContextCachingInterceptor<T> extends AbstractCachingInterceptor<T> {

	@Autowired
	private CachingSupportServiceSecurityContextAdder securityContextAdder;

	/**
	 * Puts the cached security context in the thread local.
	 *
	 * @param context the cache context
	 */
	public void restoreSecurityContext(CacheContext context) {
		securityContextAdder.restoreSecurityContext(context);
	}

	/**
	 * Puts the thread local security in the cache (only when nothing is there just yet).
	 *
	 * @param context cache context
	 */
	public void addMoreContext(CacheContext context) {
		securityContextAdder.addMoreContext(context);
	}

}
