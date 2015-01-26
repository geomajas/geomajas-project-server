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
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Interceptor to test storage and restore of the security context.
 *
 * @author Joachim Van der Auwera
 */
public class SecurityTestInterceptor extends AbstractSecurityContextCachingInterceptor<StringContainer> {

	public static final String CACHE_KEY = "key";
	public static final String CACHE_CONTEXT = "ctx";

	private static final String[] KEYS = {PipelineCode.LAYER_ID_KEY};

	@Autowired
	private TestRecorder recorder;

	public ExecutionMode beforeSteps(PipelineContext context, StringContainer response) throws GeomajasException {
		StringCacheContainer cc = getContainer(CACHE_KEY, CACHE_CONTEXT, KEYS, CacheCategory.BOUNDS, context,
				StringCacheContainer.class);
		if (cc != null) {
			recorder.record(CacheCategory.BOUNDS, "Got item from cache");
			restoreSecurityContext(cc.getContext());
			response.setString(cc.getString());
			return ExecutionMode.EXECUTE_NONE;
		}
		return ExecutionMode.EXECUTE_ALL;
	}

	public void afterSteps(PipelineContext context, StringContainer response) throws GeomajasException {
		response.setString(context.getOptional(PipelineCode.FILTER_KEY, String.class));
		recorder.record(CacheCategory.BOUNDS, "Put item in cache");
		StringCacheContainer scc = new StringCacheContainer();
		scc.setString(response.getString());
		putContainer(context, CacheCategory.BOUNDS, KEYS, CACHE_KEY, CACHE_CONTEXT, scc, null);
	}

}
