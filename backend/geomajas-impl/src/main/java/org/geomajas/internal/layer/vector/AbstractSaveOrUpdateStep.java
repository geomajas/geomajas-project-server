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

package org.geomajas.internal.layer.vector;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.FilterService;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base step for saveOrUpdate pipeline. Has  helper method for building a filter to check compliance with the
 * layer security filter, layer default filter and area.
 *
 * @author Joachim Van der Auwera
 */
public abstract class AbstractSaveOrUpdateStep implements PipelineStep {

	private final Logger log = LoggerFactory.getLogger(AbstractSaveOrUpdateStep.class);

	@Autowired
	protected SecurityContext securityContext;

	@Autowired
	private FilterService filterService;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	protected Filter getSecurityFilter(VectorLayer layer, Geometry geometry) throws GeomajasException {
		String layerId = layer.getId();

		// apply generic security filter
		Filter filter = securityContext.getFeatureFilter(layerId);

		// apply default filter
		String defaultFilter = layer.getLayerInfo().getFilter();
		if (null != defaultFilter) {
			filter = and(filter, filterService.parseFilter(defaultFilter));
		}

		// apply visible area filter
		if (null != geometry) {
			String geometryName = layer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
			if (securityContext.isPartlyVisibleSufficient(layerId)) {
				filter = and(filter, filterService.createIntersectsFilter(geometry, geometryName));
			} else {
				filter = and(filter, filterService.createWithinFilter(geometry, geometryName));
			}
		} else {
			log.warn("Usable area is null for layer " + layerId + "removing all content!");
			filter = filterService.createFalseFilter();
		}

		return filter;
	}

	protected Filter and(Filter f1, Filter f2) {
		if (null == f1) {
			return f2;
		} else if (null == f2) {
			return f1;
		} else {
			return filterService.createAndFilter(f1, f2);
		}
	}

}
