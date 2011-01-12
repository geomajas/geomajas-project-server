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

dojo.provide("geomajas.gfx.paintables.Picture");
dojo.require("geomajas.gfx.PainterVisitable");

dojo.declare("Picture", PainterVisitable, {

	constructor : function (id) {
		this.id = id;
		this.position = null;
		this.width = null;
		this.height = null;
		this.href = null;
		this.style = null;
	},

	/**
	 * Everything that can be drawn on the map, must be accessible by a
	 * PainterVisitor!
	 * @param visitor A PainterVisitor object. Comes from a MapWidget.
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit(this);
	},

	// Getters and setters:

	getId : function () {
		return this.id;
	},

	setId : function (id) {
		this.id = id;
	},

	getPosition : function () {
		return this.position;
	},

	setPosition : function (position) {
		this.position = position;
	},

	getWidth : function () {
		return this.width;
	},

	setWidth : function (width) {
		this.width = width;
	},

	getHeight : function () {
		return this.height;
	},

	setHeight : function (height) {
		this.height = height;
	},

	getStyle : function () {
		return this.style;
	},

	setStyle : function (style) {
		this.style = style;
	},

	getHref : function () {
		return this.href;
	},

	setHref : function (href) {
		this.href = href;
	}
});