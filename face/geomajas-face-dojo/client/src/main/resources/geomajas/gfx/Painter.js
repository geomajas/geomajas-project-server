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
