/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.widget.searchandfilter.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.geometry.Crs;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerService;
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

	@Autowired
	private VectorLayerService vectorLayerService;

	/**
	 * 
	 * @param criterion
	 * @param mapCrs
	 *            the geometry in geometrycriterion's are expected to be in mapCrs, the will be converted to their
	 *            respective layerCrs's for the filters.
	 * @return
	 * @throws GeomajasException
	 */
	public Map<VectorLayer, Filter> dtoCriterionToFilters(Criterion criterion, Crs mapCrs)
			throws GeomajasException {
		if (criterion != null) {
			if (criterion instanceof AttributeCriterion) {
				return dtoAttributeCriterionToFilters((AttributeCriterion) criterion);

			} else if (criterion instanceof GeometryCriterion) {
				return dtoGeometryCriterionToFilters((GeometryCriterion) criterion, mapCrs);

			} else if (criterion instanceof AndCriterion) {
				AndCriterion critter = (AndCriterion) criterion;
				prune(critter);
				return dtoAndCriterionToFilters(critter, mapCrs);

			} else if (criterion instanceof OrCriterion) {
				return dtoOrCriterionToFilters((OrCriterion) criterion, mapCrs);

			} else {
				throw new GeomajasException(ExceptionCode.ATTRIBUTE_UNKNOWN, criterion.getClass().getName());
			}
		} else {
			return new LinkedHashMap<VectorLayer, Filter>();
		}
	}

	// -------------------------------------------------

	private Map<VectorLayer, Filter> dtoAttributeCriterionToFilters(AttributeCriterion criterion)
			throws GeomajasException {
		Map<VectorLayer, Filter> filters = new LinkedHashMap<VectorLayer, Filter>();
		Filter f;
		VectorLayer l = configurationService.getVectorLayer(criterion.getServerLayerId());
		if (l == null) {
			throw new GeomajasException(ExceptionCode.LAYER_NOT_FOUND, criterion.getServerLayerId());
		}

		String operator = criterion.getOperator();
		if ("LIKE".equals(operator.toUpperCase())) {
			f = filterService.createLikeFilter(criterion.getAttributeName(), criterion.getValue());
		} else if ("DURING".equals(operator.toUpperCase()) || "BEFORE".equals(operator.toUpperCase())
				|| "AFTER".equals(operator.toUpperCase())) {
			f = filterService.parseFilter(criterion.toString()); // In case of a date filter
		} else {
			f = filterService.createCompareFilter(criterion.getAttributeName(), criterion.getOperator(),
					criterion.getValue());
		}

		filters.put(l, f);
		return filters;
	}

	private Map<VectorLayer, Filter> dtoGeometryCriterionToFilters(GeometryCriterion criterion, Crs mapCrs)
			throws GeomajasException {
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
			Geometry layerGeometry = geoService.transform(mapGeom, mapCrs, vectorLayerService.getCrs(vl));

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

	private Map<VectorLayer, Filter> dtoAndCriterionToFilters(AndCriterion criterion, Crs mapCrs)
			throws GeomajasException {
		Map<VectorLayer, Filter> filters = new LinkedHashMap<VectorLayer, Filter>();
		for (Criterion critter : criterion.getCriteria()) {
			combineFilters(filters, dtoCriterionToFilters(critter, mapCrs), "AND");
		}
		return filters;
	}

	private Map<VectorLayer, Filter> dtoOrCriterionToFilters(OrCriterion criterion, Crs mapCrs)
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

	/**
	 * Prune impossible combinations. 
	 * (eg. If And criteria filter different layers, they will return nothing, so they are pruned).
	 */
	void prune(AndCriterion criterion) {
		Set<String> usedLayers = new HashSet<String>();
		Set<String> badLayers = new HashSet<String>();
		criterion.serverLayerIdVisitor(usedLayers);
		findUnmatchedLayers(criterion, usedLayers, badLayers);
		if (usedLayers.isEmpty()) {
			criterion.getCriteria().clear();
		} else if (!badLayers.isEmpty()) {
			removeUnmatchedLayers(criterion, badLayers);
		}
	}

	private void findUnmatchedLayers(AndCriterion criterion, Set<String> usedLayers, Set<String> badLayers) {
		for (Criterion critter : criterion.getCriteria()) {
			if (critter instanceof AttributeCriterion) {
				findUnmatchedLayers((AttributeCriterion) critter, usedLayers, badLayers);

			} else if (critter instanceof GeometryCriterion) {
				findUnmatchedLayers((GeometryCriterion) critter, usedLayers, badLayers);

			} else if (critter instanceof AndCriterion) {
				findUnmatchedLayers((AndCriterion) critter, usedLayers, badLayers);

			} else if (critter instanceof OrCriterion) {
				findUnmatchedLayers((OrCriterion) critter, usedLayers, badLayers);

			} else {
				throw new IllegalStateException("Unknown CriteriaType: " + critter.getClass().getSimpleName());
			}

			if (usedLayers.size() == 0) { // no point in continuing.
				return;
			}
		}
	}

	private void findUnmatchedLayers(OrCriterion criterion, Set<String> usedLayers, Set<String> badLayers) {
		// aggregate
		Set<String> goodLayers = new HashSet<String>();
		for (Criterion critter : criterion.getCriteria()) {
			if (critter instanceof AttributeCriterion) {
				goodLayers.add(((AttributeCriterion) critter).getServerLayerId());

			} else if (critter instanceof GeometryCriterion) {
				goodLayers.addAll(((GeometryCriterion) critter).getServerLayerIds());

			} else if (critter instanceof AndCriterion) {
				AndCriterion ac = (AndCriterion) critter;
				Set<String> usedLayersInt = new HashSet<String>();
				Set<String> badLayersInt = new HashSet<String>();
				ac.serverLayerIdVisitor(usedLayersInt);
				findUnmatchedLayers(ac, usedLayersInt, badLayersInt);
				goodLayers.addAll(usedLayersInt);

			} else if (critter instanceof OrCriterion) {
				OrCriterion oc = (OrCriterion) critter;
				Set<String> usedLayersInt = new HashSet<String>();
				Set<String> badLayersInt = new HashSet<String>();
				oc.serverLayerIdVisitor(usedLayersInt);
				findUnmatchedLayers(oc, usedLayersInt, badLayersInt);
				goodLayers.addAll(usedLayersInt);

			} else {
				throw new IllegalStateException("Unknown CriteriaType: " + critter.getClass().getSimpleName());
			}
		}

		List<String> baduns = new ArrayList<String>();
		for (String layer : usedLayers) {
			if (!goodLayers.contains(layer)) {
				baduns.add(layer);
			}
		}
		usedLayers.removeAll(baduns);
		badLayers.addAll(baduns);
	}

	private void findUnmatchedLayers(AttributeCriterion criterion, Set<String> usedLayers, Set<String> badLayers) {
		for (String lid : usedLayers) {
			if (!criterion.getServerLayerId().equals(lid)) {
				badLayers.add(lid);
			}
		}
		usedLayers.removeAll(badLayers);
	}

	private void findUnmatchedLayers(GeometryCriterion criterion, Set<String> usedLayers, Set<String> badLayers) {
		List<String> layers = criterion.getServerLayerIds();
		for (String lid : usedLayers) {
			if (!layers.contains(lid)) {
				badLayers.add(lid);
			}
		}
		usedLayers.removeAll(badLayers);
	}

	private void removeUnmatchedLayers(Criterion criterion, Set<String> badLayers) {
		List<Criterion> toRemove = new ArrayList<Criterion>();
		for (Criterion critter : criterion.getCriteria()) {
			if (critter instanceof AttributeCriterion) {
				AttributeCriterion ac = (AttributeCriterion) critter;
				if (badLayers.contains(ac.getServerLayerId())) {
					toRemove.add(ac);
				}

			} else if (critter instanceof GeometryCriterion) {
				GeometryCriterion gc = (GeometryCriterion) critter;
				gc.getServerLayerIds().removeAll(badLayers);
				if (gc.getServerLayerIds().isEmpty()) {
					toRemove.add(gc);
				}

			} else {
				removeUnmatchedLayers(critter, badLayers);
				if (critter.getCriteria().isEmpty()) {
					toRemove.add(critter);
				}
			}
		}
		if (!toRemove.isEmpty()) {
			criterion.getCriteria().removeAll(toRemove);
		}
	}
}
