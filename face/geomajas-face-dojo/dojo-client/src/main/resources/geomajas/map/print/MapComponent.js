dojo.provide("geomajas.map.print.MapComponent");
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
dojo.declare("MapComponent", BaseComponent, {

	/**
	 * @class A map component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/* json object */ json, /* unique string */ id) {
		if(!this.mapId){
			this.mapId = "default";
		}
	},
	
	/**
	 * returns the location of the map in world coordinates
	 */
	getLocation : function () {
		return new Coordinate(this.location.x,this.location.y);
	},

	setLocation : function (location) {
		this.location = new Coordinate(location.x,location.y);
	},
	
	/**
	 * returns the scale of the map in pixels per unit (assuming 1 pixel per
	 * user unit (1/72 inch))
	 */
	getPpUnit : function () {
		return this.ppUnit;
	},
	
	setPpUnit : function (ppUnit){
		this.ppUnit = ppUnit;
	},
	
	getRasterResolution : function () {
		return this.rasterResolution;
	},
	
	setRasterResolution : function (rasterResolution){
		this.rasterResolution = rasterResolution;
	},
	
	setMapId : function (mapId){
		this.mapId = mapId;
	},
	
	/**
	 * Adds a view port at the center of the map with a size of 1/10th of the
	 * map and a scale of 3
	 */
	addViewPort : function() {
		var vp = new ViewPortComponent();
		// put it in lower-left corner
		vp.setBounds(new Bbox(this.getBounds().getX() + 10, this.getBounds().getY() + 10, 100, 100));
		// center the port bounds
		var pb = vp.getPortBounds().clone();
		pb.setCenterPoint(this.getBounds().getCenterPoint().clone());
		vp.setPortBounds(pb);
		this.addComponent(vp);
	},

	/**
	 * Copies the layers of this map to this component
	 */
	copyLayers : function (mapModel) {
		this.clearLayers();
		this.setMapId(mapModel.id);
		var layerList = mapModel.getOrderedLayers();
		for (var i=layerList.count-1; i >= 0; i--) {
			var layer = layerList.item(i);
			var layerComponent = null;
			if (layer instanceof VectorLayer && layer.isVisible() && layer.checkScaleVisibility()) {
				layerComponent = new VectorLayerComponent({},this.id+"."+layer.getId());
				layerComponent.setLayer(layer);
				this.children.list.unshift(layerComponent);
			} else if (layer instanceof RasterLayer && layer.isVisible() && layer.checkScaleVisibility()) {
				layerComponent = new RasterLayerComponent({},this.id+"."+layer.getId());
				layerComponent.setLayer(layer);
				this.children.list.unshift(layerComponent);
			}
		}
		// copy to view ports
		for ( var i = 0; i < this.children.list.length; i++) {
			if (this.children.list[i] instanceof ViewPortComponent) {
				this.children.list[i].copyLayers(mapModel);
			}
		}		
	},
	
	/**
	 * Removes all layer components of this map component
	 */
	clearLayers : function () {
		for(var i = this.children.list.length-1; i >= 0; i--){
			if (this.children.list[i] instanceof VectorLayerComponent
					|| this.children.list[i] instanceof RasterLayerComponent) {
				this.children.list.splice(i,1);
			}
		}
	}
	
});
