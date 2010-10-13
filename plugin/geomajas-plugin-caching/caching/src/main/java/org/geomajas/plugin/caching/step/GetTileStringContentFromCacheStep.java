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
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheKeyService;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Try to get the tile string content from the cache (instead of calculating).
 *
 * @author Joachim Van der Auwera
 */
public class GetTileStringContentFromCacheStep implements PipelineStep<GetTileContainer> {

	private static final String[] KEYS = {PipelineCode.LAYER_ID_KEY, PipelineCode.CRS_KEY, PipelineCode.FILTER_KEY};

	@Autowired
	private CacheManagerService cacheManager;

	@Autowired
	private CacheKeyService cacheKeyService;

	@Autowired
	private TestRecorder recorder;

	@Autowired
	private SecurityContext securityContext;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext pipelineContext, GetTileContainer result) throws GeomajasException {
		VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		TileMetadata metadata = pipelineContext.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);

		CacheCategory cacheCategory = CacheCategory.SVG;
		if (TileMetadata.PARAM_VML_RENDERER.equalsIgnoreCase(metadata.getRenderer())) {
			cacheCategory = CacheCategory.VML;
		}

		CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, KEYS);
		cacheContext.put("securityContext", securityContext.getId());
		String cacheKey = cacheKeyService.getCacheKey(layer, cacheCategory, cacheContext);

		TileContentCacheContainer cc =
				cacheManager.get(layer, cacheCategory, cacheKey, TileContentCacheContainer.class);

		while (null != cc) {
			if (cacheContext.equals(cc.getContext())) {
				// found item in cache
				result.getTile().setFeatureContent(cc.getFeatureContent());
				result.getTile().setLabelContent(cc.getLabelContent());
				pipelineContext.put(CacheStepConstant.CACHE_TILE_CONTENT_USED, true);
				recorder.record(cacheCategory, "Got item from cache");
				break;
			} else {
				cacheKey = cacheKeyService.makeUnique(cacheKey);
				cc = cacheManager.get(layer, cacheCategory, cacheKey, TileContentCacheContainer.class);
			}
		}
		pipelineContext.put(CacheStepConstant.CACHE_TILE_CONTENT_KEY, cacheKey);
		pipelineContext.put(CacheStepConstant.CACHE_TILE_CONTENT_CONTEXT, cacheContext);
	}
}
