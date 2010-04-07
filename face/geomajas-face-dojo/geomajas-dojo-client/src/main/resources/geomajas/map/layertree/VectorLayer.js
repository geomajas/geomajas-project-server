dojo.provide("geomajas.map.layertree.VectorLayer");
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
dojo.require("dojox.collections.Dictionary");
dojo.require("dojox.collections.ArrayList");

dojo.require("geomajas._base");
dojo.require("geomajas.map.store.common");
dojo.require("geomajas.map.workflow.WorkflowHandler");
dojo.require("geomajas.map.layertree.LayerTreeNode");
dojo.require("geomajas.gfx.PainterVisitor");

dojo.declare("VectorLayer", [LayerTreeNode], {

	/**
	 * @fileoverview Represents a layer for vector data.
	 * @class Main layer object definition. Subject to change!
	 * @author Pieter De Graef & Jan De Moerloose
	 *
	 * @constructor
	 * @extends PainterVisitable
	 * @param id Unique layer identifier.
	 */
	constructor : function (mapId, layerId, mapModel) {
		this.id = mapId + "." + layerId;
        this.layerId = layerId;
		this.mapModel = mapModel;
		this.layerType = null;
		this.maxViewScale = null;
		this.minViewScale = null;
		this.selected = false;
		this.visible = false;
		this.labeled = false;
		this.editPermissions = null;

		this.namedStyle = null;
		this.featureType = null;

		this.snappingRules = new dojox.collections.ArrayList();

		this.workflowHandler = new WorkflowHandler();

		// Filters:
		this.filter = null;
		this.filterEnabled = false;
		this.maxExtent = new Bbox(0,0,400000,400000);

		this.featureStore = null;
		this.selectionStore = null;
		this.snapper = null;
		this.defaultLineStyle = new ShapeStyle("#FFFFFF", "0", "#0033FF", "1", "0.15%", null, null);
		this.defaultPolygonStyle = new ShapeStyle("#C0C0C0", "0.6", "#909090", "1", "0.15%", null, null);
		this.defaultPointStyle = new ShapeStyle(null, null, null, null, null, null, "");
	},

	init : function () {
		this.featureStore = new SpatialCacheStore(this);
		this.selectionStore = new dojox.collections.Dictionary();
		this.snapper = new Snapper(this, this.mapModel);

		dojo.connect(this, "setVisible", dojo.hitch(this, "_onSetVisible"));
		dojo.connect(this, "setLabeled", dojo.hitch(this, "_onSetLabeled"));
	},

	addStyle : function (/*FeatureStyleInfo*/ style) {
		this.namedStyle.addStyle(style);
	},
	
	replaceStyle : function (style){
		this.namedStyle.replaceStyle(style);
	},

	getStyleById : function (styleId) {
		var s = this.namedStyle.getStyleById(styleId);
		if(s != null) {
			return s.getStyle();
		} else {
			return this.getDefaultStyle();
		}
	},
	
	getLabelFontStyle : function() {
		return this.namedStyle.getLabelStyle().getFontStyle();
	},
	
	getLayerId : function(){
		return this.layerId;
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
	
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		// Problems with symbols:
		// They change in size a bit, until zooming leads to a new tile-level.

		// Original tile-list (SpatialCodes), for later comparison:
		var spatialCodes = this.featureStore.cache.getNodeList();
		log.info("original spatialCodes : "+spatialCodes);

		// Draw layer-specific stuff (see VectorLayerPainter)
		visitor.visit (this);

		// When visible, take care of fetching through an applyOnBounds.
		if (recursive && this.checkVisibility()) {
			var filter = null;
			if(this.filterEnabled){
				filter = this.filter;
			}
			this.featureStore.applyAndSync (
				bbox, 
				filter, 
				function (tile) { 
					var features = tile.getFeatures();
					for (var i=0; i < features.length; i++) {
						if(features[i].isSelected()){
							visitor.remove(features[i]);
						}
					}
					visitor.remove(tile); 					
				},
				function (tile) { tile.accept (visitor, bbox, true); }
			);
		}
	},
	
	// Getters and setters:

	getFeatureType : function () {
		return this.featureType;
	},

	setFeatureInfo : function (featureType) {
		this.featureType = featureType;
	},

	getLayerType : function () {
		return this.layerType;
	},

	setLayerType : function (/*Integer*/layerType) {
		this.layerType = layerType;
	},

	/**
	 * Sets the maximum extent that can be visible.
	 * @param extent The maximum extent.
	 */
	setMaxExtent : function (bbox) {
        this.maxExtent = new Bbox(bbox.x, bbox.y, bbox.width, bbox.height);
	},

	getMaxExtent : function () {
		return this.maxExtent;
	},

	getMaxViewScale : function () {
		return this.maxViewScale;
	},

	setMaxViewScale : function (/*Double*/maxViewScale) {
		this.maxViewScale = maxViewScale;
	},

	getMinViewScale : function () {
		return this.minViewScale;
	},

	setMinViewScale : function (/*Double*/minViewScale) {
		this.minViewScale = minViewScale;
	},

	getLabelStyle : function () {
		return this.namedStyle.getLabelStyle();
	},

	setLabelStyle : function (labelStyle) {
		this.namedStyle.setLabelStyle(labelStyle);
	},
	
	getEditPermissions : function () {
		return this.editPermissions;
	},
	
	setEditPermissions : function (editPermissions) {
		this.editPermissions = editPermissions;
	},

	isLabeled : function () {
		return this.labeled;
	},

	setLabeled : function (/*Boolean*/labeled) {
		this.labeled = labeled;
	},

	getSnappingRules : function () {
		return this.snappingRules;
	},

	setSnappingRules : function (snappingRules) {
		this.snappingRules = snappingRules;
	},

	getNamedStyle : function () {
		return this.namedStyle;
	},

	setNamedStyle : function (namedStyle) {
		this.namedStyle = namedStyle;
	},
	
	getStyles : function () {
		return this.namedStyle.getStyles();
	},

	getWorkflowHandler : function () {
		return this.workflowHandler;
	},

	setWorkflowHandler : function (workflowHandler) {
		this.workflowHandler = workflowHandler;
	},

	getFeatureStore : function () {
		return this.featureStore;
	},

	getSelectionStore : function () {
		return this.selectionStore;
	},
	
	selectFeature : function (feature) {
		// FeatureStore does not always contain the features from selection, so keep it.
		this.selectionStore.add(feature.getId(),feature.clone());
	},
	
	deselectFeature : function (feature) {
		this.selectionStore.remove(feature.getId());
	},	

	getRelativeId : function () {
		return this.id.substring(this.id.lastIndexOf(".")+1);
	},

	getSnapper : function () {
		return this.snapper;
	},

	getDefaultStyle : function () {
		switch(this.getLayerType()){
			case geomajas.LayerTypes.LINESTRING:
			case geomajas.LayerTypes.MULTILINESTRING:
				return this.defaultLineStyle;
			case geomajas.LayerTypes.POLYGON:
			case geomajas.LayerTypes.MULTIPOLYGON:
				return this.defaultPolygonStyle;	
			default:
				return this.defaultPointStyle;
		}
	},

	// Filters:

	setFilterString : function (filter) {
		this.filter = filter;
		if (this.featureStore != null) {
			this.featureStore.clear();
		}
	},

	getFilterString : function () {
		return this.filter;
	},

	isFilterEnabled : function () {
		return this.filterEnabled;
	},

	setFilterEnabled : function (filterEnabled) {
		if (filterEnabled != this.filterEnabled && this.featureStore != null) {
			this.featureStore.clear();
		}
		this.filterEnabled = filterEnabled;
	},

	// Private functions:

	/**
	 * @private
	 */
	_onSetVisible : function(visible) {
		if (this.mapModel != null) {
			dojo.publish(this.mapModel.getRenderTopic(), [this, "all"]);
		}
	},

	/**
	 * @private
	 */
	_onSetLabeled : function (labeled) {
		if (this.mapModel != null) {
			dojo.publish(this.mapModel.getRenderTopic(), [this,"all"]);
		}
	},
	
	onCheckVisibility : function (layer){
	}
});
