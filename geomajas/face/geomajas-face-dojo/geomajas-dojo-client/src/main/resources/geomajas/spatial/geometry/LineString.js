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

dojo.provide("geomajas.spatial.geometry.LineString");
dojo.declare("LineString", Geometry, {

	/**
	 * @fileoverview Geometry implementation for LineStrings.
	 * @class Geometry implementation for LineStrings.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Geometry
	 */
	constructor : function () {
		this.coordinates = [];
	},

	/**
	 * Create a copy of this geometry and return it.
	 */
	clone : function () {
		var fac = this.getGeometryFactory();
		var coords = [];
		for(var i=0; i<this.coordinates.length; i++) {
			coords[i] = this.coordinates[i].clone();
		}
		return fac.createLineString(coords);
	},

	/**
	 * Return a coordinate, or null. 
	 * @param n Index in the geometry. This can be an integer value or an array
	 *          of values.
	 * @return A coordinate or null.
	 */
	getCoordinateN : function (n) {
		return this.getGeometryN(n);
	},

	/**
	 * Returns a coordinate, or the geometry itself.
	 * @param n Index in the geometry. This can be an integer value or an array
	 *          of values. Tries to get the coordinate n or n[0]. If that fails
	 *          it will return this linestring object. 
	 * @return A geometry object.
	 */
	getGeometryN : function (n) {
		if (n instanceof Array) {
			if (n.length == 0) {
				return this;
			} else if (n[0] >= 0 && n[0] < this.coordinates.length) {
				return this.coordinates[n[0]];
			}
		} else if (n >= 0 && n<this.coordinates.length) {
			return this.coordinates[n];
		}
		return this;
	},

	/**
	 * Returns the number of coordinates.
	 */
	getNumGeometries : function () {
		return this.getNumPoints();
	},

	/**
	 * Add a coordinate to the list. Shouldn't be here!
	 */
	appendCoordinate : function (coordinate) {
		var linestring = this._getPrimaryCoordinates();
		var length = linestring.length;
		linestring[length] = coordinate;
	},

	/**
	 * Returns 0.
	 */
	getArea : function () {
		return 0;
	},

	/**
	 * Returns the length of the linestring.
	 */
	getLength : function () {
		var len = 0;
		for (var i=0; i<this.coordinates.length-1; i++) {
			var deltaX = this.coordinates[i+1].getX() - this.coordinates[i].getX();
			var deltaY = this.coordinates[i+1].getY() - this.coordinates[i].getY();
			len += Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		}
		return len;
	},

	/**
	 * The centroid is also known as the "centre of gravity" or the
	 * "center of mass".
	 * @return Return the centre point.
	 */
	getCentroid : function () {
		if (this.coordinates.length % 2 == 1) {
			var len = Math.floor(this.coordinates.length / 2);
			return this.coordinates[len];
		} else {
			var i2 = this.coordinates.length / 2;
			var i1 = i2-1;
			var ls = new LineSegment(this.coordinates[i1], this.coordinates[i2]);
			return ls.getMiddlePoint();
		}
	},

	
	/**
	 * Returns the minimal distance between this coordinate and any line segment of the geometry
	 * @return Return the minimal distance
	 */
	
	getDistance : function (coordinate) {
		var mathLib = new MathLib();
		var minDistance = Number.MAX_VALUE;

		for (var i=0; i<this.coordinates.length-1; i++) {
			var dist = mathLib.distance(this.coordinates[i], this.coordinates[i+1],coordinate);
			if (dist  < minDistance){
				minDistance = dist;
			}
		}
		return minDistance;		
	}, 
	
	/**
	 * Returns a measure for the distance between this coordinate and any line segment of the geometry
	 * @return Returns a measure for the minimal distance (approximation, not the real dist)
	 */
	
	getDistanceApprox : function (coordinate) {
		var distance = Number.MAX_VALUE;
		for (var i=0; i<this.coordinates.length; i++) {
			var dx = this.coordinates[i].getX()-coordinate.getX();
			var dy = this.coordinates[i].getY()-coordinate.getY();
			if(Math.abs(dx)+Math.abs(dy)< distance){
				distance = Math.abs(dx)+Math.abs(dy);
			}
		}
		return distance;		
	},

	/**
	 * Returns true.
	 */
	isValid : function () {
		return true;
	},

	log : function () {
		dojo.debug("LineString:");
		var coords = this.getCoordinates();
		for (var i=0; i<this.getNumPoints(); i++) {
			dojo.debug("\t"+coords[i].toString());
		}
	},

	/**
	 * @private
	 */
	_getPrimaryCoordinates : function (){
		return this.coordinates;
	}
});