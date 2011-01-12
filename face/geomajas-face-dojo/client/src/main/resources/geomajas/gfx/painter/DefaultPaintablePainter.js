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

dojo.provide("geomajas.gfx.painter.DefaultPaintablePainter");
dojo.require("geomajas.gfx.Painter");

dojo.declare("DefaultPaintablePainter", Painter, {

	/**
	 * @class Painter implementation for default Paintable objects like the
	 * Rectangle or the Line. This painter always paints in viewspace!
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function () {
	},
	
	/**
	 * The actual painting function. Sets the modus to viewspace, applies the
	 * object's style, and then draws the geometry with the object's id.
	 * @param object A Paintable object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (/*Object*/object, /*GraphicsContext*/graphics) {
		var geometry = object.getGeometry();
		if (geometry != null) {
			if(geometry.declaredClass == geomajas.GeometryTypes.POLYGON){
				graphics.drawPolygon (geometry,{ id:object.getId(), style:object.getStyle() });
			} else if (geometry.declaredClass == geomajas.GeometryTypes.LINESTRING) {
				graphics.drawLine (geometry,{ id:object.getId(), style:object.getStyle() });
			} else if (geometry.declaredClass == geomajas.GeometryTypes.MULTIPOLYGON) {
				graphics.drawPolygon (geometry,{ id:object.getId(), style:object.getStyle() });
			} else if (geometry.declaredClass == geomajas.GeometryTypes.MULTILINESTRING) {
				graphics.drawLine (geometry,{ id:object.getId(), style:object.getStyle() });
			} else if (geometry.type == geomajas.GeometryTypes.POINT) {
				graphics.drawSymbol (geometry,{ id:object.getId(), style:object.getStyle() });
			}
		}
	}
});