dojo.provide("geomajas.spatial.geometry.MultiPolygon");
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
dojo.declare("MultiPolygon", Geometry, {

	/**
	 * @fileoverview Geometry implementation for multipolygons.
	 * @class Geometry implementation for multipolygons.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Geometry
	 */
	constructor : function () {
		this.polygons = [];
	},

	/**
	 * Create a copy of this geometry and return it.
	 */
	clone : function () {
		var fac = this.getGeometryFactory();
		var polys = [];
		for(var i=0; i<this.polygons.length; i++) {
			polys[i] = this.polygons[i].clone();
		}
		return fac.createMultiPolygon(polys);
	},

	/**
	 * Multiple cases, if an integer is passed, this function turns it into
	 * [0, 0, n]. If an array of length 2 is passed, this function turns it
	 * into [0, n[0, n[1]]].
	 * @param n Should be an array with 3 integers. The first determines the
	 *          polygon to search for an integer, the other 2 are delegated
	 *          to the polygon's "getCoordinateN" function.
	 * @return Returns a {@link Coordinate} or null.
	 */
	getCoordinateN : function (n) {
		if (n instanceof Array && n.length == 1) { // assume [0, 0, n[0]]
			if (this.polygons.length >= 1) {
				var poly = this.polygons[0];
				return poly.getCoordinateN(n);
			}
		} else if (n instanceof Array && n.length == 2) { // assume [0, n[0], n[1]]
			if (this.polygons.length >= 1) {
				var poly = this.polygons[0];
				return poly.getCoordinateN(n);
			}
		} else if (n instanceof Array && n.length >= 3) {
			if (this.polygons.length >= 1) {
				var poly = this.polygons[n[0]];
				return poly.getCoordinateN([n[1], n[2]]);
			}
		} else {
			if (this.polygons.length >= 1) {
				var poly = this.polygons[0];
				return poly.getCoordinateN(n);
			}
		}
		return null;
	},

	/**
	 * If n is an integer or an array of length 1, this function will try to
	 * return the n'th polygon. If n is an array of length 2 or 3, it will
	 * get polygon n[0], and pass the other integer(s) to that polygon's
	 * "getGeometryN" function. If all fails, it will return this object itself.
	 * @param n An integer or array of integers.
	 */
	getGeometryN : function (n) {
		if (n instanceof Array && n.length >= 3) {
			if (n[0] >= 0 && n[0]<this.polygons.length) {
				return this.polygons[n[0]].getGeometryN([ n[1], n[2] ]);
			}
		} else if (n instanceof Array && n.length == 2) {
			if (n[0] >= 0 && n[0]<this.polygons.length) {
				return this.polygons[n[0]].getGeometryN(n[1]);
			}
		} else if (n instanceof Array && n.length == 1) {
			if (n[0] >= 0 && n[0]<this.polygons.length) {
				return this.polygons[n[0]];
			}
		} else if (n != null && n >= 0 && n<this.polygons.length) {
			return this.polygons[n];
		}
		return this;
	},

	/**
	 * Returns the number of polygons.
	 */
	getNumGeometries : function () {
		return this.polygons.length;
	},

	/**
	 * Returns the added number of polygon area's.
	 */
	getArea : function () {
		var area = 0;
		if (this.polygons != null) {
			for (var i=0; i<this.polygons.length; i++) {
				area += this.polygons[i].getArea();
			}
		}
		return area;
	},

	/**
	 * Returns the total length.
	 */
	getLength : function () {
		var len = 0;
		for (var i=0; i<this.polygons.length; i++) {
			len += this.polygons[i].getLength();
		}
		return len;
	},

	/**
	 * The centroid is also known as the "centre of gravity" or the
	 * "center of mass".
	 * @return Return the centre point.
	 */
	getCentroid : function () {
		return this.polygons[0].getCentroid();
	},

	/**
	 * Returns the minimal distance between this coordinate and any vertex of the geometry
	 * @return Return the minimal distance
	 */
	getDistance : function (coordinate) {
		var distance = Number.MAX_VALUE;
		for (var i=0; i<this.polygons.length; i++) {
			var d = this.polygons[i].getDistance(coordinate);
			if(d < distance){
				distance = d;
			}
		}
		return distance;
	},
	/**
	 * Returns the minimal distance between this coordinate and any vertex of the geometry
	 * @return Return the minimal distance
	 */
	getDistanceApprox : function (coordinate) {
		var distance = Number.MAX_VALUE;
		for (var i=0; i<this.polygons.length; i++) {
			var d = this.polygons[i].getDistanceApprox(coordinate);
			if(d < distance){
				distance = d;
			}
		}
		return distance;
	},

	/**
	 * Checks all it's polygons for validity.
	 */
	isValid : function () {
		for (var i=0; i<this.polygons.length; i++) {
			if (!this.polygons[i].isValid()) {
				return false;
			}
		}
		return true;
	},

	/**
	 * Shouldn't be here.
	 */
	appendCoordinate : function (coordinate) {
		if (this.polygons == null || this.polygons.length == 0) {
			var fac = new GeometryFactory(this.srid, this.precision);
			var ring = fac.createLinearRing([coordinate, coordinate]);
			this.polygons = [ fac.createPolygon(ring, []) ];
		} else {
			var polygon = this.polygons[0];
			polygon.appendCoordinate(coordinate);
		}
	},

	/**
	 * Returns the closest Bbox around the geometry.
	 */
	getBounds : function () {
		var num = this.getNumGeometries();
		if (num == null || num == 0) {
			return null;
		}
		var bounds = null;
		for (var i=0; i<num; i++) {
			var coords = this.getGeometryN(i).getCoordinates();
			if (bounds == null) {
				bounds = this._computeBounds(coords);
			} else {
				bounds = bounds.union(this._computeBounds(coords));
			}
		}
		return bounds;
	},

	/**
	 * Get area of the intersection of "this" multi-polygon and a given rectangle.
	 *
	 * @param  rectangleClockWise		 	Clockwise directed rectangle (LinearRing)
	 * @param  rectangleCounterClockWise 	Same rectangle, but directed counter-clockwise (LinearRing)
	 *
	 * @return 0 if no intersection, else the area
	 *
	 *  @note Rectangles are provided in both directions for optimization reasons
	 */
	getAreaIntersectionWithRectangle : function (rectangleClockWise, rectangleCounterClockWise) {
		var area = 0;
		if (this.polygons != null) {
			for (var i=0; i<this.polygons.length; i++) {
				area += this.polygons[i].getAreaIntersectionWithRectangle(rectangleClockWise, rectangleCounterClockWise);
			}
		}
		return area;
	},
	/**
	 * @private
	 */
	_getPrimaryCoordinates : function () {
		if (this.polygons != null && this.polygons.length > 0) {
			var poly = this.polygons[0];
			return poly.getCoordinates();
		}
		return null;
	}
});