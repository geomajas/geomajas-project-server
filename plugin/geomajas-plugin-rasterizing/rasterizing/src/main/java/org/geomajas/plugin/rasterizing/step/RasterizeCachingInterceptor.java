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

package org.geomajas.plugin.rasterizing.step;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.step.AbstractSecurityContextCachingInterceptor;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Get rasterized image from cache.
 * 
 * @author Jan De Moerloose
 */
public class RasterizeCachingInterceptor  extends AbstractSecurityContextCachingInterceptor<RasterizingContainer> {

	private static final String[] KEYS = { PipelineCode.LAYER_ID_KEY, PipelineCode.TILE_METADATA_KEY };

	@Autowired
	private TestRecorder recorder;

	public ExecutionMode beforeSteps(PipelineContext context, RasterizingContainer response) throws GeomajasException {
		RasterizingContainer rc = getContainer(RasterizingPipelineCode.IMAGE_ID_KEY, KEYS, CacheCategory.RASTER,
				context, RasterizingContainer.class);
		if (null != rc) {
			recorder.record(CacheCategory.RASTER, "Got item from cache");
			response.setImage(rc.getImage());
			return ExecutionMode.EXECUTE_NONE;
		}
		return ExecutionMode.EXECUTE_ALL;

	}

	public void afterSteps(PipelineContext context, RasterizingContainer response) throws GeomajasException {
		// nothing to do, image should already be in the cache at this point
	}

}
