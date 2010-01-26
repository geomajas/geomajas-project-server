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

dojo.provide("geomajas.gfx.painter.VectorLayerPainter");
dojo.require("geomajas.gfx.Painter");

dojo.declare("VectorLayerPainter", Painter, {

	/**
	 * @class Painter implementation for layers. This painter always paints
	 * in worldspace!
	 * @author Jan De Moerloose & Pieter De Graef
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function (mapView) {
		this.mapView = mapView;
		this.labelStyle = new FontStyle("#FF0000", "10pt", "Courier New, Arial", "normal", "normal");
	},

	/**
	 * The actual painting function. It applies known layer styles, and also
	 * check the layer's visibility status. 
	 * @param layer A Paintable object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (layer, graphics) {
		log.info("VectorLayerPainter.paint : painting layer "+layer.layerId + ", current scale = "+this.mapView.getCurrentScale());
        // Always redraw the layer style, because it can change anytime:
		graphics.drawGroup ({
			style:     layer.getDefaultStyle(),
			id:        layer.getId()
		});
		graphics.drawGroup ({id: layer.getId() + ".features"}); // create the group...
		graphics.drawGroup ({id: layer.getId() + ".selection"}); // create the group...
		graphics.drawGroup ({id: layer.getId() + ".labels"}); // create the group...
		

		// Draw symbol types, these can change anytime as well:
		for (var i=0; i<layer.getStyles().count; i++) {
			var style = layer.getStyles().item(i).getStyle();
			graphics.drawShapeType({
				style:     style,
				id:        layer.getLayerId()+"."+layer.getStyles().item(i).getId()+".style",
			});
		}

		// Check layer visibility:
		if (layer.checkVisibility(this.mapView.getCurrentScale())) {
			graphics.unhide (layer.getId());
		} else {
			graphics.hide (layer.getId());
		}

		// Check label visibility:
		if (layer.isLabeled()) {
			if (layer.getLabelFontStyle() != null) {
				var style = this.labelStyle.clone();
				style.setFillColor (layer.getLabelFontStyle().getFillColor());
				graphics.drawGroup ({id:layer.getId() + ".labels", style:style});
			} else {
				graphics.drawGroup ({id:layer.getId() + ".labels", style:this.labelStyle});
			}
			graphics.unhide (layer.getId() + ".labels");
		} else {
			graphics.hide (layer.getId() + ".labels");
		}
	},

	/**
	 * Deletes the layer. Actualy it only deletes the children. This overriding
	 * of the basic delete function is necessary to keep the map order.
	 */
	deleteShape : function (/*VectorLayer*/layer, /*GraphicsContext*/graphics) {
		log.error ("Deleting layer "+layer.getId());
		graphics.deleteShape(layer.getId(), true);
	},

	// Getters and setters:

	setLabelStyle : function (labelStyle) {
		this.labelStyle = labelStyle;;
	},
	
	getLabelStyle : function () {
		return this.labelStyle;
	}
});