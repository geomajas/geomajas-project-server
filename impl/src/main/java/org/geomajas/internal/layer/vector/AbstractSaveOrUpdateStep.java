/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.layer.vector;

import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.FilterService;
import org.geomajas.service.pipeline.PipelineStep;
import org.opengis.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Geometry;

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
		return getSecurityFilter(null, layer, geometry);
	}

	protected Filter getSecurityFilter(Filter baseFilter, VectorLayer layer, Geometry geometry)
			throws GeomajasException {
		String layerId = layer.getId();

		// apply generic security filter
		Filter filter = and(baseFilter, securityContext.getFeatureFilter(layerId));

		// apply default filter
		String defaultFilter = layer.getLayerInfo().getFilter();
		if (null != defaultFilter) {
			filter = and(filter, filterService.parseFilter(defaultFilter));
		}

		// apply area filter
		if (!layer.getLayerInfo().isAllowEmptyGeometries()) {
			if (null != geometry) {
				String geometryName = layer.getLayerInfo().getFeatureInfo().getGeometryType().getName();
				Filter areaFilter;
				if (securityContext.isPartlyVisibleSufficient(layerId)) {
					areaFilter = filterService.createIntersectsFilter(geometry, geometryName);
				} else {
					areaFilter = filterService.createWithinFilter(geometry, geometryName);
				}
				filter = and(filter, areaFilter);
			} else {
				log.warn("Usable area is null for layer " + layerId + "removing all content!");
				filter = filterService.createFalseFilter();
			}
		}

		if (null == filter) {
			filter = filterService.createTrueFilter();
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
