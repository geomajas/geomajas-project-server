dojo.provide("geomajas.spatial.LineSegment");
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
dojo.require("geomajas.spatial.Coordinate");

dojo.declare("LineSegment", null, {

	/**
	 * @fileoverview Implementation of a linesegment.
	 * @class Representation of a linesegment, mainly for mathematical
	 * purposes. Can calculate much needed line to line and line to point
	 * calculations.
	 * @author Pieter De Graef
	 * @constructor
	 * @param c1 The first coordinate.
	 * @param c2 The second coordinate.
	 */
	constructor : function (c1, c2) {
		this.c1 = c1;
		this.c2 = c2;
	},

	/**
	 * Returns the length of the linesegment.
	 */
	getLength : function () {
		var deltaX = this.c2.getX() - this.c1.getX();
		var deltaY = this.c2.getY() - this.c1.getY();
		return Math.sqrt(deltaX*deltaX + deltaY*deltaY);
	},

	/**
	 * Returns the middle point of this linesegment.
	 */
	getMiddlePoint : function () {
		var x = this.x1() + 0.5*(this.x2()-this.x1());
		var y = this.y1() + 0.5*(this.y2()-this.y1());
		return new Coordinate(x, y);		
	},

	/**
	 * Returns the distance from a point to this linesegment. If the point is
	 * not perpendicular to the linesegment, the closest endpoint will be
	 * returned.
	 * @param c The {@link Coordinate} to check distance from.
	 */
	distance : function (c) {
/*		var len = this.getLength();

		var u = (c.getX() - this.c1.getX())*(this.c2.getX() - this.c1.getX()) + (c.getY() - this.c1.getY())*(this.c2.getY() - this.c1.getY());
		u = u / (len*len);

		if (u < 0.00001 || u > 1) {
			// Shortest point not within linesegment, so take closest endpoint.
			var ls1 = new LineSegment (c, this.c1); 
			var ls2 = new LineSegment (c, this.c2);
			return Math.min(ls1.getLength(),ls2.getLength());
		} else {
			// Intersecting point is on the line, use the formula: P = P1 + u (P2 - P1)
			var x1 = this.c1.getX() + u * (this.c2.getX() - this.c1.getX());
			var y1 = this.c1.getY() + u * (this.c2.getY() - this.c1.getY());
			var ls = new LineSegment (c, new Coordinate(x1, y1));
			return ls.getLength();
		}*/
		var nearest = this.nearest(c);
		var ls = new LineSegment (c, nearest);
		return ls.getLength();
	},

	/**
	 * Does this linesegment intersect with another or not?
	 * @param lineSegment The other linesegment.
	 * @return Returns true if they intersect in 1 point, false otherwise.
	 */
	intersects : function (lineSegment) {
		var x1 = this.x1(); var y1 = this.y1();
		var x2 = this.x2(); var y2 = this.y2();
		var x3 = lineSegment.x1(); var y3 = lineSegment.y1();
		var x4 = lineSegment.x2(); var y4 = lineSegment.y2();


		var denom = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
		if (denom == 0) {
			return false;
		}
		var u1 = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / denom;
		var u2 = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / denom;
		return (u1 > 0 && u1 < 1 && u2 > 0 && u2 < 1);
	},

	/**
	 * Returns the intersection point of 2 lines. Yes you are reading this
	 * correctly! For this function the linesegments are treated as lines. This
	 * means that the intersection point does not necessarily lie on the
	 * linesegment (in that case, the {@link #intersects} function would return
	 * false).
	 * @param lineSegment The other linesegment.
	 * @return A {@link Coordinate} representing the intersection point.
	 */
	getIntersection : function (lineSegment) { // may not be on either one of the line segments.
		//http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		var x1 = this.x1(); var y1 = this.y1();
		var x2 = this.x2(); var y2 = this.y2();
		var x3 = lineSegment.x1(); var y3 = lineSegment.y1();
		var x4 = lineSegment.x2(); var y4 = lineSegment.y2();

		var denom = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
		var u1 = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / denom;

		var x = x1 + u1*(x2-x1);
		var y = y1 + u1*(y2-y1);
		return new Coordinate(x, y);		
	},
	/**
	 * Returns the intersection point of 2 line segments if they intersect in 1 point.
	 * @param lineSegment The other linesegment.
	 * @return A {@link Coordinate} representing the intersection point (so on both line segments),
	 * 		null if segments are not intersecting or corresponding lines are coincident.
	 */
	getIntersectionSegments : function (lineSegment) {
		//http://local.wasp.uwa.edu.au/~pbourke/geometry/lineline2d/
		var x1 = this.x1(); var y1 = this.y1();
		var x2 = this.x2(); var y2 = this.y2();
		var x3 = lineSegment.x1(); var y3 = lineSegment.y1();
		var x4 = lineSegment.x2(); var y4 = lineSegment.y2();

		var denom = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
		if (denom == 0) {
			return null;
		}

		var u1 = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / denom;
		if (u1 <= 0 || u1 >= 1) {
			return null;
		}
		var u2 = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / denom;
		if (u2 <= 0 || u2 >= 1) {
			return null;
		}
		var x = x1 + u1*(x2-x1);
		var y = y1 + u1*(y2-y1);
		return new Coordinate(x, y);
	},
	/**
	 * Calculate which point on the linesegment is nearest to the given
	 * coordinate. Will be perpendicular or one of the endpoints.
	 * @param c The coordinate to check.
	 * @return The point on the linesegment nearest to the given coordinate.
	 */
	nearest : function (c) {
		var len = this.getLength();

		var u = (c.getX() - this.c1.getX())*(this.c2.getX() - this.c1.getX()) + (c.getY() - this.c1.getY())*(this.c2.getY() - this.c1.getY());
		u = u / (len*len);

		if (u < 0.00001 || u > 1) {
			// Shortest point not within linesegment, so take closest endpoint.
			var ls1 = new LineSegment (c, this.c1); 
			var ls2 = new LineSegment (c, this.c2);
			var len1 = ls1.getLength();
			var len2 = ls2.getLength();
			if (len1 < len2) {
				return this.c1;
			}
			return this.c2;
		} else {
			// Intersecting point is on the line, use the formula: P = P1 + u (P2 - P1)
			var x1 = this.c1.getX() + u * (this.c2.getX() - this.c1.getX());
			var y1 = this.c1.getY() + u * (this.c2.getY() - this.c1.getY());
			return new Coordinate(x1, y1);
		}
	},

	// Getters :

	x1 : function () {
		return this.c1.getX();
	},

	y1 : function () {
		return this.c1.getY();
	},

	x2 : function () {
		return this.c2.getX();
	},

	y2 : function () {
		return this.c2.getY();
	},

	getC1 : function () {
		return this.c1;
	},

	getC2 : function () {
		return this.c2;
	}
});