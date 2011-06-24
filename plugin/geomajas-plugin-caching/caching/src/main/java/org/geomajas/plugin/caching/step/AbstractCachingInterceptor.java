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
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CachingSupportService;
import org.geomajas.plugin.caching.service.CachingSupportServiceContextAdder;
import org.geomajas.service.pipeline.AbstractPipelineInterceptor;
import org.geomajas.service.pipeline.PipelineContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Abstract base class for pipeline interceptors that implement a cache cycle. Caches the security context.
 * 
 * @param <T> pipeline result type
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public abstract class AbstractCachingInterceptor<T> extends AbstractPipelineInterceptor<T> implements
		CachingSupportServiceContextAdder {

	@Autowired
	private CachingSupportService cachingSupportService;

	/**
	 * Get the requested object from the cache. The key is either obtained from the pipeline context (keyKey) if
	 * possible. Alternatively, the {@link CacheContainer} is built to determine the cache key.
	 *
	 * @param keyKey key to put the cache key in the pipeline context
	 * @param contextKey key to put the cache context in the pipeline context
	 * @param keys keys which need to be include in the cache context
	 * @param category cache category
	 * @param pipelineContext pipeline context
	 * @param containerClass container class
	 * @param <CONTAINER> container class
	 * @return cache container
	 */
	protected <CONTAINER extends CacheContainer> CONTAINER getContainer(String keyKey, String contextKey,
			String[] keys, CacheCategory category, PipelineContext pipelineContext, Class<CONTAINER> containerClass) {
<<<<<<< Updated upstream
		return cachingSupportService.getContainer(keyKey, contextKey, keys, category, pipelineContext, this,
				containerClass);
=======
		CONTAINER cc = null;
log.debug("1");
		try {
			VectorLayer layer = pipelineContext.getOptional(PipelineCode.LAYER_KEY, VectorLayer.class);
			String cacheKey = null;
			if (keyKey != null) {
				cacheKey = pipelineContext.getOptional(keyKey, String.class);
			}
			if (cacheKey != null) {
				cc = cacheManager.get(layer, category, cacheKey, containerClass);
			}
log.debug("2");
			if (null == cc) {
				CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
				addMoreContext(cacheContext); // add more data...
log.debug("3");

				cacheKey = cacheKeyService.getCacheKey(cacheContext);
				cc = cacheManager.get(layer, category, cacheKey, containerClass);
log.debug("4 {}", cc);
				while (null != cc) {
					if (!cacheContext.equals(cc.getContext())) {
						cacheKey = cacheKeyService.makeUnique(cacheKey);
						cc = cacheManager.get(layer, category, cacheKey, containerClass);
					} else {
						pipelineContext.put(keyKey, cacheKey);
						pipelineContext.put(contextKey, cacheContext);
						return cc;
					}
				}
			}
		} catch (Throwable t) { //NOPMD
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
log.debug("5");
		return cc;
>>>>>>> Stashed changes
	}

	/**
	 * Put {@link CacheContainer} in the cache. The cache key is stored in the pipeline context.
	 *
	 * @param pipelineContext pipeline context
	 * @param category cache category
	 * @param keys keys which need to be include in the cache context
	 * @param contextKey key to put the cache context in the pipeline context
	 * @param keyKey key to put the cache key in the pipeline context
	 * @param cacheContainer cache container
	 * @param envelope envelope
	 */
	protected void putContainer(PipelineContext pipelineContext, CacheCategory category, String[] keys, String keyKey,
			String contextKey, CacheContainer cacheContainer, Envelope envelope) {
<<<<<<< Updated upstream
		cachingSupportService.putContainer(pipelineContext, this, category, keys, keyKey, contextKey, cacheContainer,
				envelope);
=======
		try {
log.debug("10");
			VectorLayer layer = pipelineContext.getOptional(PipelineCode.LAYER_KEY, VectorLayer.class);
			CacheContext cacheContext = pipelineContext.getOptional(contextKey, CacheContext.class);
			if (null == cacheContext) {
				cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
				addMoreContext(cacheContext); // add more data...
			}
log.debug("11");

			cacheContainer.setContext(cacheContext);
log.debug("12");

			String cacheKey = pipelineContext.getOptional(keyKey, String.class);
			if (null == cacheKey) {
				cacheKey = cacheKeyService.getCacheKey(cacheContext);
			}
log.debug("13");
			CacheContainer cc = cacheManager.get(layer, category, cacheKey, CacheContainer.class);
			while (null != cc && !cacheContext.equals(cc.getContext())) {
log.debug("14");
				cacheKey = cacheKeyService.makeUnique(cacheKey);
				cc = cacheManager.get(layer, category, cacheKey, CacheContainer.class);
			}
			if (keyKey != null) {
				pipelineContext.put(keyKey, cacheKey);
			}
			cacheManager.put(layer, category, cacheKey, cacheContainer, envelope);
			pipelineContext.put(keyKey, cacheKey);
		} catch (Throwable t) { //NOPMD
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
log.debug("15");
>>>>>>> Stashed changes
	}

}
