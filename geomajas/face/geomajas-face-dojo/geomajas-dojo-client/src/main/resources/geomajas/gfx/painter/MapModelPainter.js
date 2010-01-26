dojo.provide("geomajas.gfx.painter.MapModelPainter");
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
