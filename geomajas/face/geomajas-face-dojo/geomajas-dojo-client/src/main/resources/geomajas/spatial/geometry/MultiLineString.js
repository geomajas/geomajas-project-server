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

dojo.provide("geomajas.spatial.geometry.MultiLineString");
dojo.declare("MultiLineString", Geometry, {

	/**
	 * @fileoverview Geometry implementation for MultiLineStrings.
	 * @class Geometry implementation for MultiLineStrings.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Geometry
	 */
	constructor : function () {
		this.lineStrings = [];
	},

	/**
	 * Create a copy of this geometry and return it.
	 */
	clone : function () {
		var fac = this.getGeometryFactory();
		var lines = [];
		for(var i=0; i<this.lineStrings.length; i++) {
			lines[i] = this.lineStrings[i].clone();
		}
		return fac.createMultiLineString(lines);
	},

	/**
	 * Multiple cases. If n is a integer, the n'th coordinate in the exterior
	 * ring will be returned. If n is an array with 2 indeces, the first integer
	 * depicts the linearring, the second integer the index in this ring.
	 * @param n Should be an array with 2 integers. Although just an integer is 
	 *          also supported.
	 */
	getCoordinateN : function (n) {
		if (n instanceof Array && n.length == 1) { // assume [0, n[0]]
			if (this.lineStrings.length >= 1) {
				var lineString = this.lineStrings[0];
				return lineString.getCoordinateN(n);
			}
		} else if (n instanceof Array && n.length >= 2) { // assume [n[0], n[1]]
			if (this.lineStrings.length >= 1) {
				var lineString = this.lineStrings[n[0]];
				return lineString.getCoordinateN(n[1]);
			}
		} else {
			if (this.lineStrings.length >= 1 && n != null) {
				var lineString = this.lineStrings[0];
				return lineString.getCoordinateN(n);
			}
		}
		return this;
	},

	/**
	 * Multiple cases. If n is an integer, the n'th {@link LineString} will
	 * be returned. If n  is an array of length 1 same as above. If n is an
	 * array of length 2, this function will delegate to the linestring n[0].
	 * It will ask for the "getGeometryN(n[1])".
	 * @param n Should be an array with max 2 integers. Although just an
	 *          integer is also supported.
	 */
	getGeometryN : function (n) {
		if (n instanceof Array && n.length >= 2) {
			return this.lineStrings[n[0]].getGeometryN(n[1]);
		} else if (n instanceof Array && n.length == 1) {
			return this.lineStrings[n[0]];
		} else if (n >= 0 && n < this.lineStrings.length) {
			return this.lineStrings[n];
		}
		
		return null;
	},

	/**
	 * Returns the number of linestrings.
	 */
	getNumGeometries : function () {
		return this.lineStrings.length;
	},

	/**
	 * Returns 0.
	 */
	getArea : function () {
		return 0;
	},

	/**
	 * Returns the added length of all the linestrings.
	 */
	getLength : function () {
		var len = 0;
		for (var i=0; i<this.lineStrings.length; i++) {
			len += this.lineStrings[i].getLength();
		}
		return len;
	},

	/**
	 * The centroid is also known as the "centre of gravity" or the
	 * "center of mass".
	 * @return Return the centre point.
	 */
	getCentroid : function () {
		return this.lineStrings[0].getCentroid();
	},

	/**
	 * Returns the minimal distance between this coordinate and any vertex of the geometry
	 * @return Return the minimal distance
	 */
	getDistance : function (coordinate) {
		var distance = Number.MAX_VALUE;
		for (var i=0; i< this.lineStrings.length; i++) {
			var d = this.lineStrings[i].getDistance(coordinate);
			if(d < distance){
				distance = d;
			}
		}
		return distance;		
	},

	getDistanceApprox : function (coordinate) {
		var distance = Number.MAX_VALUE;
		for (var i=0; i< this.lineStrings.length; i++) {
			var d = this.lineStrings[i].getDistanceApprox(coordinate);
			if(d < distance){
				distance = d;
			}
		}
		return distance;		
	},

	/**
	 * Check the validity of all the linestrings.
	 */
	isValid : function () {
		for (var i=0; i<this.lineStrings.length; i++) {
			if (!this.lineStrings[i].isValid()) {
				return false;
			}
		}
		return true;
	},

	/**
	 * Shouldn't be here!
	 */
	appendCoordinate : function (coordinate) {
		if (this.lineStrings == null || this.lineStrings.length == 0) {
			var fac = new GeometryFactory(this.srid, this.precision);
			this.lineStrings = [ fac.createLineString([coordinate]) ];
		} else {
			this.lineStrings[0].appendCoordinate(coordinate);
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
	 * @private
	 */
	_getPrimaryCoordinates : function () {
		if (this.lineStrings != null && this.lineStrings.length > 0) {
			return this.lineStrings[0].getCoordinates();
		}
		return null;
	}
});