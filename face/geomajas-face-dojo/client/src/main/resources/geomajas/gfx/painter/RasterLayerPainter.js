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