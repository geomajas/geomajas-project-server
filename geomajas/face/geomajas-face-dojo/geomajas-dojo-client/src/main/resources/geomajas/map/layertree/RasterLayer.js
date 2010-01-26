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
		this.maxTileLevel = 10;
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

	/**
	 * Sets the maximum tiling level for a pyramid structure.
	 * @param maxTileLevel The maximum tiling level.
	 */
	setMaxTileLevel : function (maxTileLevel) {
		this.maxTileLevel = maxTileLevel;
	},

	getMaxTileLevel : function () {
		return this.maxTileLevel;
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