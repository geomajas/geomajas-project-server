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
import org.geomajas.layer.pipeline.GetTileContainer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileMetadata;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Put tile string contents in cache for later retrieval.
 *
 * @author Joachim Van der Auwera
 */
public class PutTileStringContentInCacheStep extends AbstractPutInCacheStep<GetTileContainer> {

	public void execute(PipelineContext pipelineContext, GetTileContainer result) throws GeomajasException {
		TileMetadata metadata = pipelineContext.get(PipelineCode.TILE_METADATA_KEY, TileMetadata.class);
		CacheCategory cacheCategory = CacheCategory.SVG;
		if (TileMetadata.PARAM_VML_RENDERER.equalsIgnoreCase(metadata.getRenderer())) {
			cacheCategory = CacheCategory.VML;
		}

		InternalTile tile = result.getTile();
		execute(pipelineContext, cacheCategory, CacheStepConstant.CACHE_TILE_CONTENT_KEY, 
				CacheStepConstant.CACHE_TILE_CONTENT_CONTEXT,
				CacheStepConstant.CACHE_TILE_CONTENT_USED,
				new TileContentCacheContainer(tile.getFeatureContent(), tile.getLabelContent()), tile.getBounds());
	}
}
