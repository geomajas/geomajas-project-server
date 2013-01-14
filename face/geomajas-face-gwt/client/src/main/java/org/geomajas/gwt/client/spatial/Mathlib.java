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

package org.geomajas.gwt.client.spatial;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.MultiLineString;
import org.geomajas.gwt.client.spatial.geometry.MultiPolygon;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.spatial.geometry.Polygon;

/**
 * General math library.
 *
 * @author Pieter De Graef
 */
public final class Mathlib {

	public static final double ZERO = 0.00001;

	/**
	 * Calculates whether or not 2 line-segments intersect.
	 *
	 * @param c1
	 *            First coordinate of the first line-segment.
	 * @param c2
	 *            Second coordinate of the first line-segment.
	 * @param c3
	 *            First coordinate of the second line-segment.
	 * @param c4
	 *            Second coordinate of the second line-segment.
	 * @return Returns true or false.
	 */
	public static boolean lineIntersects(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4) {
		LineSegment ls1 = new LineSegment(c1, c2);
		LineSegment ls2 = new LineSegment(c3, c4);
		return ls1.intersects(ls2);
	}

	/**
	 * Calculates the intersection point of 2 lines.
	 *
	 * @param c1
	 *            First coordinate of the first line.
	 * @param c2
	 *            Second coordinate of the first line.
	 * @param c3
	 *            First coordinate of the second line.
	 * @param c4
	 *            Second coordinate of the second line.
	 * @return Returns a coordinate.
	 */
	public static Coordinate lineIntersection(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4) {
		LineSegment ls1 = new LineSegment(c1, c2);
		LineSegment ls2 = new LineSegment(c3, c4);
		return ls1.getIntersection(ls2);
	}

	/**
	 * Calculates the intersection point of 2 line segments.
	 *
	 * @param c1
	 *            Start point of the first line segment.
	 * @param c2
	 *            End point of the first line segment.
	 * @param c3
	 *            Start point of the second line segment.
	 * @param c4
	 *            End point of the second line segment.
	 * @return Returns a coordinate or null if not a single intersection point.
	 */
	public Coordinate lineSegmentIntersection(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4) {
		LineSegment ls1 = new LineSegment(c1, c2);
		LineSegment ls2 = new LineSegment(c3, c4);
		return ls1.getIntersectionSegments(ls2);
	}

	/**
	 * Distance between 2 points.
	 *
	 * @param c1
	 *            First coordinate
	 * @param c2
	 *            Second coordinate
	 */
	public static double distance(Coordinate c1, Coordinate c2) {
		double a = c1.getX() - c2.getX();
		double b = c1.getY() - c2.getY();
		return Math.sqrt(a * a + b * b);
	}

	/**
	 * Distance between a point and a line.
	 *
	 * @param c1
	 *            First coordinate of the line.
	 * @param c2
	 *            Second coordinate of the line.
	 * @param c3
	 *            Coordinate to calculate distance to line from.
	 */
	public static double distance(Coordinate c1, Coordinate c2, Coordinate c3) {
		LineSegment ls = new LineSegment(c1, c2);
		return ls.distance(c3);
	}

	/**
	 * Does a certain coordinate touch a given geometry?
	 *
	 * @param geometry
	 *            The geometry to check against.
	 * @param coordinate
	 *            The position to check.
	 * @return Returns true if the coordinate touches the geometry.
	 */
	public static boolean touches(Geometry geometry, Coordinate coordinate) {
		if (geometry instanceof MultiPolygon) {
			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				if (touches(geometry.getGeometryN(i), coordinate)) {
					return true;
				}
			}
			return false;
		} else if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			if (touchesLineString(polygon.getExteriorRing(), coordinate)) {
				return true;
			}
			for (int n = 0; n < polygon.getNumInteriorRing(); n++) {
				if (touchesLineString(polygon.getInteriorRingN(n), coordinate)) {
					return true;
				}
			}
			return false;
		} else if (geometry instanceof MultiLineString) {
			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				if (touchesLineString((LineString) geometry.getGeometryN(i), coordinate)) {
					return true;
				}
			}
			return false;
		} else if (geometry instanceof LineString) {
			return touchesLineString((LineString) geometry, coordinate);
		} else if (geometry instanceof Point) {
			Coordinate c = geometry.getCoordinates()[0];
			Vector2D v1 = new Vector2D(c.getX(), c.getY());
			Vector2D v2 = new Vector2D(coordinate.getX(), coordinate.getY());
			return (v1.distance(v2) < ZERO);
		}
		return false;
	}

	/**
	 * Is a certain coordinate within a given geometry?
	 *
	 * @param geometry
	 *            The geometry to check against. Only if it has {@link LinearRing}'s, can the coordinate be inside.
	 * @param coordinate
	 *            The position that is possibly within the geometry.
	 * @return Returns true if the coordinate is within the geometry.
	 */
	public static boolean isWithin(Geometry geometry, Coordinate coordinate) {
		if (geometry instanceof MultiPolygon) {
			for (int i = 0; i < geometry.getNumGeometries(); i++) {
				if (isWithin(geometry.getGeometryN(i), coordinate)) {
					return true;
				}
			}
		} else if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			if (isWithinRing(polygon.getExteriorRing(), coordinate)) {
				for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
					if (isWithinRing(polygon.getInteriorRingN(i), coordinate)) {
						return false;
					}
				}
				return true;
			}
		} else if (geometry instanceof LinearRing) {
			return isWithinRing((LinearRing) geometry, coordinate);
		}
		return false;
	}

	//-------------------------------------------------------------------------
	// Private methods:
	//-------------------------------------------------------------------------

	/**
	 * @private
	 */
	private static boolean touchesLineString(LineString lineString, Coordinate coordinate) {
		// First loop over the end-points. This will be the most common case, certainly if we take snapping into
		// account...
		for (int i = 0; i < lineString.getNumPoints(); i++) {
			if (lineString.getCoordinateN(i).equals(coordinate)) {
				return true;
			}
		}

		// Now loop over the edges:
		for (int i = 1; i < lineString.getNumPoints(); i++) {
			LineSegment edge = new LineSegment(lineString.getCoordinateN(i - 1), lineString.getCoordinateN(i));
			if (edge.distance(coordinate) < ZERO) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @private
	 */
	private static boolean isWithinRing(LinearRing linearRing, Coordinate coordinate) {
		int counter = 0;
		int num = linearRing.getNumPoints();
		Coordinate c1 = linearRing.getCoordinateN(0);
		for (int i = 1; i <= num; i++) {
			Coordinate c2 = linearRing.getCoordinateN(i % num); // this way, it should work to concatenate all ring
			// coordinate arrays of a polygon....(if they all have an equal number of coordinates)
			if (coordinate.getY() > Math.min(c1.getY(), c2.getY())) { // some checks to try and avoid the expensive
				// intersect calculation.
				if (coordinate.getY() <= Math.max(c1.getY(), c2.getY())) {
					if (coordinate.getX() <= Math.max(c1.getX(), c2.getX())) {
						if (c1.getY() != c2.getY()) {
							double xIntercept = (coordinate.getY() - c1.getY()) * (c2.getX() - c1.getX())
									/ (c2.getY() - c1.getY()) + c1.getX();
							if (c1.getX() == c2.getX() || coordinate.getX() <= xIntercept) {
								counter++;
							}
						}
					}
				}
			}
			c1 = c2;
		}
		if (counter % 2 == 0) {
			return false;
		}
		return true;
	}
}
