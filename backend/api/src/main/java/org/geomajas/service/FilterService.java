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

package org.geomajas.service;

import java.util.Date;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.feature.FeatureModel;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.opengis.filter.FilterFactory;

/**
 * Filter creator service.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface FilterService {

	/**
	 * Generic attribute name which can be used to reference the id attribute (whatever the actual name is).
	 *
	 * @since 1.11.0
	 */
	String ATTRIBUTE_ID = LabelStyleInfo.ATTRIBUTE_NAME_ID;

	/**
	 * Create a filter which passes all features from the FeatureInfo within featureTypeInfo with attribute values from
	 * attribute 'name' between values 'lower and 'upper'.<BR>
	 * This type of filter is only allowed on numeric object types (short, integer, long, float, double)
	 *
	 * @param name
	 *            The name of the attribute from featureTypeInfo.
	 * @param lower
	 *            A literal. A string that can be converted to a double.
	 * @param upper
	 *            A literal. A string that can be converted to a double.
	 * @return filter
	 */
	Filter createBetweenFilter(String name, String lower, String upper);

	/**
	 * Filter based on the LIKE comparator as in sql. arg2 is a string to compare the values of arg1 with. Make sure
	 * arg1 refers to a String-attribute. The expression understands wildcards like *(= zero or more unknown characters)
	 * and ?(exactly 1 unknown character).
	 *
	 * @param name
	 *            The name of the attribute from featureTypeInfo.
	 * @param pattern
	 *            Expression to compare with.
	 * @return filter
	 */
	Filter createLikeFilter(String name, String pattern);

	/**
	 * Create a filter that passes all features where the values of attribute 'name' compared to literal 'value' using
	 * comparator 'comparator' returns true.
	 *
	 * @param name
	 *            The name of the attribute from featureTypeInfo. This type of filter is only allowed on numeric object
	 *            types (short, integer, long, float, double)
	 * @param comparator
	 *            Depending on the type of literal, a range of operators is supported.
	 *            <ul>
	 *            <li>For numeric object types (short, integer, long, float, double) all of the following operators are
	 *            supported: "<", "<=", ">", ">=", "==", "<>".</li>
	 *            <li>For boolean and string: "==" and "<>"</li>
	 *            <li>Dates have their own method...</li>
	 *            </ul>
	 * @param value
	 *            A literal. The actual value (as string).
	 * @return filter
	 */
	Filter createCompareFilter(String name, String comparator, String value);

	/**
	 * Create a filter that passes all features where the values of attribute 'name' compared to literal 'value' using
	 * comparator 'comparator' returns true. This function compares object of the type Date.
	 *
	 * @param name
	 *            The name of the attribute from featureTypeInfo. This type of filter is only allowed on dates.
	 * @param comparator
	 *            The sort of comparison, can be "<", "<=", ">", ">=", "==", "<>".
	 * @param value
	 *            A literal. A string that can be converted to a double.
	 * @return filter
	 */
	Filter createCompareFilter(String name, String comparator, Date value);

	/**
	 * Create a filter that checks that passes all features with the specified geometry type.
	 * 
	 * @param geomName
	 *            name of geometry attribute, empty string will choose default geometry
	 * @param geometryType
	 *            the geometry type ('Point', 'MultiPoint', 'LineString', 'MultiLineString', 'Polygon' or
	 *            'MultiPolygon', see {@link org.geotools.filter.function.FilterFunction#geometryType})
	 * @return filter
	 * @since 1.9.0
	 */
	Filter createGeometryTypeFilter(String geomName, String geometryType);
	
	/**
	 * Create a logic filter. This is a combination of filters.
	 *
	 * @param filter1 first filter to combine
	 * @param logic
	 *            The logic operator. Can be 'AND', 'OR', 'NOT'.
	 * @param filter2 second filter to combine
	 *            In the case of "NOT", this parameter is not used.
	 * @return filter
	 */
	Filter createLogicFilter(Filter filter1, String logic, Filter filter2);

	/**
	 * Create a feature ID filter.
	 *
	 * @param featureIDs
	 *            Array of identifier strings for the features that need looking up.
	 * @return filter
	 */
	Filter createFidFilter(String[] featureIDs);

	/**
	 * Create a filter with all the geometries that lie completely within the given geometry (including the
	 * geometry itself).
	 *
	 * @param geometry
	 *            the geometry
	 * @param geomName
	 *            the name of the geometry field
	 * @return filter
	 */
	Filter createWithinFilter(Geometry geometry, String geomName);

	/**
	 * Create a filter with all the geometries that contain the given geometry (including the geometry itself).
	 *
	 * @param geometry
	 *            the geometry
	 * @param geomName
	 *            the name of the geometry field
	 * @return filter
	 */
	Filter createContainsFilter(Geometry geometry, String geomName);

	/**
	 * Create a filter with all the geometries that have a non-empty intersection (overlap or touching) with
	 * given geometry (including the geometry itself).
	 *
	 * @param geometry
	 *            the geometry
	 * @param geomName
	 *            the name of the geometry field
	 * @return filter
	 */
	Filter createIntersectsFilter(Geometry geometry, String geomName);

	/**
	 * Creates a filter with all the geometries that touch the given geometry.
	 *
	 * @param geometry
	 *            the geometry
	 * @param geomName
	 *            the name of the geometry field
	 * @return filter
	 */
	Filter createTouchesFilter(Geometry geometry, String geomName);

	/**
	 * Create a filter the evaluates all geometries within a certain bounding box.
	 *
	 * @param epsg
	 *            The bounding box' coordinate system reference.
	 * @param bbox
	 *            The bounding box itself.
	 * @param geomName
	 *            The name of the geometry field
	 * @return filter
	 */
	Filter createBboxFilter(String epsg, Envelope bbox, String geomName);
	
	/**
	 * Create a filter the evaluates all geometries within a certain bounding box.
	 *
	 * @param crs
	 *            The bounding box' coordinate reference system.
	 * @param bbox
	 *            The bounding box itself.
	 * @param geomName
	 *            The name of the geometry field
	 * @return filter
	 * @since 1.9.0
	 */
	Filter createBboxFilter(Crs crs, Envelope bbox, String geomName);
	

	/**
	 * Create a filter with all the geometries that overlap the given geometry.
	 *
	 * @param geometry
	 *            the geometry
	 * @param geomName
	 *            the name of the geometry field
	 * @return filter
	 */
	Filter createOverlapsFilter(Geometry geometry, String geomName);

	/**
	 * Creates a filter that allows everything.
	 *
	 * @return filter
	 */
	Filter createTrueFilter();

	/**
	 * Creates a filter that allows nothing.
	 *
	 * @return filter
	 */
	Filter createFalseFilter();

	/**
	 * Register a feature model.
	 * <p/>
	 * Only feature models which are registered can be used when filtering.
	 *
	 * @param featureModel feature model to register
	 */
	void registerFeatureModel(FeatureModel featureModel);

	/**
	 * Combine two filters using the "AND" operator.
	 *
	 * @param left first filter to combine
	 * @param right second filter to combine
	 * @return new filter combining the parameters using "and"
	 */
	Filter createAndFilter(Filter left, Filter right);

	/**
	 * Combine two filters using the "OR" operator.
	 *
	 * @param left first filter to combine
	 * @param right second filter to combine
	 * @return new filter combining the parameters using "or"
	 * @since 1.10.0
	 */
	Filter createOrFilter(Filter left, Filter right);

	/**
	 * Parse a string to build a filter object.
	 *
	 * @param filter filter as string
	 * @return actual filter
	 * @throws GeomajasException could not parse string to filter
	 */
	Filter parseFilter(String filter) throws GeomajasException;

	/**
	 * Get filter factory which is used by the implementation.
	 *
	 * @return filter factory
	 * @since 1.11.0
	 */
	FilterFactory getFilterFactory();

}
