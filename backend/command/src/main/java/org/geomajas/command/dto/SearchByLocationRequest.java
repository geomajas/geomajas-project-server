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
package org.geomajas.command.dto;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.command.LayerIdsCommandRequest;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.Api;
import org.geomajas.global.GeomajasConstant;

/**
 * Request object for {@link org.geomajas.command.feature.SearchByLocationCommand}.
 * 
 * @author Joachim Van der Auwera
 * @author Oliver May
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SearchByLocationRequest extends LayerIdsCommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 *
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.feature.SearchByLocation";

	// -------------------------------------------------------------------------
	// Command statics:
	// -------------------------------------------------------------------------

	/**
	 * Search type in which the layers are searched from the beginning to the end of the array until a result is found.
	 * In other words when a layer returns a successful result, the command will look no further.
	 */
	public static final int SEARCH_FIRST_LAYER = 1;

	/**
	 * Search type in which all layers are searched for a result, and more than one result may be returned.
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
	 * The type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2 (=SEARCH_ALL_LAYERS).
	 */
	private int searchType = SEARCH_FIRST_LAYER;

	/**
	 * The coordinate reference system in which to search by location.
	 */
	private String crs;

	private String filter;
	
	private Map<String, String> filters = new HashMap<String, String>();

	/**
	 * The optional buffer that should be added around the location before executing the search.
	 */
	private double buffer = -1;

	private int featureIncludes = GeomajasConstant.FEATURE_INCLUDE_ALL;

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
	 * @param searchType
	 *            search type
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
	 * @param buffer
	 *            buffer size
	 */
	public void setBuffer(double buffer) {
		this.buffer = buffer;
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
	 * @param featureIncludes
	 *            what the include
	 */
	public void setFeatureIncludes(int featureIncludes) {
		this.featureIncludes = featureIncludes;
	}
	
	/**
	 * Set the filter expression which should be applied on the given layer.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it is not converted!
	 *
	 * @param layerId layer to set this filter on
	 * @param filter filter expression
	 * @since 1.9.0
	 */
	public void setFilter(String layerId, String filter) {
		filters.put(layerId, filter);
	}
	/**
	 * Get the filter expression which should be applied on the layer.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it is not converted!
	 *
	 * @return filter expression
	 * @param layerId for the filter
	 * @since 1.9.0
	 */
	public String getFilter(String layerId) {
		return filters.get(layerId);
	}

	/**
	 * Set the filter expression which should be applied on the layer.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it is not converted!
	 * Note that this is a global filter that will be applied to all layers, when filtering on attributes these must
	 * be set on all layers. To add filters to individual layers use {@link #setFilter(String layerId, String filter)}.
	 *
	 * @param filter filter expression
	 * @since 1.8.0
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	/**
	 * Get the filter expression which should be applied on the layer.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it is not converted!
	 * Note that this is a global filter that will be applied to all layers, when filtering on attributes these must
	 * be set on all layers. To add filters to individual layers use {@link #setFilter(String layerId, String filter)}.
	 *
	 * @return filter expression
	 * @since 1.8.0
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Build string representation of object.
	 *
	 * @return string
	 * @since 1.8.0
	 */
	@Override
	public String toString() {
		return "SearchByLocationRequest{" +
				"location=" + location +
				", queryType=" + queryType +
				", ratio=" + ratio +
				", searchType=" + searchType +
				", crs='" + crs + '\'' +
				", filter='" + filter + '\'' +
				", buffer=" + buffer +
				", featureIncludes=" + featureIncludes +
				'}';
	}
}
