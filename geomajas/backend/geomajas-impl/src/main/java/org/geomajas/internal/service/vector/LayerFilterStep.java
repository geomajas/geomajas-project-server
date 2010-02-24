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

package org.geomajas.internal.service.vector;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.internal.service.VectorLayerServiceImpl;
import org.geomajas.layer.VectorLayer;
import org.geomajas.rendering.pipeline.PipelineContext;
import org.geomajas.rendering.pipeline.PipelineStep;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.FilterService;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Extend the existing layer filter (in the context, if any) to include layer security.
 * <p/>
 * This combines the visible area, the security filter for the layer, the default filter for the layer,
 *
 * @author Joachim Van der Auwera
 */
public class LayerFilterStep implements PipelineStep<Object, Object> {

	private final Logger log = LoggerFactory.getLogger(LayerFilterStep.class);

	private String id;

	@Autowired
	private FilterService filterService;

	@Autowired
	private SecurityContext securityContext;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void execute(Object request, PipelineContext context, Object response) throws GeomajasException {
		VectorLayer layer = context.get(VectorLayerServiceImpl.LAYER_KEY, VectorLayer.class);
		Filter filter = context.getOptional(VectorLayerServiceImpl.FILTER_KEY, Filter.class);
		VectorLayerInfo layerInfo = layer.getLayerInfo();
		String layerId = layerInfo.getId();

		// apply generic security filter
		Filter layerFeatureFilter = securityContext.getFeatureFilter(layerId);
		if (null != layerFeatureFilter) {
			filter = and(filter, layerFeatureFilter);
		}

		// apply default filter
		String defaultFilter = layerInfo.getFilter();
		if (null != defaultFilter) {
			try {
				filter = and(filter, CQL.toFilter(defaultFilter));
			} catch (CQLException ce) {
				throw new GeomajasException(ce, ExceptionCode.FILTER_APPLY_PROBLEM, defaultFilter);
			}
		}

		// apply visible area filter
		Geometry visibleArea = securityContext.getVisibleArea(layerId);
		if (null != visibleArea) {
			String geometryName = layerInfo.getFeatureInfo().getGeometryType().getName();
			if (securityContext.isPartlyVisibleSufficient(layerId)) {
				filter = and(filter, filterService.createIntersectsFilter(visibleArea, geometryName));
			} else {
				filter = and(filter, filterService.createWithinFilter(visibleArea, geometryName));
			}
		} else {
			log.warn("Visible area is null for layer " + layerId + "removing all content!");
			filter = filterService.createFalseFilter();
		}

		context.put(VectorLayerServiceImpl.FILTER_KEY, filter);
	}

	private Filter and(Filter f1, Filter f2) {
		if (null == f1) {
			return f2;
		} else if (null == f2) {
			return f1;
		} else {
			return filterService.createAndFilter(f1, f2);
		}
	}

}
