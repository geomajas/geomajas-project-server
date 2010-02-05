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

package org.geomajas.internal.service;

import java.util.Date;
import java.util.HashSet;

import org.geomajas.internal.layer.feature.FeatureModelRegistry;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.service.FilterService;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.Identifier;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * Utility class for creating filters with static functions. Make sure all arguments are correct before you use these
 * functions. This class uses the OpenGIS FilterFactory2 interface, which extends the OGC FilterFactory specification
 * with JTS geometry support.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public final class FilterServiceImpl implements FilterService {

	private static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

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
	public Filter createBetweenFilter(String name, String lower, String upper) {
		PropertyName attrib = FF.property(name);
		Expression d1 = FF.literal(lower);
		Expression d2 = FF.literal(upper);
		return FF.between(attrib, d1, d2);
	}

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
	public Filter createLikeFilter(String name, String pattern) {

		PropertyName attrib = FF.property(name);
		return FF.like(attrib, pattern, "*", "?", "\\");
	}

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
	public Filter createCompareFilter(String name, String comparator, String value) {
		PropertyName attrib = FF.property(name);
		Expression val = FF.literal(value);
		if ("<".equals(comparator)) {
			return FF.less(attrib, val);
		} else if (">".equals(comparator)) {
			return FF.greater(attrib, val);
		} else if (">=".equals(comparator)) {
			return FF.greaterOrEqual(attrib, val);
		} else if ("<=".equals(comparator)) {
			return FF.lessOrEqual(attrib, val);
		} else if ("=".equals(comparator)) {
			return FF.equals(attrib, val);
		} else if ("==".equals(comparator)) {
			return FF.equals(attrib, val);
		} else if ("<>".equals(comparator)) {
			return FF.notEqual(attrib, val, false);
		} else {
			throw new IllegalArgumentException("Could not interprete compare filter. Argument (" + value
					+ ") not known.");
		}
	}

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
	public Filter createCompareFilter(String name, String comparator, Date value) {
		PropertyName attrib = FF.property(name);
		Expression val = FF.literal(value);
		if ("<".equals(comparator)) {
			return FF.less(attrib, val);
		} else if (">".equals(comparator)) {
			return FF.greater(attrib, val);
		} else if (">=".equals(comparator)) {
			return FF.greaterOrEqual(attrib, val);
		} else if ("<=".equals(comparator)) {
			return FF.lessOrEqual(attrib, val);
		} else if ("=".equals(comparator)) {
			return FF.equals(attrib, val);
		} else if ("==".equals(comparator)) {
			return FF.equals(attrib, val);
		} else if ("<>".equals(comparator)) {
			return FF.notEqual(attrib, val, false);
		} else {
			throw new IllegalArgumentException("Could not interprete compare filter. Argument (" + value
					+ ") not known.");
		}
	}

	/**
	 * Creates a logic filter. This is a combination of filters.
	 * 
	 * @param filter1
	 *            first filter to combine
	 * @param logic
	 *            The logic operator. Can be 'AND', 'OR', 'NOT'.
	 * @param filter2
	 *            second filter to combine In the case of "NOT", this parameter is not used.
	 * @return filter
	 */
	public Filter createLogicFilter(Filter filter1, String logic, Filter filter2) {
		if ("and".equalsIgnoreCase(logic)) {
			return FF.and(filter1, filter2);
		} else if ("or".equalsIgnoreCase(logic)) {
			return FF.or(filter1, filter2);
		} else if ("not".equalsIgnoreCase(logic)) {
			return FF.not(filter1);
		} else {
			throw new IllegalArgumentException("Could not interpret logic filter. Argument (" + logic + ") not known.");
		}
	}

	/**
	 * Create a feature ID filter.
	 * 
	 * @param featureIDs
	 *            Array of identifier strings for the features that need looking up.
	 * @return filter
	 */
	public Filter createFidFilter(String[] featureIDs) {
		HashSet<Identifier> ids = new HashSet<Identifier>();
		for (String id : featureIDs) {
			ids.add(new FeatureIdImpl(id));
		}
		return FF.id(ids);
	}

	/**
	 * Creates a filter with all the geometries that contain the parameterized geometry (including the geometry itself).
	 * 
	 * @param geometry
	 *            the (parameterized) geometry
	 * @param geomName
	 *            the name of the geometry field ("the_geom")
	 * @return filter
	 */
	public Filter createContainsFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.contains(nameExpression, geomLiteral);
	}

	/**
	 * Creates a filter with all the geometries that lie completely within the parameterized geometry (including the
	 * geometry itself).
	 * 
	 * @param geometry
	 *            the (parameterized) geometry
	 * @param geomName
	 *            the name of the geometry field ("the_geom")
	 * @return filter
	 */
	public Filter createWithinFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.within(nameExpression, geomLiteral);
	}

	/**
	 * Creates a filter with all the geometries that have a non-empty intersection (overlap or touching) with the
	 * parameterized geometry (including the geometry itself).
	 * 
	 * @param geometry
	 *            the geometry
	 * @param geomName
	 *            the name of the geometry field ("the_geom")
	 * @return filter
	 */
	public Filter createIntersectsFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.intersects(nameExpression, geomLiteral);
	}

	/**
	 * Creates a filter with all the geometries that touch the parameterized geometry.
	 * 
	 * @param geometry
	 *            the geometry
	 * @param geomName
	 *            the name of the geometry field ("the_geom")
	 * @return filter
	 */
	public Filter createTouchesFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.touches(nameExpression, geomLiteral);
	}

	/**
	 * Create a filter the evaluates all geometries within a certain bounding box.
	 * 
	 * @param epsg
	 *            The bounding box' coordinate system reference.
	 * @param bbox
	 *            The bounding box itself.
	 * @param geomName
	 *            The name of the geometry field ("the_geom")
	 * @return filter
	 */
	public Filter createBboxFilter(String epsg, Envelope bbox, String geomName) {
		return FF.bbox(geomName, bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), epsg);
	}

	/**
	 * Creates a filter with all the geometries that overlap the parameterized geometry.
	 * 
	 * @param geometry
	 *            the geometry
	 * @param geomName
	 *            the name of the geometry field ("the_geom")
	 * @return filter
	 */
	public Filter createOverlapsFilter(Geometry geometry, String geomName) {
		Expression nameExpression = FF.property(geomName);
		Literal geomLiteral = FF.literal(geometry);
		return FF.overlaps(nameExpression, geomLiteral);
	}

	/**
	 * Creates a filter that evaluates everything.
	 * 
	 * @return filter
	 */
	public Filter createTrueFilter() {
		return Filter.INCLUDE;
	}

	/**
	 * Register a feature model.
	 * <p/>
	 * Only feature models which are registered can be used when filtering.
	 *
	 * @param featureModel feature model to register
	 */
	public void registerFeatureModel(FeatureModel featureModel) {
		FeatureModelRegistry.getRegistry().register(featureModel);
	}

	/**
	 * Combine two filters using the "AND" operator.
	 *
	 * @param left first filter to combine
	 * @param right second filter to combine
	 * @return new filter combining the parameters using "and"
	 */
	public Filter createAndFilter(Filter left, Filter right) {
		return FF.and(left, right);
	}
}
