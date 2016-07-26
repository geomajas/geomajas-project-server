/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.caching.step;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.pipeline.GetBoundsContainer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Interceptor for caching the bounds.
 *
 * @author Jan De Moerloose
 */
public class GetBoundsCachingInterceptor extends AbstractSecurityContextCachingInterceptor<GetBoundsContainer> {

	@Autowired
	private TestRecorder recorder;

	private static final String[] KEYS = {PipelineCode.LAYER_ID_KEY, PipelineCode.CRS_KEY, PipelineCode.FILTER_KEY};

	@Override
	public ExecutionMode beforeSteps(PipelineContext context, GetBoundsContainer response) throws GeomajasException {
		BoundsCacheContainer cc = getContainer(CacheStepConstant.CACHE_BOUNDS_KEY,
				CacheStepConstant.CACHE_BOUNDS_CONTEXT, KEYS, CacheCategory.BOUNDS, context,
				BoundsCacheContainer.class);
		if (cc != null) {
			recorder.record(CacheCategory.BOUNDS, "Got item from cache");
			response.setEnvelope(cc.getBounds());
			return ExecutionMode.EXECUTE_NONE;
		}
		return ExecutionMode.EXECUTE_ALL;
	}

	@Override
	public void afterSteps(PipelineContext context, GetBoundsContainer response) throws GeomajasException {
		recorder.record(CacheCategory.BOUNDS, "Put item in cache");
		putContainer(context, CacheCategory.BOUNDS, KEYS, CacheStepConstant.CACHE_BOUNDS_KEY,
				CacheStepConstant.CACHE_BOUNDS_CONTEXT,
				new BoundsCacheContainer(response.getEnvelope()), response.getEnvelope());
	}

}
