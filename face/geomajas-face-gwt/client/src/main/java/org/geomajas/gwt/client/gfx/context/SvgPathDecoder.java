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

package org.geomajas.gwt.client.gfx.context;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * <p>
 * Decoder for geometry objects, that transforms them into strings that can be used as the "d" attribute of SVG path
 * elements.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class SvgPathDecoder {

	private SvgPathDecoder() {
	}

	/**
	 * This function decodes a Geometry (line or polygon) to a path's d-attribute.
	 * 
	 * @param geometry
	 *            The geometry to be decoded into a single path D attribute.
	 * @return The D attribute value as a string.
	 */
	public static String decode(Geometry geometry) {
		return decode(geometry, 1.0f);
	}

	/**
	 * This function decodes a Geometry (line or polygon) to a path's d-attribute.
	 * 
	 * @param geometry
	 *            The geometry to be decoded into a single path D attribute.
	 * @param scale
	 *            A scale value to apply on all the coordinates of the geometry before decoding.
	 * @return The D attribute value as a string.
	 */
	public static String decode(Geometry geometry, float scale) {
		if (geometry == null) {
			return "";
		}
		if (geometry instanceof LinearRing) {
			return decodeLinearRing((LinearRing) geometry);
		} else if (geometry instanceof LineString) {
			return decodeLineString((LineString) geometry);
		} else if (geometry instanceof Polygon) {
			return decodePolygon((Polygon) geometry);
		} else if (geometry instanceof MultiPolygon) {
			return decodeMultiPolygon((MultiPolygon) geometry);
		} else if (geometry instanceof MultiLineString) {
			return decodeMultiLineString((MultiLineString) geometry);
		}
		return "";
	}

	// -------------------------------------------------------------------------
	// Private functions:
	// -------------------------------------------------------------------------

	/**
	 * Decode LineString.
	 * 
	 * @param lineString
	 * @return
	 */
	private static String decodeLineString(LineString lineString) {
		if (lineString == null || lineString.isEmpty()) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		Coordinate[] coordinates = lineString.getCoordinates();
		for (int i = 0; i < coordinates.length; i++) {
			buffer.append(getX(coordinates[i]));
			buffer.append(" ");
			buffer.append(getY(coordinates[i]));
			if (i < (coordinates.length - 1)) {
				buffer.append(", ");
			}
		}
		return "M" + buffer.toString();
	}

	private static String decodeLinearRing(LinearRing linearRing) {
		if (linearRing == null || linearRing.isEmpty()) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		Coordinate[] coordinates = linearRing.getCoordinates();
		for (int i = 0; i < coordinates.length - 1; i++) {
			buffer.append(getX(coordinates[i]));
			buffer.append(" ");
			buffer.append(getY(coordinates[i]));
			if (i < (coordinates.length - 2)) {
				buffer.append(", ");
			}
		}
		return "M" + buffer.toString() + " Z";
	}

	private static String decodeMultiLineString(MultiLineString multiLine) {
		int n = multiLine.getNumGeometries();
		StringBuilder pstr = new StringBuilder();
		for (int i = 0; i < n; i++) {
			pstr.append(decodeLineString((LineString) multiLine.getGeometryN(i)));
		}
		return pstr.toString();
	}

	private static String decodePolygon(Polygon polygon) {
		if (polygon == null || polygon.isEmpty()) {
			return "";
		}
		StringBuilder pstr = new StringBuilder();
		pstr.append(decodeLinearRing(polygon.getExteriorRing()));
		for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
			pstr.append(decodeLinearRing(polygon.getInteriorRingN(i)));
		}
		return pstr.toString();
	}

	private static String decodeMultiPolygon(MultiPolygon multipoly) {
		StringBuilder pstr = new StringBuilder();
		for (int i = 0; i < multipoly.getNumGeometries(); i++) {
			pstr.append(decodePolygon((Polygon) multipoly.getGeometryN(i)));
		}
		return pstr.toString();
	}

	/** Values in SVG may not go over 1000000. */
	private static double getX(Coordinate c) {
		double value = c.getX();
		if (value > 1000000) {
			value = 1000000;
		} else if (value < -1000000) {
			value = -1000000;
		}
		return value;
	}

	/** Values in SVG may not go over 1000000. */
	private static double getY(Coordinate c) {
		double value = c.getY();
		if (value > 1000000) {
			value = 1000000;
		} else if (value < -1000000) {
			value = -1000000;
		}
		return value;
	}
}