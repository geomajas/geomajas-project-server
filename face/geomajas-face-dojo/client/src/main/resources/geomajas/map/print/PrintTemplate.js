dojo.provide("geomajas.map.print.PrintTemplate");
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
dojo.declare("PrintTemplate", null, {

	/**
	 * @class 
	 * A print template (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function(/* json object */ json) {
		log.info("   creating template: "+ (json ? json.name : "new"));
		if(json){
			dojo.mixin(this,json);
			this._createPage();
		}
	},
	
	configureMapModel : function(/* MapModel */ mapModel) {
		var mapId = mapModel.getId();
		var map = this.getMapComponent(mapId);
		var legend = this.getLegendComponent(mapId);
		map.copyLayers(mapModel);
		legend.copyLayers(mapModel);
	},
	
	/**
	 * find the map with the specified id in the descendancy tree
	 */
	getMapComponent : function(/* map id */ mapId) {
		var map = null;
		this.page.executeTopDown(
			function(component) {
				if(component.declaredClass == "MapComponent"){
					if(component.mapId == mapId || component.tag == "map"){
						map = component;
					}
				}
			}
		);
		return map;
	},

	/**
	 * find the map with the specified id in the descendancy tree
	 */
	getLegendComponent : function(/* map id */ mapId) {
		var legend = null;
		this.page.executeTopDown(
			function(component) {
				if(component.declaredClass == "LegendComponent"){
					if(component.mapId == mapId || component.tag == "legend"){
						legend = component;
					}
				}
			}
		);
		return legend;
	},
	
	getComponentById : function (id) {
		var comp = null;
		this.page.executeTopDown(
			function(component) {
				if(component.getId() == id){
					comp = component;
					return true;
				} else {
					return false;
				}
			}
		);
		return comp;
	},
	
	getBestMatchingComponentById : function (id) {
		var comp = null;
		this.page.executeBottomUp(
			function(component) {
				if(id.startsWith(component.getId())){
					comp = component;
					return true;
				} else {
					return false;
				}
			}
		);
		return comp;
	},

	addViewPort : function ( mapId ){
		var map = this.getMapComponent(mapId);
		map.addViewPort();
	},
	
	_createPage : function() {
		this.page = new PageComponent(this.page, "print.page");
		this._createChildren(this.page);
	},

	_createChildren : function(/*BaseComponent*/parent) {
		for(var i = 0; i < parent.children.list.length; i++){
			var child = parent.children.list[i];
			var id = parent.getId()+"."+i;
			if(child.javaClass.endsWith("MapComponent")){
				parent.children.list[i] = new MapComponent(child,id);
			} else if(child.javaClass.endsWith("PageComponent")){
				parent.children.list[i] = new PageComponent(child,id);
			} else if(child.javaClass.endsWith("LabelComponent")){
				parent.children.list[i] = new LabelComponent(child,id);
			} else if(child.javaClass.endsWith("VectorLayerComponent")){
				parent.children.list[i] = new VectorLayerComponent(child,id);
			} else if(child.javaClass.endsWith("RasterLayerComponent")){
				parent.children.list[i] = new RasterLayerComponent(child,id);
			}  else if(child.javaClass.endsWith("LegendComponent")){
				parent.children.list[i] = new LegendComponent(child,id);
			}  else if(child.javaClass.endsWith("LegendItemComponent")){
				parent.children.list[i] = new LegendItemComponent(child,id);
			}  else if(child.javaClass.endsWith("ViewPortComponent")){
				parent.children.list[i] = new ViewPortComponent(child,id);
			} else {
				parent.children.list[i] = new BaseComponent(child,id);
			}
			log.info("create component "+child.javaClass+" with id "+parent.children.list[i].id);
			this._createChildren(parent.children.list[i]);
		}
	},
	
	getPage : function() {
		return this.page;
	},
	
	getName : function() {
		return this.name;
	},
	
	setName : function(name) {
		this.name = name;
	}
		
});
