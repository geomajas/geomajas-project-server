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
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheKeyService;
import org.geomajas.plugin.caching.service.CacheManagerService;
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
 * @param <T> pipeline result type
 *
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public abstract class AbstractCachingInterceptor<T> extends AbstractPipelineInterceptor<T> {

	private final Logger log = LoggerFactory.getLogger(AbstractCachingInterceptor.class);

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private CacheKeyService cacheKeyService;

	/**
	 * Hook to allow additional data to be added in the cache context by subclasses.
	 *
	 * @param cacheContext cache context
	 */
	protected void addMoreContext(CacheContext cacheContext) {
		// nothing to do, can be overwritten by subclasses...
	}

	/**
	 * Get the requested object from the cache. The {@link CacheContainer} is built to determine the cache key.
	 *
	 * @param keys keys which need to be include in the cache context
	 * @param category cache category
	 * @param pipelineContext pipeline context
	 * @param containerClass container class
	 * @param <CONTAINER> container class
	 * @return cache container
	 */
	protected <CONTAINER extends CacheContainer> CONTAINER getContainer(String[] keys, CacheCategory category,
			PipelineContext pipelineContext, Class<CONTAINER> containerClass) {
		return getContainer(null, keys, category, pipelineContext, containerClass);
	}

	/**
	 * Get the requested object from the cache. The key is either obtained from the pipeline context (keyKey) if
	 * possible. Alternatively, the {@link CacheContainer} is built to determine the cache key.
	 *
	 * @param keyKey key to put the cache key in the pipeline context
	 * @param keys keys which need to be include in the cache context
	 * @param category cache category
	 * @param pipelineContext pipeline context
	 * @param containerClass container class
	 * @param <CONTAINER> container class
	 * @return cache container
	 */
	protected <CONTAINER extends CacheContainer> CONTAINER getContainer(String keyKey, String[] keys,
			CacheCategory category, PipelineContext pipelineContext, Class<CONTAINER> containerClass) {
		CONTAINER cc = null;
		try {
			VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			String cacheKey = null;
			if (keyKey != null) {
				cacheKey = pipelineContext.getOptional(keyKey, String.class);
			}
			if (cacheKey != null) {
				cc = cacheManager.get(layer, category, cacheKey, containerClass);
			}
			if (null == cc) {
				// context should have all keys !
				for (String key : keys) {
					if (pipelineContext.getOptional(key) == null) {
						return null;
					}
				}
				CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
				addMoreContext(cacheContext); // add more data...

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
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
		return cc;
	}

	/**
	 * Put {@link CacheContainer} in the cache. The cache key is stored in the pipeline context.
	 *
	 * @param pipelineContext pipeline context
	 * @param category cache category
	 * @param keys keys which need to be include in the cache context
	 * @param keyKey key to put the cache key in the pipeline context
	 * @param cacheContainer cache container
	 * @param envelope envelope
	 */
	protected void putContainer(PipelineContext pipelineContext, CacheCategory category, String[] keys, String keyKey,
			CacheContainer cacheContainer, Envelope envelope) {
		try {
			VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);
			CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, keys);
			addMoreContext(cacheContext); // add more data...

			cacheContainer.setContext(cacheContext);

			String cacheKey = cacheKeyService.getCacheKey(cacheContext);
			CacheContainer cc = cacheManager.get(layer, category, cacheKey, CacheContainer.class);
			while (null != cc) {
				if (!cacheContext.equals(cc.getContext())) {
					cacheKey = cacheKeyService.makeUnique(cacheKey);
					cc = cacheManager.get(layer, category, cacheKey, CacheContainer.class);
				} else {
					if (keyKey != null) {
						pipelineContext.put(keyKey, cacheKey);
					}
				}
			}
			cacheManager.put(layer, category, cacheKey, cacheContainer, envelope);
			pipelineContext.put(keyKey, cacheKey);
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
	

}
