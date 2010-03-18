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
	constructor : function () {
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
			// abusing the url to pass the map type !!!
			this._assertMapType(jsonImage.url);			
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
