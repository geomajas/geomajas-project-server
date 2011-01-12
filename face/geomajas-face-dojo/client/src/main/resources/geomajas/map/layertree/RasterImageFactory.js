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
		image.setLevel(jsonImage.code.tileLevel);
		image.setXIndex(jsonImage.code.x);
		image.setYIndex(jsonImage.code.y);
		image.setStyle(this.layer.getStyle());
		return image;	
	},
	
	setLayer : function (layer){
		this.layer = layer;
	}
	
	

});