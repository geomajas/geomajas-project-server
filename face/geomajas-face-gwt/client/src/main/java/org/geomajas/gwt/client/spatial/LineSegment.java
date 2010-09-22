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

package org.geomajas.gwt.client.spatial;

import org.geomajas.geometry.Coordinate;

/**
 * Representation of a LineSegment, mainly for mathematical purposes. Can calculate much needed line to line and line to
 * point calculations.
 *
 * @author Pieter De Graef
 */
public class LineSegment {

	/**
	 * The line-segment's first(begin) coordinate.
	 */
	private Coordinate c1;

	/**
	 * The line-segment's last(end) coordinate.
	 */
	private Coordinate c2;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public LineSegment() {
		c1 = null;
		c2 = null;
	}

	public LineSegment(Coordinate c1, Coordinate c2) {
		this.c1 = c1;
		this.c2 = c2;
	}

	// -------------------------------------------------------------------------
	// Class specific functions:
	// -------------------------------------------------------------------------

	/**
	 * Return the length of the linesegment.
	 */
	public double getLength() {
		double deltaX = this.c2.getX() - this.c1.getX();
		double deltaY = this.c2.getY() - this.c1.getY();
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	/**
	 * Return the middle point of this linesegment.
	 */
	public Coordinate getMiddlePoint() {
		double x = this.x1() + 0.5 * (this.x2() - this.x1());
		double y = this.y1() + 0.5 * (this.y2() - this.y1());
		return new Coordinate(x, y);
	}

	/**
	 * Return the distance from a point to this linesegment. If the point is not perpendicular to the linesegment, the
	 * closest endpoint will be returned.
	 *
	 * @param c
	 *            The {@link Coordinate} to check distance from.
	 */
	public double distance(Coordinate c) {
		Coordinate nearest = this.nearest(c);
		LineSegment ls = new LineSegment(c, nearest);
		return ls.getLength();
	}

	/**
	 * Does this linesegment intersect with another or not?
	 *
	 * @param lineSegment
	 *            The other linesegment.
	 * @return Returns true if they intersect in 1 point, false otherwise.
	 */
	public boolean intersects(LineSegment lineSegment) {
		double x1 = this.x1();
		double y1 = this.y1();
		double x2 = this.x2();
		double y2 = this.y2();
		double x3 = lineSegment.x1();
		double y3 = lineSegment.y1();
		double x4 = lineSegment.x2();
		double y4 = lineSegment.y2();

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
	public Coordinate getIntersection(LineSegment lineSegment) { // may not be on either one of the line segments.
		// http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		double x1 = this.x1();
		double y1 = this.y1();
		double x2 = this.x2();
		double y2 = this.y2();
		double x3 = lineSegment.x1();
		double y3 = lineSegment.y1();
		double x4 = lineSegment.x2();
		double y4 = lineSegment.y2();

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
	public Coordinate getIntersectionSegments(LineSegment lineSegment) {
		// http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		double x1 = this.x1();
		double y1 = this.y1();
		double x2 = this.x2();
		double y2 = this.y2();
		double x3 = lineSegment.x1();
		double y3 = lineSegment.y1();
		double x4 = lineSegment.x2();
		double y4 = lineSegment.y2();

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
		double len = this.getLength();

		double u = (c.getX() - this.c1.getX()) * (this.c2.getX() - this.c1.getX()) + (c.getY() - this.c1.getY())
				* (this.c2.getY() - this.c1.getY());
		u = u / (len * len);

		if (u < 0.00001 || u > 1) {
			// Shortest point not within LineSegment, so take closest end-point.
			LineSegment ls1 = new LineSegment(c, this.c1);
			LineSegment ls2 = new LineSegment(c, this.c2);
			double len1 = ls1.getLength();
			double len2 = ls2.getLength();
			if (len1 < len2) {
				return this.c1;
			}
			return this.c2;
		} else {
			// Intersecting point is on the line, use the formula: P = P1 + u (P2 - P1)
			double x1 = this.c1.getX() + u * (this.c2.getX() - this.c1.getX());
			double y1 = this.c1.getY() + u * (this.c2.getY() - this.c1.getY());
			return new Coordinate(x1, y1);
		}
	}

	//-------------------------------------------------------------------------
	// Getters
	//-------------------------------------------------------------------------

	public double x1() {
		return this.c1.getX();
	}

	public double y1() {
		return this.c1.getY();
	}

	public double x2() {
		return this.c2.getX();
	}

	public double y2() {
		return this.c2.getY();
	}

	public Coordinate getC1() {
		return this.c1;
	}

	public Coordinate getC2() {
		return this.c2;
	}
}
