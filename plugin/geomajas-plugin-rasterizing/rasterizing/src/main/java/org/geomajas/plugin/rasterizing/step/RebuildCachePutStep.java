/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.rasterizing.step;

import org.geomajas.command.dto.GetVectorTileRequest;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CachingSupportService;
import org.geomajas.plugin.caching.service.CachingSupportServiceSecurityContextAdder;
import org.geomajas.plugin.rasterizing.api.RasterizingPipelineCode;
import org.geomajas.service.TestRecorder;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Put the tile context in the rebuild cache to allow building the image later.
 * 
 * @author Joachim Van der Auwera
 */
public class RebuildCachePutStep implements PipelineStep<GetTileContainer> {

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
		recorder.record(CacheCategory.REBUILD, "Put item in cache");
		TileMetadata tileMetadata = context.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
		if (tileMetadata.isPaintGeometries()) {
			// create a geometry-only image and context key
			RebuildCacheContainer rcc = new RebuildCacheContainer();
			TileMetadata metadata = cloneMetadata(tileMetadata, false);
			rcc.setMetadata(metadata);
			context.put(PipelineCode.TILE_METADATA_KEY, metadata);
			cachingSupportService.putContainer(context, securityContextAdder, CacheCategory.REBUILD, KEYS,
					RasterizingPipelineCode.IMAGE_ID_KEY, RasterizingPipelineCode.IMAGE_ID_CONTEXT, rcc, container
							.getTile().getBounds());
		}
		if (tileMetadata.isPaintLabels()) {
			// create a labels-only image and context key
			RebuildCacheContainer rcc = new RebuildCacheContainer();
			TileMetadata metadata = cloneMetadata(tileMetadata, true);
			rcc.setMetadata(metadata);
			context.put(PipelineCode.TILE_METADATA_KEY, metadata);
			cachingSupportService.putContainer(context, securityContextAdder, CacheCategory.REBUILD, KEYS,
					RasterizingPipelineCode.IMAGE_ID_LABEL_KEY, RasterizingPipelineCode.IMAGE_ID_LABEL_CONTEXT, rcc,
					container.getTile().getBounds());
		}
	}

	private TileMetadata cloneMetadata(TileMetadata tileMetadata, boolean paintLabels) {
		TileMetadata metadata = new GetVectorTileRequest(tileMetadata);
		metadata.setPaintLabels(paintLabels);
		metadata.setPaintGeometries(!paintLabels);
		return metadata;
	}
}
