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

dojo.provide("geomajas.map.layertree.RasterLayer");
dojo.require("geomajas.map.store.common");
dojo.require("geomajas.map.layertree.RasterImageFactory");
dojo.require("geomajas.map.layertree.LayerTreeNode");

dojo.declare("RasterLayer", [LayerTreeNode], {

	/**
	 * @fileoverview Represents a layer for raster images.
	 * @class The layers of a MapModel object are ordered as a tree, with
	 * internal nodes represented by this class, and leafs represented by one
	 * of the layer classes. In other words, the layers are also nodes in the
	 * layer tree, and therefore extend this class.
	 * 
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends LayerTreeNode
	 * @param id Unique identifier for the node.
	 * @param mapWidget Reference to the MapWidget.
	 */
	constructor : function (mapId, layerId, mapWidget, mapModel) {
		this.id = mapId + "." + layerId;
        this.mapId = mapId;
        this.layerId = layerId;
		this.mapModel = mapModel;
		this.layerType = null;
		this.visible = false;
		this.maxViewScale = null;
		this.minViewScale = null;
		this.selected = false;
		this.mapWidget = mapWidget;
		this.maxExtent = new Bbox(0,0,400000,400000);
		this.imageFactory = new RasterImageFactory(this);
		this.imageFactory.setLayer(this);
		this.style = null;
	},

	init : function () {
		this.store = new CachingRemoteRasterStore (this, this.mapWidget);
		dojo.connect(this, "setVisible", dojo.hitch(this, "_onSetVisible"));
	},

	getLayerId : function(){
		return this.layerId;
	},

	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit(this);
		if(recursive && this.checkVisibility()){
			this.store.applyAndSync (
					bbox, 
					function (node) { 
						log.info("deleting raster node "+node.getId());
						visitor.remove(node); 					
					},
					function (node) { 
						log.info("updating raster node "+node.getId());
						node.accept (visitor, bbox, true);
					}
				);
		}
	},

	checkVisibility : function () {
		this.onCheckVisibility(this);
		var scale = this.mapModel.getMapView().getCurrentScale();
		if (this.visible && scale >= this.minViewScale && scale < this.maxViewScale) {
			return true;
		}
		return false;
	},

	checkScaleVisibility : function () {
		var scale = this.mapModel.getMapView().getCurrentScale();
		if (scale >= this.minViewScale && scale < this.maxViewScale) {
			return true;
		}
		return false;
	},

	// What's this still doing here??
	isEditable : function () {
		return false;
	},

	getLayerType : function () {
		return this.layerType;
	},

	setLayerType : function (layerType) {
		this.layerType = layerType;
	},

	getStyle : function () {
		return this.style;
	},

	setStyle : function (style) {
		this.style = style;
	},

	getRasterStore : function () {
		return this.store;
	},
	
	/**
	 * Sets the maximum extent that can be visible.
	 * @param bbox The maximum extent.
	 */
	setMaxExtent : function(bbox) {
		this.maxExtent = new Bbox(bbox.x, bbox.y, bbox.width, bbox.height);
	},

	getMaxExtent : function () {
		return this.maxExtent;
	},

	getMaxViewScale : function () {
		return this.maxViewScale;
	},

	setMaxViewScale : function (maxViewScale) {
		this.maxViewScale = maxViewScale;
	},

	getMinViewScale : function () {
		return this.minViewScale;
	},

	setMinViewScale : function (minViewScale) {
		this.minViewScale = minViewScale;
	},

	setImageFactory : function (imageFactory) {
		imageFactory.setLayer(this);
		this.imageFactory = imageFactory;
	},

	getImageFactory : function () {
		return this.imageFactory;
	},

	getDefaultStyle : function () {
		return null;
	},
	
	getRasterStore : function () {
		return this.store;
	},

	/**
	 * @private
	 */
	_onSetVisible : function (visible) {
		if (this.mapWidget != null) {
			dojo.publish(this.mapWidget.getMapModel().getRenderTopic(), [this, "all"]);
		}
	},

	onCheckVisibility : function (layer){
	}
});