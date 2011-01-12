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

dojo.provide("geomajas.map.Camera");
dojo.require("geomajas.spatial.Coordinate");

dojo.declare("Camera", null, {

	/**
	 * @fileoverview This object determines the current position on the map.
	 * @class This object determines the current position on the map.
	 * The x and y are in world coordinates, but after transformation to
	 * viewspace, they are supposed to lie in the center of the SVG map.
	 *
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
		this.x = 0;
		this.y = 0;
		
		this.alpha = 0; // radial angle.
	},
	
	// init:
	
	/**
	 * Set a new position for the camera in world coordinates.
	 * @param x Value along the X-axis.
	 * @param y Value along the Y-axis.
	 */
	setPosition : function (/*Double*/x, /*Double*/y) {
		this.x = x;
		this.y = y;
	},
	
	/**
	 * Return the current position of the camera in world cooredinates.
	 * @returns The position as a Coordinate object.
	 */
	getPosition : function () {
		return new Coordinate (this.x, this.y);
	},
	
	// Morphing functions:
	
	/**
	 * Move the camera along 2 axis.
	 * @param x Translation along the X-axis.
	 * @param y Translation along the Y-axis.
	 */
	translate : function (x, y) {
		this.x += x;
		this.y += y;
	},

	/**
	 * Rotate the camera over the given angle.
	 * @param angle Should be a double. (radial, not degrees)
	 */	
	rotate : function (angle) {
		this.alpha += angle;
	},
	
	toString : function () {
		return "Camera: x="+this.x+", y="+this.y+", a="+this.alpha;
	},
	
	// Getters and setters:
	
	getX : function () {
		return this.x;
	},
	
	getY : function () {
		return this.y;
	},

	getAlpha : function () {
		return this.alpha;
	},
	
	setX : function (x) {
		this.x = x;
	},

	setY : function (y) {
		this.y = y;
	},

	setAlpha : function (alpha) {
		this.alpha = alpha;
	}

});
