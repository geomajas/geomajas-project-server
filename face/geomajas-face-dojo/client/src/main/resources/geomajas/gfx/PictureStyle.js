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

dojo.provide("geomajas.gfx.PictureStyle");
dojo.declare("PictureStyle", null, {

	/**
	 * @fileoverview Simple style object for CSS.
	 * @class Simple style object for CSS.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function (opacity) {
		this.opacity = opacity;
	},

	clone : function () {
		return new PictureStyle(this.opacity);
	},

	// Getters and setters:

	getOpacity : function () {
		return this.opacity;
	},

	setOpacity : function (opacity) {
		this.opacity = opacity;
	}
});