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
package org.geomajas.widget.searchandfilter.client.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.command.dto.SearchByLocationRequest;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.GeometryCriterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;

import com.google.gwt.core.client.GWT;

/**
 * Utility methods for working with criterions.
 *
 * @author Kristof Heirwegh
 */
public final class CriterionUtil {

	private static final List<Criterion> ACTIVE_LAYER_FILTER_CRITERIONS = new ArrayList<Criterion>();

	/**
	 * Utility class
	 */
	private CriterionUtil() {
	}

	public static void clearLayerFilters(MapWidget mapWidget) {
		for (VectorLayer layer : mapWidget.getMapModel().getVectorLayers()) {
			if (layer.getFilter() != null) {
				layer.setFilter(null);
			}
		}
		ACTIVE_LAYER_FILTER_CRITERIONS.clear();
	}

	public static void setLayerFilter(final MapWidget mapWidget, final Criterion criterion) {
		Map<String, String> filters = criterionToFilters(criterion, mapWidget.getMapModel());
		if (filters != null && !filters.isEmpty()) {
			List<VectorLayer> vls = mapWidget.getMapModel().getVectorLayers();
			for (Entry<String, String> entry : filters.entrySet()) {
				for (VectorLayer vl : vls) {
					if (entry.getKey().equals(vl.getServerLayerId())) {
						vl.setFilter(entry.getValue());
						GWT.log("Setting layerfilter: " + entry.getKey() + " - " + entry.getValue());
					}
				}
			}
		}
		ACTIVE_LAYER_FILTER_CRITERIONS.add(criterion);
	}

	/**
	 * This does the same as dtoConverterservice but uses strings for filters instead of (serverside) filter objects.
	 * 
	 * @param criterion
	 * @param mapModel
	 */
	public static Map<String, String> criterionToFilters(Criterion criterion, MapModel mapModel) {
		if (criterion != null) {
			if (criterion instanceof AttributeCriterion) {
				return attributeCriterionToFilters((AttributeCriterion) criterion);

			} else if (criterion instanceof GeometryCriterion) {
				return geometryCriterionToFilters((GeometryCriterion) criterion, mapModel);

			} else if (criterion instanceof AndCriterion) {
				return andCriterionToFilters((AndCriterion) criterion, mapModel);

			} else if (criterion instanceof OrCriterion) {
				return orCriterionToFilters((OrCriterion) criterion, mapModel);

			} else {
				return null;
			}
		} else {
			return new LinkedHashMap<String, String>();
		}
	}

	// ----------------------------------------------------------

	private static Map<String, String> attributeCriterionToFilters(AttributeCriterion criterion) {
		Map<String, String> filters = new LinkedHashMap<String, String>();
		String filter = criterion.getAttributeName() + " " + criterion.getOperator() + " ";
		if (criterion.getValue() != null
				&& ((criterion.getValue().contains("\"") || criterion.getValue().contains(" ") || !criterion.getValue()
						.matches("(\\.|\\-|[0-9])*")) || "LIKE".equalsIgnoreCase(criterion.getOperator()))
				&& ((!"DURING".equals(criterion.getOperator().toUpperCase())
						&& !"BEFORE".equals(criterion.getOperator().toUpperCase()) && !"AFTER".equals(criterion
						.getOperator().toUpperCase())))) {
			filter += "\'" + criterion.getValue().replaceAll("\\*", "%") + "\'";
		} else {
			filter += criterion.getValue();
		}
		filters.put(criterion.getServerLayerId(), filter);
		return filters;
	}

	private static Map<String, String> geometryCriterionToFilters(GeometryCriterion criterion, MapModel mapModel) {
		Map<String, String> filters = new LinkedHashMap<String, String>();
		org.geomajas.gwt.client.spatial.geometry.Geometry mapGeom = GeometryConverter.toGwt(criterion.getGeometry());
		String wktGeom = mapGeom.toWkt();
		String method;
		switch (criterion.getOperator()) {
			case SearchByLocationRequest.QUERY_INTERSECTS:
				method = "INTERSECTS";
				break;
			case SearchByLocationRequest.QUERY_CONTAINS:
				method = "CONTAINS";
				break;
			case SearchByLocationRequest.QUERY_TOUCHES:
				method = "TOUCHES";
				break;
			case SearchByLocationRequest.QUERY_WITHIN:
				method = "WITHIN";
				break;
			default:
				return null;
		}
		for (String serverLayerId : criterion.getServerLayerIds()) {
			VectorLayer vl = findVectorLayer(mapModel, serverLayerId);
			if (vl != null) {
				String geomAttName = vl.getLayerInfo().getFeatureInfo().getGeometryType().getName();
				filters.put(serverLayerId, method + "(" + geomAttName + ", " + wktGeom + ")");
			}
		}
		return filters;
	}

	private static Map<String, String> andCriterionToFilters(AndCriterion criterion, MapModel mapModel) {
		Map<String, String> filters = new LinkedHashMap<String, String>();
		for (Criterion critter : criterion.getCriteria()) {
			combineFilters(filters, criterionToFilters(critter, mapModel), " AND ");
		}
		return filters;
	}

	private static Map<String, String> orCriterionToFilters(OrCriterion criterion, MapModel mapModel) {
		Map<String, String> filters = new LinkedHashMap<String, String>();
		for (Criterion critter : criterion.getCriteria()) {
			combineFilters(filters, criterionToFilters(critter, mapModel), " OR ");
		}
		return filters;
	}

	private static void combineFilters(Map<String, String> keep, Map<String, String> add, String method) {
		if (add.size() != 0) {
			for (Entry<String, String> entry : add.entrySet()) {
				if (keep.containsKey(entry.getKey())) {
					keep.put(entry.getKey(), "(" + keep.get(entry.getKey()) + method + entry.getValue() + ")");
				} else {
					keep.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}

	private static VectorLayer findVectorLayer(MapModel mapModel, String serverLayerId) {
		List<VectorLayer> vls = mapModel.getVectorLayersByServerId(serverLayerId);
		if (vls != null && vls.size() > 0) {
			return vls.get(0);
		} else {
			return null;
		}
	}

	public static boolean isActiveLayerFilter(Criterion criterion) {
		return ACTIVE_LAYER_FILTER_CRITERIONS.contains(criterion);
	}
	
	/**
	 * @return true if at least one layer is filtered by a criterion.
	 */
	public static boolean isFilterActive() {
		return ACTIVE_LAYER_FILTER_CRITERIONS.size() > 0;
	}
}
