/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.command.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.GeomajasConstant;

/**
 * Request object for
 * {@link org.geomajas.command.feature.SearchByLocationCommand}.
 * 
 * @author Joachim Van der Auwera
 * @author Oliver May
 * @author An Buyle
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SearchByLocationRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	/**
	 * Command name for this request.
	 * 
	 * @since 1.9.0
	 * */
	public static final String COMMAND = "command.feature.SearchByLocation";

	/**
	 * Search type in which the layers are searched from the beginning to the
	 * end of the array until a result is found. In other words when a layer
	 * returns a successful result, the command will look no further.
	 */
	public static final int SEARCH_FIRST_LAYER = 1;

	/**
	 * Search type in which all layers are searched for a result, and more than
	 * one result may be returned.
	 */
	public static final int SEARCH_ALL_LAYERS = 2;

	/**
	 * Use an intersects query. If ratio is between 0 and 1, it will accept
	 * geometries that intersects partially with the given location. (depending
	 * on the value of the ratio).
	 */
	public static final int QUERY_INTERSECTS = 1;

	/**
	 * Use touches query.
	 */
	public static final int QUERY_TOUCHES = 2;

	/**
	 * Use within query.
	 */
	public static final int QUERY_WITHIN = 3;

	/**
	 * Use contains query.
	 */
	public static final int QUERY_CONTAINS = 4;

	/**
	 * The geometric description of the location to search features at.
	 */
	private Geometry location;

	/**
	 * <p>
	 * Type of geometric query to be executed. Default is "QUERY_INTERSECTS".
	 * All options are:
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
	 * This option can only be used in case of an intersects query. It accepts
	 * features whose geometry intersect with the given geometry for at least
	 * the given ratio. This number must always be a value between 0 and 1.
	 */
	private float ratio = -1;

	/**
	 * The type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2
	 * (=SEARCH_ALL_LAYERS).
	 */
	private int searchType = SEARCH_FIRST_LAYER;

	/**
	 * The coordinate reference system in which to search by location.
	 */
	private String crs;

	private String filter;

	private Map<String, LayerFilterSpecification> layerFilters = new HashMap<String, LayerFilterSpecification>();

	/**
	 * The optional buffer that should be added around the location before
	 * executing the search.
	 */
	private double buffer = -1;

	private int featureIncludes = GeomajasConstant.FEATURE_INCLUDE_ALL;

	/**
	 * The geometric description of the location to search features at.
	 * 
	 * @return location
	 */
	public Geometry getLocation() {
		return location;
	}

	/**
	 * Set the geometry to use as search location.
	 *
	 * @param location location
	 */
	public void setLocation(Geometry location) {
		this.location = location;
	}

	/**
	 * <p>
	 * Type of geometric query to be executed. Default is "QUERY_INTERSECTS".
	 * All options are:
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
	 * Type of geometric query to be executed. Default is "QUERY_INTERSECTS".
	 * All options are:
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
	 * This option can only be used in case of an intersects query. It accepts
	 * features whose geometry intersect with the given geometry for at least
	 * the given ratio. This number must always be a value between 0 and 1.
	 * 
	 * @return minimum match ratio
	 */
	public float getRatio() {
		return ratio;
	}

	/**
	 * This option can only be used in case of an intersects query. It accepts
	 * features whose geometry intersect with the given geometry for at least
	 * the given ratio. This number must always be a value between 0 and 1.
	 * 
	 * @param ratio
	 *            The new ratio
	 */
	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	/**
	 * Get the coordinate reference space which should be used for the returned
	 * geometries.
	 * 
	 * @return The map's coordinate reference system.
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference space which should be used for the returned
	 * geometries.
	 * 
	 * @param crs
	 *            The map's coordinate reference system.
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2
	 * (=SEARCH_ALL_LAYERS).
	 * 
	 * @return search type
	 */
	public int getSearchType() {
		return searchType;
	}

	/**
	 * Set a new type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2
	 * (=SEARCH_ALL_LAYERS).
	 * 
	 * @param searchType
	 *            search type
	 */
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	/**
	 * Get the optional buffer that should be added around the location before
	 * executing the search.
	 * 
	 * @return buffer size
	 */
	public double getBuffer() {
		return buffer;
	}

	/**
	 * Set a buffer that should be added to the location before executing the
	 * search.
	 * 
	 * @param buffer
	 *            buffer size
	 */
	public void setBuffer(double buffer) {
		this.buffer = buffer;
	}

	/**
	 * Get which data should be included in the features. For possible values,
	 * see {@link org.geomajas.layer.VectorLayerService}.
	 * 
	 * @return what to include
	 */
	public int getFeatureIncludes() {
		return featureIncludes;
	}

	/**
	 * Set the data to include in the features which are returned. For possible
	 * values, see {@link org.geomajas.layer.VectorLayerService}.
	 * 
	 * @param featureIncludes
	 *            what the include
	 */
	public void setFeatureIncludes(int featureIncludes) {
		this.featureIncludes = featureIncludes;
	}

	/**
	 * Add a layer with an optional filter expression which should be applied on
	 * the given layer.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it
	 * is not converted!
	 * 
	 * @param resultTag
	 *            tag to make the distinction in the response object between the
	 *            features for the same serverLayerId but a different filter
	 *            (e.g. client layer id)
	 * @param serverLayerId
	 *            server layerId layer to set this filter on
	 * @param filter
	 *            filter expression for the specified layer, can be null (==true
	 *            filter)no client layer specific filtering
	 * 
	 * @since 1.10.0
	 */
	public void addLayerWithFilter(String resultTag, String serverLayerId,
			String filter) {
		// note: layer filter specification will be overwritten if it already
		// exists
		layerFilters.put(resultTag, new LayerFilterSpecification(serverLayerId,
				filter));
	}

	/**
	 * Set the filter expression which should be applied on the given server
	 * layer. If there has already been specified a filter for that server
	 * layer, it will be overwritten.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it
	 * is not converted!
	 * 
	 * @param serverLayerId
	 *            server layer to set this filter on
	 * @param filter
	 *            filter expression
	 * 
	 * @deprecated use {@link #addLayerWithFilter(String, String, String)}.
	 * @since 1.9.0
	 */
	@Deprecated
	public void setFilter(String serverLayerId, String filter) {
		addLayerWithFilter(serverLayerId, serverLayerId, filter);
	}

	/**
	 * Get the filter expression which should be applied on the layer for the
	 * specified result tag (e.g. client layer id) as specified in the filters
	 * map.
	 * 
	 * @return filter expression, which can be null if no layer specific filter
	 *         needs to be applied (=true filter)
	 * 
	 * @param resultTag
	 *            result tag for the layer filter specification
	 * @since 1.9.0
	 */
	public String getFilter(String resultTag) {
		return layerFilters.get(resultTag).getFilter();
	}

	/**
	 * Get the server layer id for the specified result tag (e.g. client layer
	 * id).
	 * 
	 * @param resultTag
	 *            result tag for the layer filter specification (e.g. client
	 *            layer id)
	 * 
	 * @return server layer id
	 * 
	 * @since 1.10.0
	 */
	public String getServerLayerId(String resultTag) {
		return layerFilters.get(resultTag).getServerLayerId();
	}

	/**
	 * Get the result tags for layer specific filtering (e.g. client layer
	 * id's).
	 *
	 * @return result tags for layer specific filtering (e.g. client layer id's)
	 * @since 1.10.0
	 */
	public String[] getLayerIds() {
		Set<String> layerIds = layerFilters.keySet();
		if (layerIds == null) {
			return new String[0];
		}
		return layerIds.toArray(new String[layerIds.size()]);
	}

	/**
	 * Set the server layer ids.
	 * <p/>
	 * Note: use {@link #addLayerWithFilter(String, String, String)} to specify
	 * filter expressions
	 *
	 * @param serverLayerIds
	 *            server layer ids
	 *
	 * @since 1.9.0
	 */
	public void setLayerIds(String[] serverLayerIds) {
		for (String serverLayerId : serverLayerIds) {
			addLayerWithFilter(serverLayerId, serverLayerId, null);
		}
	}

	/**
	 * Set the global filter expression which should be applied.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it
	 * is not converted! Note that this is a global filter that will be applied
	 * to all layers, when filtering on attributes these must be set on all
	 * layers. To add filters to individual layers use
	 * {@link #setFilter(String layerId, String filter)}.
	 * 
	 * @param filter
	 *            filter expression
	 * @since 1.8.0
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get the global filter expression which should be applied on all layers.
	 * <p/>
	 * If the filter contains a geometry, then this needs to be in layer CRS, it
	 * is not converted! Note that this is a global filter that will be applied
	 * to all layers, when filtering on attributes these must be set on all
	 * layers. To add filters to individual layers use
	 * {@link #setFilter(String layerId, String filter)}.
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
		StringBuilder value = new StringBuilder();
		value.append("SearchByLocationRequest{");
		value.append("location=");
		value.append(location);
		value.append(", queryType=");
		value.append(queryType);
		value.append(", ratio=");
		value.append(ratio);
		value.append(", searchType=");
		value.append(searchType);
		value.append(", crs='");
		value.append(crs);
		value.append('\'');
		value.append(", global filter='");
		value.append(filter);
		value.append('\'');
		value.append(", buffer=");
		value.append(buffer);
		value.append(", featureIncludes=");
		value.append(featureIncludes);
		value.append(", layerFilters={");
		boolean first = true;
		for (String filterResultTags : layerFilters.keySet()) {
			if (!first) {
				value.append(", ");
			}
			first = false;
			value.append("{ResultTag='");
			value.append(filterResultTags);
			value.append("'");
			value.append(";filter=");
			value.append(layerFilters.get(filterResultTags));
			value.append("}");
		}
		value.append("}}");
		return value.toString();
	}

}
