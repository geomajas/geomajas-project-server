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

dojo.provide("geomajas.spatial.Coordinate");
dojo.declare("Coordinate", null, {

	/**
	 * @fileoverview Object for 2-dimensional points.
	 * @class Object for 2-dimensional points.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param x The x-ordinate.
	 * @param y The y-ordinate.
	 */
	constructor : function (x, y) {
		this.x = x;
		this.y = y;
	},
	
	equals : function (coordinate) {
		return (this.x == coordinate.getX() && this.y == coordinate.getY());
	},
	
	equalsDelta : function (coordinate, delta) {
		return (Math.abs(this.x-coordinate.getX()) < delta && Math.abs(this.y-coordinate.getY()) < delta);
	},

	clone : function () {
		return new Coordinate(this.x, this.y);
	},
	
	translate : function (dx, dy) {
		this.x = this.x + dx;
		this.y = this.y + dy;
	},
	
	/**
	 * Return this object as a string.
	 */
	toString : function () {
		return "Coordinate(" + this.x + ", " + this.y + ")";
	},
	
	// Getters and setters.

	getX : function () {
		return this.x;
	},

	setX : function (/*Double*/x) {
		this.x = x;
	},

	getY : function () {
		return this.y;
	},

	setY : function (/*Double*/y) {
		this.y = y;
	}

});
