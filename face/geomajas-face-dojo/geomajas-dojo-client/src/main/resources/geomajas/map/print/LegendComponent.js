dojo.provide("geomajas.map.print.LegendComponent");
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
dojo.declare("LegendComponent", BaseComponent, {

	/**
	 * @class 
	 * A legend component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/* json object */ json, /* unique string */ id) {
		if(!this.mapId){
			this.mapId = "default";
		}
	},
		
	setMapId : function (mapId){
		this.mapId = mapId;
	},
		
	/**
	 * overridden to ignore children (not taken into account for the moment)
	 */
	accept : function (/*Visitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit (this);
	},

	/**
	 * overridden to ignore children (not taken into account for the moment)
	 */
	executeTopDown : function (/*function*/callback) {
		if(callback(this)){
			return true;
		}
		return false;
	},
	
	/**
	 * overridden to ignore children (not taken into account for the moment)
	 */
	executeBottomUp : function (/*function*/callback) {
		if(callback(this)){
			return true;
		}
	},

	/**
	 * Copies the layers of this map to this component
	 */
	copyLayers : function (mapModel) {
		this.clearItems();
		this.setMapId(mapModel.id);
		var layerList = mapModel.getVisibleLayerList();
		for (var i=0 ; i < layerList.count; i++) {
			var layer = layerList.item(i);
			var item = null;
			if(layer instanceof VectorLayer){
				var label = layer.getLabel();
				var defs = layer.getStyles();
				for (var j = 0; j < defs.count; j++) {
					var text = "";
					var def = defs.item(j);
					if (defs.count > 1) {
						text = label + "(" + def.getName() + ")";
					} else {
						text = label;
					}
					var item = new LegendItemComponent({},this.getId()+"."+i+"."+j);
					item.init(def, text, layer.getLayerType(), this.font);
					this.children.list.push(item);
				}
			} else if(layer instanceof RasterLayer){
				var label = layer.getLabel();
				var item = new LegendItemComponent({},this.id+"."+i);
				item.init(null,label,layer.getLayerType(), this.font);
				this.children.list.push(item);
			}
		}		
	},
	
	/**
	 * Removes all layer components of this map component
	 */
	clearItems : function () {
		for(var i = this.children.list.length-1; i >= 0; i--){
			if(this.children.list[i] instanceof LegendItemComponent){
				this.children.list.splice(i,1);
			}
		}
	}
	
});
