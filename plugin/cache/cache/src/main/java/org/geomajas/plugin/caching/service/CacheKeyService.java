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

import org.geomajas.service.pipeline.PipelineContext;

/**
 * Service for determining a cache key based on cache context, which is typically based on pipeline context.
 *
 * @author Joachim Van der Auwera
 */
public interface CacheKeyService {

	/**
	 * Get a key for an object to cache.
	 *
	 * @param context context for object to cache
	 * @return key
	 */
	String getCacheKey(CacheContext context);

	/**
	 * Build a cache context object from the requested keys of the pipeline context.
	 * Note that the keys are optional, though a warning is logged for null values.
	 *
	 * @param pipelineContext pipeline context to key base info from
	 * @param keys keys for items to include from pipeline context
	 * @return cache context
	 */
	CacheContext getCacheContext(PipelineContext pipelineContext, String[] keys);

	/**
	 * Change a key to try to make it unique.
	 *
	 * @param duplicateKey key which was not unique
	 * @return new key, hoping it is now unique
	 */
	String makeUnique(String duplicateKey);
}
