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
import org.geomajas.layer.pipeline.GetFeaturesContainer;
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
 * Try to get the features from the cache (instead of accessing the data source).
 *
 * @author Joachim Van der Auwera
 */
public class GetFeaturesFromCacheStep implements PipelineStep<GetFeaturesContainer> {

	private final Logger log = LoggerFactory.getLogger(GetFeaturesFromCacheStep.class);
	
	private static final String[] KEYS =
			{PipelineCode.LAYER_ID_KEY, PipelineCode.CRS_KEY, PipelineCode.FILTER_KEY, PipelineCode.OFFSET_KEY,
					PipelineCode.MAX_RESULT_SIZE_KEY, PipelineCode.FEATURE_INCLUDES_KEY};

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

	public void execute(PipelineContext pipelineContext, GetFeaturesContainer result) throws GeomajasException {
		try {
			VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);

			CacheContext cacheContext = cacheKeyService.getCacheContext(pipelineContext, KEYS);
			cacheContext.put("securityContext", securityContext.getId());
			String cacheKey = cacheKeyService.getCacheKey(cacheContext);

			FeaturesCacheContainer cc =
					cacheManager.get(layer, CacheCategory.FEATURE, cacheKey, FeaturesCacheContainer.class);

			while (null != cc) {
				if (cacheContext.equals(cc.getContext())) {
					// found item in cache
					result.setFeatures(cc.getFeatures());
					result.setBounds(cc.getBounds());
					pipelineContext.put(CacheStepConstant.CACHE_FEATURES_USED, true);
					recorder.record(CacheCategory.FEATURE, "Got item from cache");
					break;
				} else {
					cacheKey = cacheKeyService.makeUnique(cacheKey);
					cc = cacheManager.get(layer, CacheCategory.FEATURE, cacheKey, FeaturesCacheContainer.class);
				}
			}
			pipelineContext.put(CacheStepConstant.CACHE_FEATURES_KEY, cacheKey);
			pipelineContext.put(CacheStepConstant.CACHE_FEATURES_CONTEXT, cacheContext);
		} catch (Throwable t) {
			// have to prevent caching code from making the pipeline fail, log and discard errors
			log.error("Error during caching step, only logged: " + t.getMessage(), t);
		}
	}
}
