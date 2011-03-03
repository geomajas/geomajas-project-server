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
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.step.GetTileCachingInterceptor;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Rasterizing version of {@link GetTileCachingInterceptor} which also stores the raster in the cache.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
public class GetRasterizedTileAllCachingInterceptor extends GetTileCachingInterceptor {

	@Autowired
	private TestRecorder recorder;

	@Override
	public void afterSteps(PipelineContext context, GetTileContainer response) throws GeomajasException {
		super.afterSteps(context, response);

		// optionally put the image in the cache for later retrieval
		RasterizingContainer rc = context
				.getOptional(RasterizingPipelineCode.CONTAINER_KEY, RasterizingContainer.class);
		if (rc != null) {
			recorder.record(CacheCategory.RASTER, "Put item in cache");
			putContainer(context, CacheCategory.RASTER, KEYS, RasterizingPipelineCode.IMAGE_ID_KEY, rc, response
					.getTile().getBounds());
		}
	}
}
