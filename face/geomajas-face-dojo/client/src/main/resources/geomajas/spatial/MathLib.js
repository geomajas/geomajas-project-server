dojo.provide("geomajas.spatial.MathLib")
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
dojo.require("geomajas.spatial.LineSegment");

dojo.declare("MathLib", null, {

	constructor : function () {
		this.zero = 0.00001;
	},

	/**
	 * Calculates whether or not 2 line-segments intersect.
	 * @param c1 First coordinate of the first line-segment.
	 * @param c2 Second coordinate of the first line-segment.
	 * @param c3 First coordinate of the second line-segment.
	 * @param c4 Second coordinate of the second line-segment.
	 * @return Returns true or false.
	 */
	lineIntersects : function (c1, c2, c3, c4) {
		var ls1 = new LineSegment (c1, c2);
		var ls2 = new LineSegment (c3, c4);
		return ls1.intersects(ls2);
	},

	/**
	 * Calculates the intersection point of 2 lines.
	 * @param c1 First coordinate of the first line.
	 * @param c2 Second coordinate of the first line.
	 * @param c3 First coordinate of the second line.
	 * @param c4 Second coordinate of the second line.
	 * @return Returns a coordinate.
	 */
	lineIntersection : function (c1, c2, c3, c4) {
		var ls1 = new LineSegment (c1, c2);
		var ls2 = new LineSegment (c3, c4);
		return ls1.getIntersection(ls2);
	},

	/**
	 * Calculates the intersection point of 2 line segments.
	 * @param c1 Start point of the first line segment.
	 * @param c2 End point of the first line segment.
	 * @param c3 Start point of the second line segment.
	 * @param c4 End point of the second line segment.
	 * @return Returns a coordinate or null if not a single intersection point.
	 */
	lineSegmentIntersection : function (c1, c2, c3, c4) {
		var ls1 = new LineSegment (c1, c2);
		var ls2 = new LineSegment (c3, c4);
		return ls1.getIntersectionSegments(ls2);
	},

	/**
	 * Distance between a point and a line.
	 * @param c1 First coordinate of the line.
	 * @param c2 Second coordinate of the line.
	 * @param c3 Coordinate to calculate distance to line from.
	 */
	distance : function (c1, c2, c3) {
		var ls = new LineSegment (c1, c2);
		return ls.distance(c3);
	},

	/**
	 * Does a certain coordinate touch a given geometry?
	 * @param geometry The geometry to check against.
	 * @param coordinate The position to check.
	 * @return Returns true if the coordinate touches the geometry.
	 */
	touches : function (geometry, coordinate) {
		if (geometry instanceof MultiPolygon) {
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				if (this.touches(geometry.getGeometryN(i), coordinate)) {
					return true;
				}
			}
			return false;
		} else if (geometry instanceof Polygon) {
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				if (this._touchesLineString(geometry.getGeometryN(i), coordinate)) {
					return true;
				}
			}
			return false;
		} else if (geometry instanceof MultiLineString) {
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				if (this._touchesLineString(geometry.getGeometryN(i), coordinate)) {
					return true;
				}
			}
			return false;
		} else if (geometry instanceof LineString) {
			return this._touchesLineString(geometry, coordinate);
		} else if (geometry instanceof Point) {
			var c = geometry.getCoordinateN(0);
			var v1 = new Vector2D(c.getX(), c.getY());
			var v2 = new Vector2D(coordinate.getX(), coordinate.getY());
			return (v1.distance(v2) < this.zero);
		}
	},

	/**
	 * Is a certain coordinate within a given geometry?
	 * @param geometry The geometry to check against. Only if it has
	 *                 {@link LinearRing}'s, can the coordinate be inside.
	 * @param coordinate The position that is possibly within the geometry.
	 * @return Returns true if the coordinate is within the geometry.
	 */
	isWithin : function (geometry, coordinate) {
		if (geometry instanceof MultiPolygon) {
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				if (this.isWithin(geometry.getGeometryN(i), coordinate)) {
					return true;
				}
			}
		} else if (geometry instanceof Polygon) {
			if (this._isWithinRing(geometry.getExteriorRing(), coordinate)) {
				for (var i=0; i<geometry.getNumInteriorRing(); i++) {
					if (this._isWithinRing(geometry.getInteriorRingN(i), coordinate)) {
						return false;
					}
				}
				return true;
			}
		} else if (geometry instanceof LinearRing) {
			return this._isWithinRing(geometry, coordinate);
		}
		return false;
	},

	/**
	 * @private
	 */
	_touchesLineString : function (lineString, coordinate) {
		// First loop over the endpoints. This will be the most common case, certainly if we take snapping into account...
		for (var i=0; i<lineString.getNumPoints(); i++) {
			if (lineString.getCoordinateN(i).equals(coordinate)) {
				return true;
			}
		}
		
		// Now loop over the edges:
		for (var i=1; i<lineString.getNumPoints(); i++) {
			var edge = new LineSegment(lineString.getCoordinateN(i-1), lineString.getCoordinateN(i));
			if (edge.distance(coordinate) < this.zero) {
				return true;
			}
		}

		return false;
	},	

	/**
	 * @private
	 */
	_isWithinRing : function (linearRing, coordinate) {
		var counter = 0;
		var num = linearRing.getNumPoints();
		var c1 = linearRing.getCoordinateN(0);
		for (var i=1; i<=num; i++) {
			var c2 = linearRing.getCoordinateN(i % num); // this way, it should work to concatenate all ring coordinate arrays of a poly....(if they all have an equal number of coords)
			if (coordinate.getY() > Math.min(c1.getY(), c2.getY())) { // some checks to try and avoid the expensive intersect calculation.
				if (coordinate.getY() <= Math.max(c1.getY(), c2.getY())) {
					if (coordinate.getX() <= Math.max(c1.getX(), c2.getX())) {
						if (c1.getY() != c2.getY()) {
							var xIntercept = (coordinate.getY()-c1.getY())*(c2.getX()-c1.getX()) / (c2.getY()-c1.getY()) + c1.getX();
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
});
