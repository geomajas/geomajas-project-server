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
