/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
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
import org.geomajas.plugin.caching.service.CachingSupportService;
import org.geomajas.plugin.caching.service.CachingSupportServiceSecurityContextAdder;
import org.geomajas.plugin.rasterizing.api.RasterizingContainer;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Put the rasterized image in the cache.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 */
public class RasterCachePutStep implements PipelineStep<GetTileContainer> {

	private static final String[] KEYS = { PipelineCode.LAYER_ID_KEY, PipelineCode.TILE_METADATA_KEY };

	private String id;

	@Autowired
	private CachingSupportService cachingSupportService;

	@Autowired
	private CachingSupportServiceSecurityContextAdder securityContextAdder;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Autowired
	private TestRecorder recorder;

	public void execute(PipelineContext context, GetTileContainer container) throws GeomajasException {
		RasterizingContainer rc = context.getOptional(RasterizingPipelineCode.CONTAINER_KEY,
				RasterizingContainer.class);
		if (rc != null) {
			recorder.record(CacheCategory.RASTER, "Put item in cache");
			cachingSupportService.putContainer(context, securityContextAdder, CacheCategory.RASTER, KEYS,
					RasterizingPipelineCode.IMAGE_ID_KEY, RasterizingPipelineCode.IMAGE_ID_CONTEXT, rc,
					container.getTile().getBounds());
		}
	}

}
