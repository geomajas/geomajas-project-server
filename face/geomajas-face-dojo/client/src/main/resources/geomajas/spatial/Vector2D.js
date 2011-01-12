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
dojo.provide("geomajas.spatial.Vector2D");
dojo.require("geomajas.spatial.Coordinate");


dojo.declare("Vector2D", null, {

	/**
	 * @fileoverview A 2-dimensional vector.
	 * @class A 2-dimensional vector. We're trying to keep the mathematical
	 * difference between points and vectors alive.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param x The x-ordinate.
	 * @param y The y-ordinate.
	 */
	constructor : function (x, y) {
		this.v = [x, y];
	},
	
	/**
	 * Take a Coordinate object to make this vector.
	 * @param point Coordinate object to take the x and y values from.
	 */
	fromCoordinate : function (point) {
		this.constructor (point.getX(), point.getY());
	},
	
	/**
	 * Takes the difference between 2 Coordinates to calculate the vector.
	 * vector = point1 - point2
	 * @param point1 First coordinate.
	 * @param point2 Second coordinate.
	 */
	fromCoordinates : function (point1, point2) {
		this.constructor (point1.getX() - point2.getX(), point1.getY() - point2.getY());
	},

	/**
	 * Add another vector to this one.
	 * @param vector The other vector.
	 */
	add : function (vector) {
		this.v[0] += vector.get(0);
		this.v[1] += vector.get(1);
	},
	
	/**
	 * Subtract another vector from this one.
	 * @param vector The other vector.
	 */
	subtract : function (vector) {
		this.v[0] -= vector.get(0);
		this.v[1] -= vector.get(1);
	},
	
	/**
	 * Scale this vector.
	 * @param x Scale the X-factor with this value.
	 * @param y Scale the Y-factor with this value.
	 */
	scale : function (x, y) {
		this.v[0] *= x;
		this.v[1] *= y;
	},
	
	/**
	 * Translate this vector.
	 * @param x Translate the X-factor with x.
	 * @param y Translate the Y-factor with y.
	 */
	translate : function (x, y) {
		this.v[0] += x;
		this.v[1] += y;
	},
	
	/**
	 * Calculate the distance between 2 vector by using Pythagoras' formula.
	 * @param vector The other vector.
	 * @returns The distance between these 2 vectors as a Double. 
	 */
	distance : function (/*Vector2D*/vector) {
		var x = vector.get(0) - this.v[0];
		var y = vector.get(1) - this.v[1];
		return Math.sqrt (x*x + y*y);
	},
	
	/**
	 * Normalize this vector.
	 */
	normalize : function () {
		var len = this.length();
   		if (len==0) { return; }
		this.v[0] /= len; this.v[1] /= len;
	},
	
	/**
	 * Return the length of this vector. (Euclides)
	 * @returns The lenth as a Double.
	 */
	length : function () {
		var len = (this.v[0] * this.v[0]) + (this.v[1] * this.v[1]);
		return Math.sqrt(len);
	},

	/**
	 * Calculates a vector's cross product.
	 * @param vector2D The second vector.
	 */
	cross : function (vector2D) {
		return this.v[0] * vector2D.get(1) - this.v[1] * vector2D.get(0);
	},
	
	/**
	 * Return this vector object as a string.
	 */
	toString : function () {
		return "Vector2D("+this.v[0]+", "+this.v[1]+")";
	},
	
	// Getters and setters.

	/**
	 * Get the value with the given index.
	 * @param i Should be 0 or 1.
	 * @returns X- or Y-factor.
	 */
	get : function (/*Integer*/i) {
		if (i == 0 || i == 1) {
			return this.v[i];
		} 
		return null;
	},

	/**
	 * Set the value with the given index.
	 * @param i Should be 0 or 1.
	 * @param x The value to be set.
	 */
	set : function (/*Integer*/i, /*Double*/x) {
		if (i == 0 || i == 1) {
			this.v[i] = x;
		}
	}
});
