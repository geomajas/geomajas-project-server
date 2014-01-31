/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import org.geomajas.annotation.Api;
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
		return cachingSupportService.getContainer(keyKey, contextKey, keys, category, pipelineContext, this,
				containerClass);
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
		cachingSupportService.putContainer(pipelineContext, this, category, keys, keyKey, contextKey, cacheContainer,
				envelope);
	}

}
