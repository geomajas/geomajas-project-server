/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.geometry.service;

import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;

/**
 * General service for calculating mathematical properties of geometries.
 * 
 * @author Pieter De Graef
 */
public final class MathService {

	private static final double PARAM_DEFAULT_DELTA = 0.0001;

	private MathService() {
		// Private no-argument constructor for final classes.
	}

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
	public static boolean intersectsLineSegment(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4) {
		double x1 = c1.getX();
		double y1 = c1.getY();
		double x2 = c2.getX();
		double y2 = c2.getY();
		double x3 = c3.getX();
		double y3 = c3.getY();
		double x4 = c4.getX();
		double y4 = c4.getY();

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (denom == 0) {
			return false;
		}
		double u1 = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
		double u2 = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
		return (u1 > 0 && u1 < 1 && u2 > 0 && u2 < 1);
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
		// http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		double x1 = c1.getX();
		double y1 = c1.getY();
		double x2 = c2.getX();
		double y2 = c2.getY();
		double x3 = c3.getX();
		double y3 = c3.getY();
		double x4 = c4.getX();
		double y4 = c4.getY();

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		double u1 = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;

		double x = x1 + u1 * (x2 - x1);
		double y = y1 + u1 * (y2 - y1);
		return new Coordinate(x, y);
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
	public static Coordinate lineSegmentIntersection(Coordinate c1, Coordinate c2, Coordinate c3, Coordinate c4) {
		// http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		double x1 = c1.getX();
		double y1 = c1.getY();
		double x2 = c2.getX();
		double y2 = c2.getY();
		double x3 = c3.getX();
		double y3 = c3.getY();
		double x4 = c4.getX();
		double y4 = c4.getY();

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (denom == 0) {
			return null;
		}

		double u1 = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
		if (u1 <= 0 || u1 >= 1) {
			return null;
		}
		double u2 = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
		if (u2 <= 0 || u2 >= 1) {
			return null;
		}
		double x = x1 + u1 * (x2 - x1);
		double y = y1 + u1 * (y2 - y1);
		return new Coordinate(x, y);
	}

	/**
	 * Distance between 2 points.
	 * 
	 * @param c1
	 *            First coordinate
	 * @param c2
	 *            Second coordinate
	 * @return distance between given points
	 */
	public static double distance(Coordinate c1, Coordinate c2) {
		double a = c1.getX() - c2.getX();
		double b = c1.getY() - c2.getY();
		return Math.sqrt(a * a + b * b);
	}

	/**
	 * Distance between a point and a line segment. This method looks at the line segment c1-c2, it does not regard it
	 * as a line. This means that the distance to c is calculated to a point between c1 and c2.
	 * 
	 * @param c1
	 *            First coordinate of the line segment.
	 * @param c2
	 *            Second coordinate of the line segment.
	 * @param c
	 *            Coordinate to calculate distance to line from.
	 * @return distance between point and line segment
	 */
	public static double distance(Coordinate c1, Coordinate c2, Coordinate c) {
		return distance(nearest(c1, c2, c), c);
	}

	/**
	 * Calculate which point on a line segment is nearest to the given coordinate. Will be perpendicular or one of the
	 * end-points.
	 * 
	 * @param c1
	 *            First coordinate of the line segment.
	 * @param c2
	 *            Second coordinate of the line segment.
	 * @param c
	 *            The coordinate to search the nearest point for.
	 * @return The point on the line segment nearest to the given coordinate.
	 */
	public static Coordinate nearest(Coordinate c1, Coordinate c2, Coordinate c) {
		double len = distance(c1, c2);

		double u = (c.getX() - c1.getX()) * (c2.getX() - c1.getX()) + (c.getY() - c1.getY()) * (c2.getY() - c1.getY());
		u = u / (len * len);

		if (u < 0.00001 || u > 1) {
			// Shortest point not within LineSegment, so take closest end-point.
			double len1 = distance(c, c1);
			double len2 = distance(c, c2);
			if (len1 < len2) {
				return c1;
			}
			return c2;
		} else {
			// Intersecting point is on the line, use the formula: P = P1 + u (P2 - P1)
			double x1 = c1.getX() + u * (c2.getX() - c1.getX());
			double y1 = c1.getY() + u * (c2.getY() - c1.getY());
			return new Coordinate(x1, y1);
		}
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
		if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				if (touches(geometry.getGeometries()[i], coordinate)) {
					return true;
				}
			}
			return false;
		} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				if (touchesLineString(geometry.getGeometries()[i], coordinate)) {
					return true;
				}
			}
			return false;
		} else if (Geometry.MULTI_LINE_STRING.equals(geometry.getGeometryType())) {
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				if (touchesLineString(geometry.getGeometries()[i], coordinate)) {
					return true;
				}
			}
			return false;
		} else if (Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
			return touchesLineString(geometry, coordinate);
		} else if (Geometry.LINE_STRING.equals(geometry.getGeometryType())) {
			return touchesLineString(geometry, coordinate);
		} else if (Geometry.POINT.equals(geometry.getGeometryType())) {
			return distance(geometry.getCoordinates()[0], coordinate) < PARAM_DEFAULT_DELTA;
		}
		return false;
	}

	/**
	 * Is a certain coordinate within a given geometry?
	 * 
	 * @param geometry
	 *            The geometry to check against. Only geometries that contain closed rings can return true (i.e.
	 *            LinearRing, Polygon, MultiPolygon).
	 * @param coordinate
	 *            The position that is possibly within the geometry.
	 * @return Returns true if the coordinate is within the geometry.
	 */
	public static boolean isWithin(Geometry geometry, Coordinate coordinate) {
		if (Geometry.MULTI_POLYGON.equals(geometry.getGeometryType())) {
			for (int i = 0; i < geometry.getGeometries().length; i++) {
				if (isWithin(geometry.getGeometries()[i], coordinate)) {
					return true;
				}
			}
		} else if (Geometry.POLYGON.equals(geometry.getGeometryType())) {
			if (isWithinRing(geometry.getGeometries()[0], coordinate)) {
				if (geometry.getGeometries().length > 1) {
					for (int i = 1; i < geometry.getGeometries().length; i++) {
						if (isWithinRing(geometry.getGeometries()[i], coordinate)) {
							return false;
						}
					}
				}
				return true;
			}
		} else if (Geometry.LINEAR_RING.equals(geometry.getGeometryType())) {
			return isWithinRing(geometry, coordinate);
		}
		return false;
	}

	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------

	private static boolean touchesLineString(Geometry lineString, Coordinate coordinate) {
		// Basic argument checking:
		if (lineString.getCoordinates() == null || lineString.getCoordinates().length == 0) {
			return false;
		}

		// First loop over the vertices (end-points). This will be the most common case:
		for (int i = 0; i < lineString.getCoordinates().length; i++) {
			if (lineString.getCoordinates()[i].equals(coordinate)) {
				return true;
			}
		}

		// Now loop over the edges:
		for (int i = 1; i < lineString.getCoordinates().length; i++) {
			double distance = distance(lineString.getCoordinates()[i - 1], lineString.getCoordinates()[i], coordinate);
			if (distance < PARAM_DEFAULT_DELTA) {
				return true;
			}
		}

		// No touchy!
		return false;
	}

	private static boolean isWithinRing(Geometry linearRing, Coordinate coordinate) {
		// Basic argument checking:
		if (linearRing.getCoordinates() == null || linearRing.getCoordinates().length < 4) {
			return false;
		}

		int counter = 0;
		int num = linearRing.getCoordinates().length;
		Coordinate c1 = linearRing.getCoordinates()[0];
		for (int i = 1; i <= num; i++) {
			Coordinate c2 = linearRing.getCoordinates()[i % num]; // this way, it should work to concatenate all ring
			// coordinate arrays of a polygon....(if they all have an equal number of coordinates)

			// some checks to try and avoid the expensive intersect calculation.
			if (coordinate.getY() > Math.min(c1.getY(), c2.getY()) &&
					coordinate.getY() <= Math.max(c1.getY(), c2.getY()) &&
					coordinate.getX() <= Math.max(c1.getX(), c2.getX()) &&
					c1.getY() != c2.getY()) {
				double xIntercept = (coordinate.getY() - c1.getY()) * (c2.getX() - c1.getX())
						/ (c2.getY() - c1.getY()) + c1.getX();
				if (c1.getX() == c2.getX() || coordinate.getX() <= xIntercept) {
					counter++;
				}
			}
			c1 = c2;
		}
		return counter % 2 != 0;
	}
}