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

package org.geomajas.plugin.caching.service;

import org.geomajas.security.SavedAuthorization;
import org.geomajas.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@link CachingSupportServiceSecurityContextAdder} implementation.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class CachingSupportServiceSecurityContextAdderImpl implements CachingSupportServiceSecurityContextAdder {

	private final Logger log = LoggerFactory.getLogger(CachingSupportServiceSecurityContextAdderImpl.class);

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	/**
	 * Puts the cached security context in the thread local.
	 *
	 * @param context the cache context
	 */
	public void restoreSecurityContext(CacheContext context) {
		SavedAuthorization cached = context.get(CacheContext.SECURITY_CONTEXT_KEY, SavedAuthorization.class);
		if (cached != null) {
			log.debug("Restoring security context {}", cached);
			securityManager.restoreSecurityContext(cached);
		} else {
			securityManager.clearSecurityContext();
		}
	}

	/**
	 * Puts the thread local security in the cache (only when nothing is there just yet).
	 *
	 * @param context cache context
	 */
	public void addMoreContext(CacheContext context) {
		Object cached = context.get(CacheContext.SECURITY_CONTEXT_KEY);
		if (cached == null) {
			SavedAuthorization sa = securityContext.getSavedAuthorization();
			log.debug("Storing SavedAuthorization {}", sa);
			context.put(CacheContext.SECURITY_CONTEXT_KEY, sa);
		}
	}

}
