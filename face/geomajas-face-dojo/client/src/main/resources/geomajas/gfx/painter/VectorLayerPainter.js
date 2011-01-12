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
		var styles = layer.getStyles();
		for (var i=0; i<styles.count; i++) {
			var style = styles.item(i);
			graphics.drawShapeType({ style: style.getStyle(), id: style.styleId	});
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