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

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;

/**
 * Request object for {@link org.geomajas.command.feature.SearchByLocationCommand}.
 * 
 * @author Joachim Van der Auwera
 */
public class SearchByLocationRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	// -------------------------------------------------------------------------
	// Command statics:
	// -------------------------------------------------------------------------

	/**
	 * Search type in which the layers are searched from the beginning to the end of the array until a result is found.
	 */
	public static final int SEARCH_FIRST_LAYER = 1;

	/**
	 * Search type in which all layers are searched for a result.
	 */
	public static final int SEARCH_ALL_LAYERS = 2;

	/**
	 * Use an intersects query. If ratio is between 0 and 1, it will accept geometries that intersects partially with
	 * the given location. (depending on the value of the ratio).
	 */
	public static final int QUERY_INTERSECTS = 1;

	public static final int QUERY_TOUCHES = 2;

	public static final int QUERY_WITHIN = 3;

	public static final int QUERY_CONTAINS = 4;

	// -------------------------------------------------------------------------
	// Command fields:
	// -------------------------------------------------------------------------

	/**
	 * The geometric description of the location to search features at.
	 */
	private Geometry location;

	/**
	 * <p>
	 * Type of geometric query to be executed. Default is "QUERY_INTERSECTS". All options are:
	 * </p>
	 * <ul>
	 * <li>QUERY_INTERSECTS</li>
	 * <li>QUERY_TOUCHES</li>
	 * <li>QUERY_WITHIN</li>
	 * <li>QUERY_CONTAINS</li>
	 * </ul>
	 */
	private int queryType = QUERY_INTERSECTS;

	/**
	 * This option can only be used in case of an intersects query. It accepts features whose geometry intersect with
	 * the given geometry for at least the given ratio. This number must always be a value between 0 and 1.
	 */
	private float ratio = -1;

	/**
	 * A list of layers we want to retrieve features from.
	 */
	private String[] layerIds;

	/**
	 * The type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2 (=SEARCH_ALL_LAYERS).
	 */
	private int searchType = SEARCH_FIRST_LAYER;

	/**
	 * The coordinate reference system in which to search by location.
	 */
	private String crs;

	/**
	 * The optional buffer that should be added around the location before executing the search.
	 */
	private double buffer = -1;

	private int featureIncludes = 0x7fff;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public SearchByLocationRequest() {
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * The geometric description of the location to search features at.
	 * 
	 * @return location
	 */
	public Geometry getLocation() {
		return location;
	}

	public void setLocation(Geometry location) {
		this.location = location;
	}

	/**
	 * <p>
	 * Type of geometric query to be executed. Default is "QUERY_INTERSECTS". All options are:
	 * </p>
	 * <ul>
	 * <li>QUERY_INTERSECTS</li>
	 * <li>QUERY_TOUCHES</li>
	 * <li>QUERY_WITHIN</li>
	 * <li>QUERY_CONTAINS</li>
	 * </ul>
	 * 
	 * @return query type
	 */
	public int getQueryType() {
		return queryType;
	}

	/**
	 * <p>
	 * Type of geometric query to be executed. Default is "QUERY_INTERSECTS". All options are:
	 * </p>
	 * <ul>
	 * <li>QUERY_INTERSECTS</li>
	 * <li>QUERY_TOUCHES</li>
	 * <li>QUERY_WITHIN</li>
	 * <li>QUERY_CONTAINS</li>
	 * </ul>
	 * 
	 * @param queryType
	 *            The new type of query
	 */
	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	/**
	 * This option can only be used in case of an intersects query. It accepts features whose geometry intersect with
	 * the given geometry for at least the given ratio. This number must always be a value between 0 and 1.
	 * 
	 * @return minimum match ratio
	 */
	public float getRatio() {
		return ratio;
	}

	/**
	 * This option can only be used in case of an intersects query. It accepts features whose geometry intersect with
	 * the given geometry for at least the given ratio. This number must always be a value between 0 and 1.
	 * 
	 * @param ratio
	 *            The new ratio
	 */
	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	/**
	 * Get the list of layers we want to retrieve features from.
	 * 
	 * @return list of layer ID's
	 */
	public String[] getLayerIds() {
		return layerIds;
	}

	/**
	 * Set a new list of layer ID's to search in.
	 * 
	 * @param layerIds
	 */
	public void setLayerIds(String[] layerIds) {
		this.layerIds = layerIds;
	}

	/**
	 * Get the coordinate reference space which should be used for the returned geometries.
	 * 
	 * @return The map's coordinate reference system.
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference space which should be used for the returned geometries.
	 * 
	 * @param crs
	 *            The map's coordinate reference system.
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2 (=SEARCH_ALL_LAYERS).
	 * 
	 * @return search type
	 */
	public int getSearchType() {
		return searchType;
	}

	/**
	 * Set a new type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2 (=SEARCH_ALL_LAYERS).
	 * 
	 * @param searchType search type
	 */
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	/**
	 * Get the optional buffer that should be added around the location before executing the search.
	 * 
	 * @return buffer size
	 */
	public double getBuffer() {
		return buffer;
	}

	/**
	 * Set a buffer that should be added to the location before executing the search.
	 * 
	 * @param buffer buffer size
	 */
	public void setBuffer(double buffer) {
		this.buffer = buffer;
	}

	/**
	 * Get which data should be included in the features. For possible values, see
	 * {@link org.geomajas.service.VectorLayerService}.
	 *
	 * @return what to include
	 */
	public int getFeatureIncludes() {
		return featureIncludes;
	}

	/**
	 * Set the data to include in the features which are returned. For possible values, see
	 * {@link org.geomajas.service.VectorLayerService}.
	 *
	 * @param featureIncludes what the include
	 */
	public void setFeatureIncludes(int featureIncludes) {
		this.featureIncludes = featureIncludes;
	}
}
