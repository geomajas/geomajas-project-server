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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Try to get the tile string content from the cache (instead of calculating).
 *
 * @author Joachim Van der Auwera
 */
public class GetTileStringContentFromCacheStep implements PipelineStep<GetTileContainer> {

	private final Logger log = LoggerFactory.getLogger(GetTileStringContentFromCacheStep.class);

	private static final String[] KEYS =
			{PipelineCode.LAYER_ID_KEY, PipelineCode.FILTER_KEY, PipelineCode.TILE_METADATA_KEY};

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
		try {
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
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
}
