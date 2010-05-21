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
package org.geomajas.command.dto;

import org.geomajas.command.LayerIdCommandRequest;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.layer.feature.SearchCriterion;

import java.util.Arrays;

/**
 * Request object for {@link org.geomajas.command.feature.SearchFeatureCommand}.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SearchFeatureRequest extends LayerIdCommandRequest {

	private static final long serialVersionUID = 151L;

	public static final int MAX_UNLIMITED = 0;

	public static final String ID_ATTRIBUTE = "$id";
	
	private int max = MAX_UNLIMITED;

	private String booleanOperator = "AND";

	private SearchCriterion[] criteria;

	private String filter;

	private String crs;

	private int featureIncludes = GeomajasConstant.FEATURE_INCLUDE_ALL;

	public SearchFeatureRequest() {
	}

	/**
	 * Get the maximum number of feature which may be returned.
	 *
	 * @return max number of features to return o 0 for unlimited.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Set the maximum number of features which may be returned.
	 *
	 * @param max max number of feature to return, or 0 for unlimited.
	 */
	public void setMax(int max) {
		this.max = max;
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
				", crs='" + crs + '\'' +
				", featureIncludes=" + featureIncludes +
				'}';
	}
}
