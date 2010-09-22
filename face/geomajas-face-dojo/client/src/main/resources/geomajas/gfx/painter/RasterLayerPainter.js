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

dojo.provide("geomajas.gfx.painter.RasterLayerPainter");
dojo.require("geomajas._base");
dojo.require("geomajas.gfx.Painter");

dojo.declare("RasterLayerPainter", Painter, {

	/**
	 * @class Painter implementation for raster layers. This painter always paints
	 * in worldspace!
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function (mapView) {
		this.mapView = mapView;
	},

	/**
	 * The actual painting function. It applies known layer styles, and also
	 * check the layer's visibility status. 
	 * @param layer A Paintable object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (layer, graphics) {
		log.info("RasterLayerPainter.paint : painting layer "+layer.layerId + ", current scale = "+this.mapView.getCurrentScale());
		// default for vml is vml:group, needs div option to force div !
		graphics.drawGroup ({
			id:    layer.getId(),
			type: "div"
		});

		// Check visibility:
		if (layer.checkVisibility(this.mapView.getCurrentScale())) {
			graphics.unhide (layer.getId());
		} else {
			graphics.hide (layer.getId());
		}
	},

	deleteShape : function (/*Object*/object, /*GraphicsContext*/graphics) {
		graphics.deleteShape(object.getId(), true);
	}
});