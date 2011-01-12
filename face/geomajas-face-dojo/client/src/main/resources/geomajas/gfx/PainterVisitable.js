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

dojo.provide("geomajas.gfx.PainterVisitable");
dojo.declare("PainterVisitable", null, {

	/**
	 * @class Interface defining the visitor structure for painting. Something
	 * is "PainterVisitable" if it can be visited by a PainterVisitor, which
	 * indirectly also means it can be painted.
	 * @author Pieter De Graef & Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
	},

	getId : function () {
		alert("PainterVisitable:getId => override me!");
	},

	/**
	 * PainterVisitable objects must implement this function.
	 * @param visitor A PainterVisitor object.
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		alert("PainterVisitable:accept => override me!");
	}
});