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
package org.geomajas.widget.searchandfilter.command.dto;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.command.CommandRequest;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.SortDirection;


/**
 * Request for {@link org.geomajas.widget.searchandfilter.command.searchandfilter.FeatureSearchCommand}.
 * 
 * @author Kristof Heirwegh
 */
public class FeatureSearchRequest implements CommandRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND = "command.searchandfilter.FeatureSearch";

	public static final int MAX_UNLIMITED = 0;

	private int featureIncludes = GeomajasConstant.FEATURE_INCLUDE_ALL;
	
	//Note that these sorting config fields have no effect on v1.0 implementations
	private String sortByFieldName; /* if null: no sorting */
	private SortDirection sortDirection = SortDirection.ASCENDING;

	private Criterion criterion;

	/**
	 * ServerLayerId, filterstring.
	 */
	private Map<String, String> layerFilters;

	private String mapCrs;

	/**
	 * On a per layer basis.
	 */
	private int max = MAX_UNLIMITED;

	// ----------------------------------------------------------

	public Criterion getCriterion() {
		return criterion;
	}

	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	public String getMapCrs() {
		return mapCrs;
	}

	public void setMapCrs(String mapCrs) {
		this.mapCrs = mapCrs;
	}

	public int getFeatureIncludes() {
		return featureIncludes;
	}

	public void setFeatureIncludes(int featureIncludes) {
		this.featureIncludes = featureIncludes;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * @return sortByFieldName; if null: no sorting
	 */
	public String getSortByField() {
		return sortByFieldName;
	}

	/**
	 * @param sortByFieldName  if null: no sorting
	 */
	public void setSortByFieldName(String sortByFieldName) {
		this.sortByFieldName = sortByFieldName;
	}

	public SortDirection getSortDirection() {
		return sortDirection;
	}
	
	/**
	 * @param sortDirection (SortDirection):  direction for sorting when the field name for 
	 * 					sorting has been by specified by setSortByFieldName
	 */
	public void setSortDirection(SortDirection sortDirection) {
		if (null != sortDirection) {
			this.sortDirection = sortDirection;
		}
	}

	public Map<String, String> getLayerFilters() {
		if (layerFilters == null) {
			layerFilters = new HashMap<String, String>();
		}
		return layerFilters;
	}

	public void setLayerFilters(Map<String, String> layerFilters) {
		this.layerFilters = layerFilters;
	}
}
