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

dojo.provide("geomajas.map.layertree.GoogleImageFactory");
dojo.require("dojox.collections.Dictionary");

dojo.declare("GoogleImageFactory", null, {

	/**
	 * @fileoverview Factory for raster images from Google raster layers.
	 * @class Factory for raster images from Google raster layers.
	 * @author Jan De Moerloose
	 * 
	 * @constructor
	 */
	constructor : function (type) {
		// init map types
		this.mapTypes = new dojox.collections.Dictionary();
		this.layer = null;
		
		log.info("checking for google maps");
		log.info("checking for google maps : "+typeof(G_NORMAL_MAP));
		
		if(typeof(G_NORMAL_MAP) != "undefined"){
			this.mapTypes.add("G_NORMAL_MAP@GoogleLayer", G_NORMAL_MAP);
			this.mapTypes.add("G_SATELLITE_MAP@GoogleLayer", G_SATELLITE_MAP);
			this.mapTypes.add("G_HYBRID_MAP@GoogleLayer", G_HYBRID_MAP);
			this.mapTypes.add("G_PHYSICAL_MAP@GoogleLayer", G_PHYSICAL_MAP);
 		
			// the current map type
			this.currentMapType =G_NORMAL_MAP;

			this._assertMapType(type);
		} else {
			log.warn("no google maps");
		}
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
			if(typeof(GPoint) != "undefined"){
				image.setUrl(this.currentMapType.getTileLayers()[0].getTileUrl(new GPoint(image.getXIndex(),image.getYIndex()),image.getLevel()));
			}
			image.setStyle(this.layer.getStyle());
			return image;	
	},
	
	setLayer : function (layer){
		this.layer = layer;
	},
	
	
	/**
	 * @private
	 */
	_assertMapType : function (url) {
		if(!this.mapTypes.contains(url)){
			return;
		}
		if(this.currentMapType !== this.mapTypes.item(url)) {
			this.currentMapType = this.mapTypes.item(url);
		}		
	}

});
