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

dojo.provide("geomajas.map.layertree.RasterImageFactory");
dojo.require("geomajas.map.RasterImage");

dojo.declare("RasterImageFactory", null, {

	/**
	 * @fileoverview Factory for raster images from raster layers.
	 * @class Factory for raster images from raster layers.
	 * @author Jan De Moerloose
	 * 
	 * @constructor
	 */
	constructor : function () {
		this.layer = null;
	},

	/**
	 * Create a {@link RasterImage} object from JSON input.
	 * @param jsonImage The JSON object to create a RasterImage from.
	 */
	createImage : function (jsonImage, mapId) {
		var image = new RasterImage();
		image.setId(mapId + "." + jsonImage.id);
		image.setUrl(jsonImage.url);
		var b = jsonImage.bounds;
		image.setBounds(new Bbox(b.x,b.y,b.width,b.height));
		image.setLevel(jsonImage.level);
		image.setXIndex(jsonImage.XIndex);
		image.setYIndex(jsonImage.YIndex);
		image.setStyle(this.layer.getStyle());
		return image;	
	},
	
	setLayer : function (layer){
		this.layer = layer;
	}
	
	

});