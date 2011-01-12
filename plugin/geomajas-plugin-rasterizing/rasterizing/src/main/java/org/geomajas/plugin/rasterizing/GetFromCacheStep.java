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

package org.geomajas.plugin.rasterizing;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Try to get the rasterized image from the raster cache.
 *
 * @author Joachim Van der Auwera
 */
public class GetFromCacheStep extends AbstractRasterizingStep {

	@Autowired
	private CacheManagerService cacheManager;

	public void execute(PipelineContext context, RasterizingContainer rasterizingContainer) throws
			GeomajasException {

		String id = context.get(RasterizingPipelineCode.IMAGE_ID_KEY, String.class);
		VectorLayer layer = context.get(PipelineCode.LAYER_KEY, VectorLayer.class);

		RasterizingContainer rc = cacheManager.get(layer, CacheCategory.RASTER, id, RasterizingContainer.class);
		if (null != rc) {
			rasterizingContainer.setImage(rc.getImage());
			context.setFinished(true);
		}
	}
}
