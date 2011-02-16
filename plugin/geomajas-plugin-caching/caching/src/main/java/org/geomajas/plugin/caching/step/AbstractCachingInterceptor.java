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
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheKeyService;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.pipeline.AbstractPipelineInterceptor;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Abstract base class for pipeline interceptors that implement a cache cycle. Caches the security context.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T> pipeline
 * @since 1.9.0
 */
@Api(allMethods = true)
public abstract class AbstractCachingInterceptor<T> extends AbstractPipelineInterceptor<T> {

	private final Logger log = LoggerFactory.getLogger(AbstractCachingInterceptor.class);

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private CacheKeyService cacheKeyService;

	@Autowired
	private SecurityContext securityContext;
	
	private boolean securityContextCached;


	/**
	 * Is the security context cached by this interceptor ?
	 * @return true if cached, false otherwise
	 */
	public boolean isSecurityContextCached() {
		return securityContextCached;
	}

	/**
	 * If set to true, the security context will be cached as well.
	 * @param securityContextCached true if the security context has to be cached
	 */
	public void setSecurityContextCached(boolean securityContextCached) {
		this.securityContextCached = securityContextCached;
	}

	protected <CONTAINER extends CacheContainer> CONTAINER getContainer(String[] keys, CacheCategory category,
			PipelineContext pipelineContext, Class<CONTAINER> containerClass) throws GeomajasException {
		return getContainer(null, keys, category, pipelineContext, containerClass);
	}

	protected <CONTAINER extends CacheContainer> CONTAINER getContainer(String keyKey, String[] keys,
			CacheCategory category, PipelineContext pipelineContext, Class<CONTAINER> containerClass)
			throws GeomajasException {
		VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		CONTAINER cc;
		String cacheKey = null;
		if (keyKey != null) {
			cacheKey = pipelineContext.getOptional(keyKey, String.class);
		}
		if (cacheKey != null) {
			cc = cacheManager.get(layer, category, cacheKey, containerClass);
			if (cc != null && isSecurityContextCached()) {
				// deserialize cached security context
				deserializeSecurityContext(cc.getContext());
			}
		} else {
			// context should have all keys !
			for (String key : keys) {
				if (pipelineContext.getOptional(key) == null) {
					return null;
				}
			}
			CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
			// add the security key
			if (isSecurityContextCached()) {
				serializeSecurityContext(cacheContext);
			}
			cacheKey = cacheKeyService.getCacheKey(cacheContext);
			cc = cacheManager.get(layer, category, cacheKey, containerClass);
			while (null != cc) {
				if (!cacheContext.equals(cc.getContext())) {
					cacheKey = cacheKeyService.makeUnique(cacheKey);
					cc = cacheManager.get(layer, category, cacheKey, containerClass);
				} else {
					if (keyKey != null) {
						pipelineContext.put(keyKey, cacheKey);
					}
					return cc;
				}
			}
		}
		return cc;
	}

	protected void putContainer(PipelineContext pipelineContext, CacheCategory category, String[] keys, String keyKey,
			CacheContainer cacheContainer, Envelope envelope) throws GeomajasException {
		try {
			// recorder.record(category, "Put item in cache");
			VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
			cacheContainer.setContext(cacheContext);
			// serialize security for cacheKey users
			if (isSecurityContextCached()) {
				serializeSecurityContext(cacheContext);
			}
			String cacheKey = cacheKeyService.getCacheKey(cacheContext);
			Object cc = cacheManager.get(layer, category, cacheKey);
			while (null != cc) {
				cacheKey = cacheKeyService.makeUnique(cacheKey);
				cc = cacheManager.get(layer, category, cacheKey);
			}
			cacheManager.put(layer, category, cacheKey, cacheContainer, envelope);
			pipelineContext.put(keyKey, cacheKey);
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
	
	/**
	 * Puts the cached security context in the thread local.
	 *
	 * @param context the cache context
	 */
	protected void deserializeSecurityContext(CacheContext context) {
		Object cached = context.get(CacheContext.SECURITY_CONTEXT_KEY);
		if (cached != null) {
			log.debug("Restoring security context ", cached);
			securityContext.restore((CacheableObject) cached);
		}
	}
	
	/**
	 * Puts the thread local security in the cache.
	 *
	 * @param context cache context
	 */
	protected void serializeSecurityContext(CacheContext context) {
		Object cached = context.get(CacheContext.SECURITY_CONTEXT_KEY);
		if (cached == null) {
			log.debug("Storing security context ", securityContext.getCacheableObject());
			context.put(CacheContext.SECURITY_CONTEXT_KEY, securityContext.getCacheableObject());
		}
	}

}
