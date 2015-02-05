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
package org.geomajas.plugin.caching.step;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerLazyFeatureConversionSupport;
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Interceptor for caching the features.
 *
 * @author Jan De Moerloose
 */
public class GetFeaturesEachCachingInterceptor extends AbstractSecurityContextCachingInterceptor<GetFeaturesContainer> {

	@Autowired
	private TestRecorder recorder;

	private static final String[] KEYS = {PipelineCode.LAYER_ID_KEY, PipelineCode.CRS_KEY, PipelineCode.FILTER_KEY,
			PipelineCode.STYLE_KEY, PipelineCode.OFFSET_KEY, PipelineCode.MAX_RESULT_SIZE_KEY,
			PipelineCode.FEATURE_INCLUDES_KEY};

	@Override
	public ExecutionMode beforeSteps(PipelineContext context, GetFeaturesContainer response) throws GeomajasException {
		// do not cache features which are converted lazily, this would put detached objects in cache
		if (isCacheable(context)) {
			FeaturesCacheContainer cc =
					getContainer(CacheStepConstant.CACHE_FEATURES_KEY, CacheStepConstant.CACHE_FEATURES_CONTEXT, KEYS,
							CacheCategory.FEATURE, context, FeaturesCacheContainer.class);
			if (cc != null) {
				recorder.record(CacheCategory.FEATURE, "Got item from cache");
				response.setBounds(cc.getBounds());
				response.setFeatures(cc.getFeatures());
				return ExecutionMode.EXECUTE_NONE;
			}
		}
		return ExecutionMode.EXECUTE_ALL;
	}

	@Override
	public void afterSteps(PipelineContext context, GetFeaturesContainer response) throws GeomajasException {
		// do not cache features which are converted lazily, this would put detached objects in cache
		if (isCacheable(context)) {
			recorder.record(CacheCategory.FEATURE, "Put item in cache");
			putContainer(context, CacheCategory.FEATURE, KEYS, CacheStepConstant.CACHE_FEATURES_KEY,
					CacheStepConstant.CACHE_FEATURES_CONTEXT,
					new FeaturesCacheContainer(response.getFeatures(), response.getBounds()), response.getBounds());
		}
	}

	/**
	 * Features are only cacheable when not converted lazily as lazy features are incomplete, it would put detached
	 * objects in the cache.
	 *
	 * @return true when features are not converted lazily
	 */
	private boolean isCacheable(PipelineContext context) throws GeomajasException {
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);
		return !(layer instanceof VectorLayerLazyFeatureConversionSupport &&
				((VectorLayerLazyFeatureConversionSupport) layer).useLazyFeatureConversion());
	}

}
