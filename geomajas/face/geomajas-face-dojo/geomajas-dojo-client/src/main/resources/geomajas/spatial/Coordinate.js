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
