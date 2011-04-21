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

package org.geomajas.puregwt.client.spatial;

import org.geomajas.geometry.Coordinate;

/**
 * Representation of a LineSegment, mainly for mathematical purposes. Can calculate much needed line to line and line to
 * point calculations.
 * 
 * @author Pieter De Graef
 */
public class LineSegment {

	/** The line-segment's first(begin) coordinate. */
	private Coordinate c1;

	/** The line-segment's last(end) coordinate. */
	private Coordinate c2;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public LineSegment(Coordinate c1, Coordinate c2) {
		if (c1 == null || c2 == null) {
			throw new NullPointerException("Null value passed to LineSegment constructor.");
		}
		this.c1 = c1;
		this.c2 = c2;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Return the length of the line segment.
	 */
	public double getLength() {
		double deltaX = c2.getX() - c1.getX();
		double deltaY = c2.getY() - c1.getY();
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	/**
	 * Return the middle point of this line segment.
	 */
	public Coordinate getMiddlePoint() {
		double x = c1.getX() + 0.5 * (c2.getX() - c1.getX());
		double y = c1.getY() + 0.5 * (c2.getY() - c1.getY());
		return new Coordinate(x, y);
	}

	/**
	 * Return the distance from a point to this line segment. If the point is not perpendicular to the line segment, the
	 * closest end-point will be returned.
	 * 
	 * @param c
	 *            The {@link Coordinate} to check distance from.
	 */
	public double distance(Coordinate c) {
		Coordinate nearest = nearest(c);
		LineSegment ls = new LineSegment(c, nearest);
		return ls.getLength();
	}

	/**
	 * Does this line segment intersect with another or not?
	 * 
	 * @param lineSegment
	 *            The other line segment.
	 * @return Returns true if they intersect in 1 point, false otherwise.
	 */
	public boolean intersects(LineSegment lineSegment) {
		double x1 = c1.getX();
		double y1 = c1.getY();
		double x2 = c2.getX();
		double y2 = c2.getY();
		double x3 = lineSegment.getCoordinate1().getX();
		double y3 = lineSegment.getCoordinate1().getY();
		double x4 = lineSegment.getCoordinate2().getX();
		double y4 = lineSegment.getCoordinate2().getY();

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (denom == 0) {
			return false;
		}
		double u1 = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
		double u2 = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
		return (u1 > 0 && u1 < 1 && u2 > 0 && u2 < 1);
	}

	/**
	 * Return the intersection point of 2 lines. Yes you are reading this correctly! For this function the LineSegments
	 * are treated as lines. This means that the intersection point does not necessarily lie on the LineSegment (in that
	 * case, the {@link #intersects} function would return false).
	 * 
	 * @param lineSegment
	 *            The other LineSegment.
	 * @return A {@link Coordinate} representing the intersection point.
	 */
	public Coordinate getLineIntersection(LineSegment lineSegment) { // may not be on either one of the line segments.
		// http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		double x1 = c1.getX();
		double y1 = c1.getY();
		double x2 = c2.getX();
		double y2 = c2.getY();
		double x3 = lineSegment.getCoordinate1().getX();
		double y3 = lineSegment.getCoordinate1().getY();
		double x4 = lineSegment.getCoordinate2().getX();
		double y4 = lineSegment.getCoordinate2().getY();

		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		double u1 = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;

		double x = x1 + u1 * (x2 - x1);
		double y = y1 + u1 * (y2 - y1);
		return new Coordinate(x, y);
	}

	/**
	 * Return the intersection point of 2 line segments if they intersect in 1 point.
	 * 
	 * @param lineSegment
	 *            The other LineSegment.
	 * @return A {@link Coordinate} representing the intersection point (so on both line segments), null if segments are
	 *         not intersecting or corresponding lines are coinciding.
	 */
	public Coordinate getIntersection(LineSegment lineSegment) {
		// http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		double x1 = c1.getX();
		double y1 = c1.getY();
		double x2 = c2.getX();
		double y2 = c2.getY();
		double x3 = lineSegment.getCoordinate1().getX();
		double y3 = lineSegment.getCoordinate1().getY();
		double x4 = lineSegment.getCoordinate2().getX();
		double y4 = lineSegment.getCoordinate2().getY();

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
	 * Calculate which point on the LineSegment is nearest to the given coordinate. Will be perpendicular or one of the
	 * end-points.
	 * 
	 * @param c
	 *            The coordinate to check.
	 * @return The point on the LineSegment nearest to the given coordinate.
	 */
	public Coordinate nearest(Coordinate c) {
		double len = getLength();

		double u = (c.getX() - c1.getX()) * (c2.getX() - c1.getX()) + (c.getY() - c1.getY()) * (c2.getY() - c1.getY());
		u = u / (len * len);

		if (u < 0.00001 || u > 1) {
			// Shortest point not within LineSegment, so take closest end-point.
			LineSegment ls1 = new LineSegment(c, c1);
			LineSegment ls2 = new LineSegment(c, c2);
			double len1 = ls1.getLength();
			double len2 = ls2.getLength();
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

	// -------------------------------------------------------------------------
	// Getters
	// -------------------------------------------------------------------------

	public Coordinate getCoordinate1() {
		return c1;
	}

	public Coordinate getCoordinate2() {
		return c2;
	}
}