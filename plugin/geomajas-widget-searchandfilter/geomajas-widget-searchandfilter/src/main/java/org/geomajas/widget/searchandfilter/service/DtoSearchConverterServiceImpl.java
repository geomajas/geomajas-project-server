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
package org.geomajas.widget.searchandfilter.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.service.ConfigurationService;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.FilterService;
import org.geomajas.service.GeoService;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.GeometryCriterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Converts Dto Criterion to map of filters.
 * 
 * @author Kristof Heirwegh
 * 
 */
@Service("DtoSearchConverterService")
public class DtoSearchConverterServiceImpl implements DtoSearchConverterService {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(DtoSearchConverterServiceImpl.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DtoConverterService converter;

	@Autowired
	private GeoService geoService;

	@Autowired
	private FilterService filterService;

	/**
	 * 
	 * @param criterion
	 * @param mapCrs
	 *            the geometry in geometrycriterion's are expected to be in mapCrs, the will be converted to their
	 *            respective layerCrs's for the filters.
	 * @return
	 * @throws GeomajasException
	 */
	public Map<VectorLayer, Filter> dtoCriterionToFilters(Criterion criterion, CoordinateReferenceSystem mapCrs)
			throws GeomajasException {
		if (criterion != null) {
			if (criterion instanceof AttributeCriterion) {
				return dtoAttributeCriterionToFilters((AttributeCriterion) criterion);

			} else if (criterion instanceof GeometryCriterion) {
				return dtoGeometryCriterionToFilters((GeometryCriterion) criterion, mapCrs);

			} else if (criterion instanceof AndCriterion) {
				return dtoAndCriterionToFilters((AndCriterion) criterion, mapCrs);

			} else if (criterion instanceof OrCriterion) {
				return dtoOrCriterionToFilters((OrCriterion) criterion, mapCrs);

			} else {
				throw new GeomajasException(ExceptionCode.ATTRIBUTE_UNKNOWN, criterion.getClass().getName());
			}
		} else {
			return new LinkedHashMap<VectorLayer, Filter>();
		}
	}

	private Map<VectorLayer, Filter> dtoAttributeCriterionToFilters(AttributeCriterion criterion)
			throws GeomajasException {
		Map<VectorLayer, Filter> filters = new LinkedHashMap<VectorLayer, Filter>();
		Filter f = null;
		VectorLayer l = configurationService.getVectorLayer(criterion.getServerLayerId());
		if (l == null) {
			throw new GeomajasException(ExceptionCode.LAYER_NOT_FOUND, criterion.getServerLayerId());
		}
		
		String operator = criterion.getOperator();
		if("LIKE".equals(operator.toUpperCase())){
			f = filterService.createLikeFilter(criterion.getAttributeName(), criterion.getValue());
		}else if("DURING".equals(operator.toUpperCase()) || "BEFORE".equals(operator.toUpperCase())
				|| "AFTER".equals(operator.toUpperCase())){
			f = filterService.parseFilter(criterion.toString()); // In case of a date filter
		}else{
			f = filterService.createCompareFilter(criterion.getAttributeName(), criterion.getOperator(),
					criterion.getValue());
		}

		filters.put(l, f);
		return filters;
	}

	private Map<VectorLayer, Filter> dtoGeometryCriterionToFilters(GeometryCriterion criterion,
			CoordinateReferenceSystem mapCrs) throws GeomajasException {
		if (mapCrs == null) {
			throw new GeomajasException(ExceptionCode.PARAMETER_MISSING, "mapCrs");
		}

		Map<VectorLayer, Filter> filters = new LinkedHashMap<VectorLayer, Filter>();
		Filter f;
		Geometry mapGeom = converter.toInternal(criterion.getGeometry());

		for (String serverLayerId : criterion.getServerLayerIds()) {
			VectorLayer vl = configurationService.getVectorLayer(serverLayerId);
			if (vl == null) {
				throw new GeomajasException(ExceptionCode.LAYER_NOT_FOUND, serverLayerId);
			}

			// Transform geometry to layer CRS:
			Geometry layerGeometry = geoService.transform(mapGeom, mapCrs, vl.getCrs());

			switch (criterion.getOperator()) {
				case SearchByLocationRequest.QUERY_INTERSECTS:
					f = filterService.createIntersectsFilter(layerGeometry, vl.getFeatureModel()
							.getGeometryAttributeName());
					break;
				case SearchByLocationRequest.QUERY_CONTAINS:
					f = filterService.createContainsFilter(layerGeometry, vl.getFeatureModel()
							.getGeometryAttributeName());
					break;

				case SearchByLocationRequest.QUERY_TOUCHES:
					f = filterService.createTouchesFilter(layerGeometry, vl.getFeatureModel()
							.getGeometryAttributeName());
					break;

				case SearchByLocationRequest.QUERY_WITHIN:
					f = filterService
							.createWithinFilter(layerGeometry, vl.getFeatureModel().getGeometryAttributeName());
					break;

				default:
					throw new GeomajasException(ExceptionCode.ATTRIBUTE_UNKNOWN, "QueryType");
			}

			filters.put(vl, f);
		}
		return filters;
	}

	private Map<VectorLayer, Filter> dtoAndCriterionToFilters(AndCriterion criterion, CoordinateReferenceSystem mapCrs)
			throws GeomajasException {
		Map<VectorLayer, Filter> filters = new LinkedHashMap<VectorLayer, Filter>();
		for (Criterion critter : criterion.getCriteria()) {
			combineFilters(filters, dtoCriterionToFilters(critter, mapCrs), "AND");
		}
		return filters;
	}

	private Map<VectorLayer, Filter> dtoOrCriterionToFilters(OrCriterion criterion, CoordinateReferenceSystem mapCrs)
			throws GeomajasException {
		Map<VectorLayer, Filter> filters = new LinkedHashMap<VectorLayer, Filter>();
		for (Criterion critter : criterion.getCriteria()) {
			combineFilters(filters, dtoCriterionToFilters(critter, mapCrs), "OR");
		}
		return filters;
	}

	private void combineFilters(Map<VectorLayer, Filter> keep, Map<VectorLayer, Filter> add, String method) {
		if (add.size() != 0) {
			for (Entry<VectorLayer, Filter> entry : add.entrySet()) {
				if (keep.containsKey(entry.getKey())) {
					keep.put(entry.getKey(),
							filterService.createLogicFilter(keep.get(entry.getKey()), method, entry.getValue()));
				} else {
					keep.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}
}
