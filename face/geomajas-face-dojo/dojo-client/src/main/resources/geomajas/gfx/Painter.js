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

dojo.provide("geomajas.gfx.Painter");
dojo.declare("Painter", null, {

	/**
	 * @class General painter interface. Has only one method: the paint method.
	 * It is based on the java AWT library. 
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
		/** A painter always paints a certain style. */
		this.style = null;

		/**
		 * This is quite SVG specific. It determines whether to add, update or
		 * remove SVG DOM elements, because that's how drawing happens in SVG.
		 */
		this.status = null;

		/**
		 * View- or worldspace.
		 */
		this.modus = null;
	},

	/**
	 * Paint a given object throught the given graphics context.
	 * @param object The object to be painted. Each painter should recognize a certain type of object.
	 * @param graphics An implementation of the "GraphicsContext" interface,
	 *           responsible for the actual drawing of the object on the screen.
	 */
	paint : function (/*Object*/object, /*GraphicsContext*/graphics) {
	},

	/**
	 * Default implementation for the deletion of an object from the map.
	 */
	deleteShape : function (/*Object*/object, /*GraphicsContext*/graphics) {
		graphics.deleteShape(object.getId());
	},

	// Getters and setters:

	getStyle : function () {
		return this.style;
	},

	setStyle : function (style) {
		this.style = style;
	},

	getStatus : function () {
		return this.status;
	},

	setStatus : function (status) {
		this.status = status;
	},

	getModus : function () {
		return this.modus;
	},

	setModus : function (modus) {
		this.modus = modus;
	}
});
