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

import com.vividsolutions.jts.geom.Envelope;
import org.geomajas.annotation.Api;
import org.geomajas.plugin.caching.step.CacheContainer;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Support service for pipeline classes which use caching.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface CachingSupportService {

	/**
	 * Get the requested object from the cache. The key is either obtained from the pipeline context (keyKey) if
	 * possible. Alternatively, the {@link CacheContainer} is built to determine the cache key.
	 *
	 * @param keyKey key to put the cache key in the pipeline context
	 * @param contextKey key to put the cache context in the pipeline context
	 * @param keys keys which need to be include in the cache context
	 * @param category cache category
	 * @param pipelineContext pipeline context
	 * @param contextAdder class to add additional context
	 * @param containerClass container class
	 * @param <CONTAINER> container class
	 * @return cache container
	 */
	<CONTAINER extends CacheContainer> CONTAINER getContainer(String keyKey, String contextKey,
			String[] keys, CacheCategory category, PipelineContext pipelineContext,
			CachingSupportServiceContextAdder contextAdder, Class<CONTAINER> containerClass);

	/**
	 * Put {@link CacheContainer} in the cache. The cache key is stored in the pipeline context.
	 *
	 * @param pipelineContext pipeline context
	 * @param contextAdder class to add additional context
	 * @param category cache category
	 * @param keys keys which need to be include in the cache context
	 * @param contextKey key to put the cache context in the pipeline context
	 * @param keyKey key to put the cache key in the pipeline context
	 * @param cacheContainer cache container
	 * @param envelope envelope
	 */
	void putContainer(PipelineContext pipelineContext, CachingSupportServiceContextAdder contextAdder,
			CacheCategory category, String[] keys, String keyKey, String contextKey, CacheContainer cacheContainer,
			Envelope envelope);

}
