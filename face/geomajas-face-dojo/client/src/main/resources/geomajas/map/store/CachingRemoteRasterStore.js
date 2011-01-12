dojo.provide("geomajas.map.store.CachingRemoteRasterStore");
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

dojo.require("dojox.collections.Dictionary");
dojo.require("geomajas.map.store.LayerStore");

dojo.declare("CachingRemoteRasterStore", RasterStore, {

	statics : { cfgCmd: "command.render.GetRasterTiles",
        cfgReqClass: "org.geomajas.command.dto.GetRasterTilesRequest" },

	constructor : function (layer, mapWidget) {
		this.layer = layer;
		// this breaks MVC, maybe get the scale from a topic ?
		this.mapWidget = mapWidget;
		this.dispatcher = geomajasConfig.dispatcher;
		
		this.nodes = new dojox.collections.Dictionary();
	},

	/**
	 * Apply a callback function on every tile within certain bounds.
	 * @param bounds The bounds wherein to look for tiles.
	 * @callback onDelete function object. Should accept a tile as
	 *           argument.
	 * @callback onUpdate function object. Should accept a tile as
	 *           argument.
	 */
	applyAndSync : function (/*Bbox*/bounds, /*Function*/onDelete,  /*Function*/onUpdate) {
		// delete the node if it has become invisible
		if(!this.layer.getMapModel().getMapView().isPanning()){
			// delete all nodes
			log.info("deleting raster nodes");
			var e = this.nodes.getIterator();
			while(e.get()) {
		 		var node = e.element.value;
		 		onDelete(node);
			}
			this.clear();
		}

		var deferred = this._fetch(bounds, this.mapWidget.getMapView().getCurrentScale());
		deferred.addCallback(this,
			function(data){
				this.addImages(data,onUpdate);
				return data;
			}
		);	
	},
	
	getImageCount : function () {
		var nodes = this.nodes.getValueList();
		var count = 0;
		for (var i=0; i < nodes.length; i++) {
			count += nodes[i].getImages().count;
		}
		return count;
	},
	
	clear : function () {
		this.nodes.clear();
	},
		
	getNodes : function () {
		return this.nodes.getValueList();
	},
	

	/**
	 * @private
	 */
	_fetch : function (bounds, scale) {
		log.info("Fetching "+bounds);
		var command = new JsonCommand(this.statics.cfgCmd, this.statics.cfgReqClass, null, false);
		command.addParam("layerId",this.layer.layerId);
		command.addParam("crs",this.mapWidget.mapModel.getCrs());		
		command.addParam("bbox",bounds.toJSON());
		command.addParam("scale",scale);
		var deferred = this.dispatcher.execute(command);
		return deferred;	
	},

	addImages : function (result, callback) {
		var list = result.rasterData.list;
		var nodeId = this.layer.mapId+"."+result.nodeId;

		log.info("addImages : adding "+list.length+" images");			
		//get or create the group node;
		var node = this.nodes.item(nodeId);
		
		if (!node) {
			node = new RasterNode();
			node.setId(nodeId);
			this.nodes.add(node.getId(),node);
		}

		//add the images to the group node
		for (var i=0; i<list.length; i++) 
		{
			var image = this.layer.getImageFactory().createImage(list[i],this.mapWidget.mapModel.id);
			// not so good, refactor this so we at least don't have to make server calls while panning
			var t = this.layer.getMapModel().getMapView().getWorldToPanTranslation();
			image.getBounds().translate(Math.round(t.dx), Math.round(t.dy));	
			node.addImage(image);
		}
		var nodes = this.nodes.getValueList();
		for (var i=0; i< nodes.length; i++) {
			if(nodes[i].getId() == node.getId()){
				nodes[i].setVisible(true);
			}
			else {
				nodes[i].setVisible(false);
			}
			callback(nodes[i]);		
		}
	}
});