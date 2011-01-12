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
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Put entire tile in cache for later retrieval.
 *
 * @author Joachim Van der Auwera
 */
public class PutTileInCacheStep extends AbstractPutInCacheStep<GetTileContainer> {

	public void execute(PipelineContext pipelineContext, GetTileContainer result) throws GeomajasException {
		InternalTile tile = result.getTile();
		execute(pipelineContext, CacheCategory.TILE, CacheStepConstant.CACHE_TILE_KEY,
				CacheStepConstant.CACHE_TILE_CONTEXT, CacheStepConstant.CACHE_TILE_USED,
				new TileCacheContainer(tile), tile.getBounds());
	}
}
