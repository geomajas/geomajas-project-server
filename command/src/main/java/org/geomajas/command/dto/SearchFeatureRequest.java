/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import java.util.Arrays;

import org.geomajas.annotation.Api;
import org.geomajas.command.LayerIdCommandRequest;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.feature.SearchCriterion;

/**
 * Request object for {@link org.geomajas.command.feature.SearchFeatureCommand}.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SearchFeatureRequest extends LayerIdCommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.feature.Search";

	/** Use this number as maximum to indicate that all results should be returned. */
	public static final int MAX_UNLIMITED = 0;

	/** Field ID to use to always get the id field. */
	public static final String ID_ATTRIBUTE = "$id";
	
	private int max = MAX_UNLIMITED;
	
	private int offSet;

	private String booleanOperator = "AND";

	private SearchCriterion[] criteria;

	private String filter;

	private String crs;

	private int featureIncludes = GeomajasConstant.FEATURE_INCLUDE_ALL;

	/**
	 * Get the maximum number of features which may be returned.
	 * <p/>
	 * The default value is to have unlimited of features returned.
	 *
	 * @return max number of features to return of {@link #MAX_UNLIMITED} (0) for unlimited.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Set the maximum number of features which may be returned.
	 *
	 * @param max max number of features to return, or {@link #MAX_UNLIMITED} (0) for unlimited.
	 */
	public void setMax(int max) {
		this.max = max;
	}	

	/**
	 * Get the index of the first feature to be returned. This is useful for layers that support paging. 
	 * 
	 * @return the offset
	 * @since 1.10.0
	 */
	public int getOffSet() {
		return offSet;
	}

	/**
	 * Set the index of the first feature to be returned. This is useful for layers that support paging.
	 * 
	 * @param offSet the offset
	 * @since 1.10.0
	 */
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	/**
	 * Get boolean operator for combining the different search criteria.
	 *
	 * @return boolean operator, possible values include "and" and "or"
	 */
	public String getBooleanOperator() {
		return booleanOperator;
	}

	/**
	 * Set boolean operator for combining the different search criteria.
	 *
	 * @param booleanOperator boolean operator, possible values include "and" and "or"
	 */
	public void setBooleanOperator(String booleanOperator) {
		this.booleanOperator = booleanOperator;
	}

	/**
	 * Get the list of search criteria.
	 *
	 * @return list of search criteria
	 */
	public SearchCriterion[] getCriteria() {
		return criteria;
	}

	/**
	 * Set the list of search criteria.
	 *
	 * @param criteria list of criteria
	 */
	public void setCriteria(SearchCriterion[] criteria) {
		this.criteria = criteria;
	}

	/**
	 * Get the filter expression which should be applied on the layer.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it is not converted!
	 *
	 * @return filter expression
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Set the filter expression which should be applied on the layer.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it is not converted!
	 *
	 * @param filter filter expression
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get the coordinate reference space which should be used for the returned geometries.
	 *
	 * @return crs
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference space which should be used for the returned geometries.
	 *
	 * @param crs crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get which data should be included in the features. For possible values, see
	 * {@link org.geomajas.layer.VectorLayerService}.
	 *
	 * @return what to include
	 */
	public int getFeatureIncludes() {
		return featureIncludes;
	}

	/**
	 * Set the data to include in the features which are returned. For possible values, see
	 * {@link org.geomajas.layer.VectorLayerService}.
	 *
	 * @param featureIncludes what the include
	 */
	public void setFeatureIncludes(int featureIncludes) {
		this.featureIncludes = featureIncludes;
	}

	@Override
	public String toString() {
		return "SearchFeatureRequest{" +
				"max=" + max +
				", layerId=" + getLayerId() +
				", crs=" + getCrs() +
				", booleanOperator='" + booleanOperator + '\'' +
				", criteria=" + (criteria == null ? null : Arrays.asList(criteria)) +
				", filter='" + filter + '\'' +
				", featureIncludes=" + featureIncludes +
				'}';
	}
}
