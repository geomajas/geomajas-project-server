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

dojo.provide("geomajas.gfx.paintables.Text");
dojo.require("geomajas.gfx.PainterVisitable");

dojo.declare("Text", PainterVisitable, {

	constructor : function (id, text) {
		/** A unique identifier. */
		this.id = "Text";
		if (id) {
			this.id = id;
		}

		/** The text-content. */
		this.text = "";
		if (text) {
			this.text = text;
		}

		/** The begin position. */
		this.position = null;

		/** A style object that determines the look. */
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

	getText : function () {
		return this.text;
	},

	setText : function (text) {
		this.text = text;
	},

	getStyle : function () {
		return this.style;
	},

	setStyle : function (style) {
		this.style = style;
	}
});