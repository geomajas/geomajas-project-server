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

dojo.provide("geomajas.gfx.painter.RectanglePainter");
dojo.require("geomajas.gfx.Painter");

dojo.declare("RectanglePainter", Painter, {

	/**
	 * @class Painter implementation for rectangles. This painter always
	 * paints in viewspace!
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function () {
	},
	
	/**
	 * The actual painting function. Sets the modus to viewspace, applies the
	 * object's style, and then draws the rectangle with the object's id.
	 * @param rectangle A Rectangle object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (/*Rectangle*/rectangle, /*GraphicsContext*/graphics) {
		graphics.drawRectangle (rectangle, { id:rectangle.getId(), style:rectangle.getStyle() });
	}
});