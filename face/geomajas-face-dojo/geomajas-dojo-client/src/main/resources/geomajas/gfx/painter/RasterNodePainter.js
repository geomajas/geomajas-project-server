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

dojo.provide("geomajas.gfx.painter.RasterNodePainter");
dojo.require("geomajas.gfx.Painter");

dojo.declare("RasterNodePainter", Painter, {

	/**
	 * @class Painter implementation for raster nodes. This painter always paints
	 * in viewspace!
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends Painter
	 */
	constructor : function () {
		this.maxImagesPerNode = 20;
	},

	/**
	 * The actual painting function.
	 * @param object A Paintable object.
	 * @param graphics A GraphicsContext object, responsible for actual drawing.
	 */
	paint : function (node, graphics) {
		log.debug("painting raster node "+node.getId()+","+node.getImages().count);
		// default for vml is vml:group, needs div option to force div !
		graphics.drawGroup({id : node.getId(), type : "div"});
		if(node.isVisible()){
			log.debug("painting raster node showing "+node.getId());
			graphics.unhide(node.getId());
		} else {
			log.debug("painting raster node hiding "+node.getId());
			graphics.hide(node.getId());
		}
	}
});