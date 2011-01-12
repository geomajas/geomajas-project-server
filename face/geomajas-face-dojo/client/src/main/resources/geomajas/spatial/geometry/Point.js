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

dojo.provide("geomajas.spatial.geometry.Point");
dojo.declare("Point", Geometry, {

	/**
	 * @fileoverview Geometry implementation for points.
	 * @class Geometry implementation for points.
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
		if (this.coordinates.length == 0) {
			return this.getGeometryFactory().createPoint(null);
		}
		return this.getGeometryFactory().createPoint(this.coordinates[0]);
	},

	/**
	 * Returns the only coordinate, or null if empty.
	 */
	getCoordinateN : function (n) {
		if (this.coordinates.length > 0) {
			return this.coordinates[0];
		}
		return null;
	},

	/**
	 * Return the number of coordinates.
	 */
	getNumPoints : function () {
		return this.coordinates.length;
	},

	/**
	 * Returns the only coordinate or this point itself.
	 * @param n Integer or array of integers.
	 */
	getGeometryN : function (n) {
		if (n instanceof Array) {
			if (n.length >= 1 && n[0] == 0 && this.coordinates.length > 0) {
				return this.coordinates[0];
			} else {
				return this;
			}
		} else {
			if (n == 0 && this.coordinates.length > 0) {
				return this.coordinates[0];
			}
		}
		return this;
	},

	/**
	 * Same as {@link #getNumPoints}.
	 */
	getNumGeometries : function () {
		return this.getNumPoints();
	},

	/**
	 * Shouldn't be here.
	 */
	appendCoordinate : function (coordinate) {
		if (coordinate instanceof Coordinate) {
			this.coordinates[0] = coordinate;
		}
	},

	/**
	 * Returns 0.
	 */
	getArea : function () {
		return 0;
	},

	/**
	 * Returns 0.
	 */
	getLength : function () {
		return 0;
	},

	/**
	 * The centroid is also known as the "centre of gravity" or the
	 * "center of mass".
	 * @return Return the centre point.
	 */
	getCentroid : function () {
		return this.getCoordinateN(0);
	},

	/**
	 * Returns the minimal distance between coordinate and this point
	 * @return Return the minimal distance
	 */
	getDistance : function (coordinate) {
		var dx = this.coordinates[0].getX()-coordinate.getX();
		var dy = this.coordinates[0].getY()-coordinate.getY();
		var distance = dx*dx + dy*dy;
		return Math.sqrt(distance);		 
	},
	
	getDistanceApprox: function (coordinate) {
		var distance = Number.MAX_VALUE;
		for (var i=0; i<this.coordinates.length; i++) {
			var dx = this.coordinates[i].getX()-coordinate.getX();
			var dy = this.coordinates[i].getY()-coordinate.getY();
			var dist = Math.abs(dx)+Math.abs(dy);
			if(dist < distance){
				distance = dist;
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
		dojo.debug("Point in "+this.srid);
		dojo.debug(this.coordinates[0]);
	},

	/**
	 * @private
	 */
	_getPrimaryCoordinates : function (){
		return this.coordinates;
	}
});