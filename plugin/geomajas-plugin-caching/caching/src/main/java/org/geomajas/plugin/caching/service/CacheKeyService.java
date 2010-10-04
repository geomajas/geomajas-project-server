/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.caching.service;

import org.geomajas.layer.Layer;
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
	 * @param layer layer for object to cache
	 * @param category cache category
	 * @param context context for object to cache
	 * @return key
	 */
	String getCacheKey(Layer layer, CacheCategory category, CacheContext context);

	/**
	 * Build a cache context object from the requested keys of the pipeline context.
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
