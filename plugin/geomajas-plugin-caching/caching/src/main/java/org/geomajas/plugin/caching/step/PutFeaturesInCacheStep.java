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
import org.geomajas.layer.pipeline.GetFeaturesContainer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.service.pipeline.PipelineContext;

/**
 * Put features in the cache for later retrieval.
 *
 * @author Joachim Van der Auwera
 */
public class PutFeaturesInCacheStep extends AbstractPutInCacheStep<GetFeaturesContainer> {

	public void execute(PipelineContext pipelineContext, GetFeaturesContainer result) throws GeomajasException {
		execute(pipelineContext, CacheCategory.FEATURE, CacheStepConstant.CACHE_FEATURES_KEY,
				CacheStepConstant.CACHE_FEATURES_CONTEXT, CacheStepConstant.CACHE_FEATURES_USED,
				new FeaturesCacheContainer(result.getFeatures(), result.getBounds()), result.getBounds());
	}
}
