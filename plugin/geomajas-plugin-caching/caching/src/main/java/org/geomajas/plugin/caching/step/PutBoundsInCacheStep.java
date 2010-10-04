/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.caching.step;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.pipeline.GetBoundsContainer;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheContext;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.geomajas.service.pipeline.PipelineCode;
import org.geomajas.service.pipeline.PipelineContext;
import org.geomajas.service.pipeline.PipelineStep;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Put bounds in cache for later retrieval.
 *
 * @author Joachim Van der Auwera
 */
public class PutBoundsInCacheStep implements PipelineStep<GetBoundsContainer> {

	@Autowired
	private CacheManagerService cacheManager;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(PipelineContext pipelineContext, GetBoundsContainer getBoundsContainer)
			throws GeomajasException {
		VectorLayer layer = pipelineContext.get(PipelineCode.LAYER_KEY, VectorLayer.class);

		String cacheKey = pipelineContext.get(CacheStepConstant.CACHE_BOUNDS_KEY, String.class);
		CacheContext cacheContext = pipelineContext.get(CacheStepConstant.CACHE_BOUNDS_CONTEXT, CacheContext.class);

		BoundsCacheContainer cc = new BoundsCacheContainer();
		cc.setContext(cacheContext);
		cc.setBounds(getBoundsContainer.getEnvelope());
		cacheManager.put(layer, CacheCategory.BOUNDS, cacheKey, cc, null);
	}
}
