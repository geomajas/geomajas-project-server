dojo.provide("geomajas.gfx.painter.MapModelPainter");
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
dojo.require("geomajas.gfx.Painter");

dojo.declare("MapModelPainter", Painter, {

	/**
	 * @class Painter implementation for map model
	 * 
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
	paint : function (mapModel, graphics) {
		// update the translation part of the transform
		graphics.drawGroup ({
			id:        mapModel.getId(),
			transform: this.mapView.getPanToViewTranslation() // translation for world space !!!!
		});

		if (mapModel.getPaintableObjects() != null && mapModel.getPaintableObjects().count != 0) {
			// Paint the paintable objects as well:
			graphics.drawGroup ({
				id:        mapModel.getId() + "._world.paintables",
				transform: this.mapView.getWorldToViewTransformation() // translation for world space !!!!
			});
		}
	}
});
