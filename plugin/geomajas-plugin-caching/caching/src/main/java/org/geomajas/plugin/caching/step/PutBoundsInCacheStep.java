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
import org.geomajas.layer.pipeline.GetBoundsContainer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Put bounds in cache for later retrieval.
 *
 * @author Joachim Van der Auwera
 */
public class PutBoundsInCacheStep extends AbstractPutInCacheStep<GetBoundsContainer> {

	public void execute(PipelineContext pipelineContext, GetBoundsContainer result)
			throws GeomajasException {
		execute(pipelineContext, CacheCategory.BOUNDS, CacheStepConstant.CACHE_BOUNDS_KEY,
				CacheStepConstant.CACHE_BOUNDS_CONTEXT, CacheStepConstant.CACHE_BOUNDS_USED,
				new BoundsCacheContainer(result.getEnvelope()), null);
	}
}
