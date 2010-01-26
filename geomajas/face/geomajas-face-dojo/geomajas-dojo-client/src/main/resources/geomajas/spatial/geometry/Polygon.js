dojo.provide("geomajas.spatial.geometry.Polygon");
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
dojo.declare("Polygon", Geometry, {

	/**
	 * @fileoverview Geometry implementation for polygons.
	 * @class Geometry implementation for polygons. Supports holes.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Geometry
	 */
	constructor : function () {
		/** Exterior ring, of type {@link LinearRing} */
		this.shell = null;

		/** Interior ring array, of type {@link LinearRing} */
		this.holes = [];
	},

	/**
	 * Create a copy of this geometry and return it.
	 */
	clone : function () {
		var fac = this.getGeometryFactory();
		var h = [];
		for(var i=0; i<this.holes.length; i++) {
			h[i] = this.holes[i].clone();
		}
		return fac.createPolygon(this.shell.clone(), h);
	},

	/**
	 * Multiple cases. If n is a integer, the n'th coordinate in the exterior
	 * ring will be returned. If n is an array with 2 indeces, the first integer
	 * depicts the linearring, the second integer the index in this ring.
	 * @param n Should be an array with 2 integers. Although just an integer is 
	 *          also supported.
	 */
	getCoordinateN : function (n) {
		if (n instanceof Array && n.length >= 2) {
			var ringN = n[0];
			if (ringN == 0) {
				var ring = this.shell;
				return ring.getCoordinateN(n[1]);
			} else {
				var ring = this.holes[ringN-1];
				return ring.getCoordinateN(n[1]);
			}
		} else if (n instanceof Array && n.length == 1 && this.shell != null){
			return this.shell.getCoordinateN(n[0]);
		} else if (this.shell != null){
			return this.shell.getCoordinateN(n);
		}
		return null;
	},


	/**
	 * Returns the minimal distance between this coordinate and any vertex of the geometry
	 * @return Return the minimal distance
	 */
	getDistance : function (coordinate) {
		var distance = Number.MAX_VALUE;
		for (var i=0; i<this.holes.length; i++) {
			var d = this.holes[i].getDistance(coordinate);
			if(d < distance){
				distance = d;
			}
		}
		var d = this.shell.getDistance(coordinate);
		if(d < distance){
			distance = d;
		}
		return distance;		
	},
	
	/**
	 * Returns the approx. minimal distance between this coordinate and any vertex of the geometry
	 * @return Return the approx minimal distance
	 */
	getDistanceApprox : function (coordinate) {
		var distance = Number.MAX_VALUE;
		for (var i=0; i<this.holes.length; i++) {
			var d = this.holes[i].getDistanceApprox(coordinate);
			if(d < distance){
				distance = d;
			}
		}
		var d = this.shell.getDistanceApprox(coordinate);
		if(d < distance){
			distance = d;
		}
		return distance;	
	},
	/**
	 * Returns the polygon's exterior ring. Also called the shell.
	 */
	getExteriorRing : function () {
		return this.shell;
	},

	/**
	 * Returns the number of interior rings (called holes).
	 */
	getNumInteriorRing : function () {
		return this.holes.length;
	},

	/**
	 * Returns hole numbre n. Or null.
	 * @n Integer value that determines which hole to return.
	 */
	getInteriorRingN : function (n) {
		if (n >= 0 && n < this.holes.length) {
			return this.holes[n];
		}
		return null;
	},

	/**
	 * If n is an integer or array of length 0, it will return one of it's
	 * rings. If n is an array of length 2, it will pass n[1] to the
	 * {@link #getGeometryN} of ring n[0]. Ring 0 is the shell, and ring 1 etc
	 * are the holes.
	 * @param n Integer or array of integer values.
	 */
	getGeometryN : function (n) {
		if (this.shell == null) {
			return this;
		}
		if (n instanceof Array && n.length >= 2) {
			if (n[0] == 0) {
				return this.getExteriorRing().getGeometryN(n[1]);
			} else {
				return this.getInteriorRingN(n[0]-1).getGeometryN(n[1]);
			}
		} else if (n instanceof Array && n.length == 1){
			if (n[0] == 0) {
				return this.getExteriorRing();
			} else {
				return this.getInteriorRingN(n[0]-1);
			}
		} else if (n instanceof Array && n.length == 0){
			return this;
		} else {
			if (n == 0) {
				return this.getExteriorRing();
			} else {
				return this.getInteriorRingN(n-1);
			}
		}
	},

	/**
	 * Returns the number of rings (interior + exterior)
	 */
	getNumGeometries : function () {
		var count = 0;
		if (this.shell != null) {
			count++;
		}
		return count + this.getNumInteriorRing();
	},

	/**
	 * Returns the shell area minus holes areas.
	 */
	getArea : function () {
		var area = 0;
		if (this.shell != null) {
			area = this.shell.getArea();
		}
		for (var i=0; i<this.holes.length; i++) {
			area -= this.holes[i].getArea();
		}
		return area;
	},

	/**
	 * Returns the total length of all rings.
	 */
	getLength : function () {
		var len = 0;
		if (this.shell != null) {
			len = this.shell.getLength();
		}
		for (var i=0; i<this.holes.length; i++) {
			len += this.holes[i].getLength();
		}
		return len;
	},

	/**
	 * The centroid is also known as the "centre of gravity" or the
	 * "center of mass".
	 * @return Return the centre point.
	 */
	getCentroid : function () {
		return this.shell.getCentroid();
	},

	/**
	 * Is the shell empty or not?
	 */
	isEmpty : function () {
		return this.shell == null;
	},

	/**
	 * TODO: should also check touching.
	 */
	isValid : function () {
		if (this.isEmpty()) {
			return true;
		}
		for (var i=0; i<this.getNumGeometries(); i++) {
			var ring = this.getGeometryN(i);
			if (!ring.isValid()) {
				return false;
			}
			for (j=0; j<this.getNumGeometries(); j++) {
				if (j!=i) {
					var ring2 = this.getGeometryN(j);
					if (ring.intersects(ring2)) {
						return false;
					}
				}
			}
		}
		
		return true;
	},

	/**
	 * Shouldn't be here.
	 */
	appendCoordinate : function (coordinate) {
	if (this.shell == null) {
			var fac = new GeometryFactory(this.srid, this.precision);
			this.shell = fac.createLinearRing([coordinate, coordinate]);
		} else {
			this.shell.appendCoordinate(coordinate);
		}
	},

	/**
	 * Get area of the intersection of "this" polygon and a given rectangle.
	 *
	 * @param  rectangleClockWise		 	Clockwise directed rectangle (LinearRing)
	 * @param  rectangleCounterClockWise 	Same rectangle, but directed counter-clockwise (LinearRing)
	 *
	 * @return 0 if no intersection, else the area
	 *
	 *  @note Rectangles are provided in both directions for optimization reasons
	 */
	getAreaIntersectionWithRectangle : function (rectangleClockWise, rectangleCounterClockWise) {

		var area  = 0
		if (this.shell == null)
			return 0;

		area = this.shell.getAreaIntersectionWithRectangle(rectangleClockWise, rectangleCounterClockWise);

		for (var i=0; i<this.holes.length; i++) {
			area -= this.holes[i].getAreaIntersectionWithRectangle(rectangleClockWise, rectangleCounterClockWise);
		}
		return area;
	},

	/**
	 * Getter for the hole array.
	 */
	getHoles : function () {
		return this.holes;
	},

	/**
	 * @private
	 */
	_getPrimaryCoordinates : function () {
		if (this.shell != null) {
			return this.shell.getCoordinates();
		}
		return null;
	}
});