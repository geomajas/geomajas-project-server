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

package org.geomajas.plugin.caching.step;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheKeyService;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Try to get the entire tile from the cache.
 *
 * @author Joachim Van der Auwera
 */
public class GetTileFromCacheStep implements PipelineStep<GetTileContainer> {

	private Logger log = LoggerFactory.getLogger(GetTileFromCacheStep.class);

	private static final String[] KEYS =
			{PipelineCode.LAYER_ID_KEY, PipelineCode.FILTER_KEY, PipelineCode.TILE_METADATA_KEY,
					PipelineCode.FEATURE_INCLUDES_KEY};

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private CacheKeyService cacheKeyService;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private TestRecorder recorder;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext pipelineContext, GetTileContainer result) throws GeomajasException {
		try {
			VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);

			CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, KEYS);
			cacheContext.put("securityContext", securityContext.getId());
			String cacheKey = cacheKeyService.getCacheKey(layer, CacheCategory.TILE, cacheContext);

			TileCacheContainer cc = cacheManager.get(layer, CacheCategory.TILE, cacheKey, TileCacheContainer.class);

			while (null != cc) {
				if (cacheContext.equals(cc.getContext())) {
					// found item in cache
					result.setTile(cc.getTile());
					pipelineContext.put(CacheStepConstant.CACHE_TILE_USED, true);
					recorder.record(CacheCategory.TILE, "Got item from cache");
					pipelineContext.setFinished(true); // request nothing, stop now to avoid more work being done
					break;
				} else {
					cacheKey = cacheKeyService.makeUnique(cacheKey);
					log.debug("Cache context did not match, new key {}", cacheKey);
					cc = cacheManager.get(layer, CacheCategory.TILE, cacheKey, TileCacheContainer.class);
				}
			}
			pipelineContext.put(CacheStepConstant.CACHE_TILE_KEY, cacheKey);
			pipelineContext.put(CacheStepConstant.CACHE_TILE_CONTEXT, cacheContext);
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
}
