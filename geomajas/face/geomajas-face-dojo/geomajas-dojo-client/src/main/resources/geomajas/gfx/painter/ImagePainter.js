dojo.provide("geomajas.gfx.painter.ImagePainter");
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

dojo.declare("ImagePainter", Painter, {

	/**
	 * @class Painter implementation for pictures. This painter always
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
	 * object's style, and then draws the image with the object's id.
	 * @param object A Picture object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (/*Picture*/picture, /*GraphicsContext*/graphics) {
		graphics.drawImage(picture, {id:picture.getId(), style:picture.getStyle()});
	}
});
