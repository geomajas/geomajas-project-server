/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import org.geomajas.global.Api;
import org.geomajas.global.CacheableObject;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final Logger log = LoggerFactory.getLogger(AbstractSecurityContextCachingInterceptor.class);

	@Autowired
	private SecurityContext securityContext;

	/**
	 * Puts the cached security context in the thread local.
	 *
	 * @param context the cache context
	 */
	public void restoreSecurityContext(CacheContext context) {
		Object cached = context.get(CacheContext.SECURITY_CONTEXT_KEY);
		if (cached != null) {
			log.debug("Restoring security context ", cached);
			securityContext.restore((CacheableObject) cached);
		}
	}

	/**
	 * Puts the thread local security in the cache (only when nothing is there just yet).
	 *
	 * @param context cache context
	 */
	protected void addMoreContext(CacheContext context) {
		Object cached = context.get(CacheContext.SECURITY_CONTEXT_KEY);
		if (cached == null) {
			Object sc = securityContext.getCacheableObject();
			log.debug("Storing security context ", sc);
			context.put(CacheContext.SECURITY_CONTEXT_KEY, sc);
		}
	}

}
