dojo.provide("geomajas.action.layertree.ShowTableAction");
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
dojo.require("geomajas.action.LayerTreeAction");

dojo.declare("ShowTableAction", LayerTreeAction, {

	/**
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends LayerTreeAction
	 */
	constructor : function (layerTree, mapWidget, selectTabFunction, tableID) {
		if (layerTree){
			this.id = layerTree.id + ".ShowTableAction";
		} else {
			this.id = mapWidget.id + ".ShowTableAction";
		}

		this.layerTree = layerTree;
		this.mapWidget = mapWidget;

		/** The image for this tool's button. */
		this.image = "showTableIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.ShowTableAction;

		/** Name of the feature table widget. */
		if (tableID) {
			this.featureTable = tableID;
		} else {
			this.featureTable = "mainTable"; // default in this gegis module...
		}

		this.selectTabFunction = selectTabFunction;
	},

	actionPerformed : function (event) {
		var layer;
		if (this.layerTree != null) {
			layer = this.getLayerTree().getSelected();
		} else {
			layer = this.mapWidget.getMapModel().getSelectedLayer();
		}
		if (layer != null && layer instanceof VectorLayer) {
			var table = dijit.byId(this.featureTable);
			if (table) {
				refreshTable(table, layer);
			}
		}
	},

	refreshTable: function(table, layer) {
		try {
			table.enableSelection(this.mapWidget.getMapModel().getSelectionTopic()); // Listens to the selection-topic!
		} catch(e){}
		table.setLayer(layer);

		var fs = layer.getFeatureStore();
		var el = fs.getElements(); // All elements
		this.features = el.getValueList();

		// First we synchronize all features in bulk (much faster then one-by-one)
		this._synchronizeFeatures(layer.layerId);

		// Now they have their attributes, so we can add them to the table efficiently.
		for (var i=0; i<this.features.length; i++) {
			var feature = this.features[i];
			table.addFeature (feature); // Add them to the table one by one.
		}
		if (this.selectTabFunction) {
			this.selectTabFunction(); // Table should render automatically...
		}
		table.render();
	},

	getEnabledByLayer : function (layer) {
		if (layer == null || !(layer instanceof VectorLayer)) {
			return false;
		}
		return true;
	},
	
	_synchronizeFeatures : function (layerId) {
		if (this.features.length > 0) {
			var crs = this.features[0].crs;
			var command = new JsonCommand("command.feature.Search",
					"org.geomajas.command.dto.SearchFeatureRequest", null, true);
			command.addParam("layerId", layerId);
			command.addParam("crs", crs);
			command.addParam("booleanOperator", "OR");
			var criteria = [];
			for (var i=0; i<this.features.length; i++) {
				criteria.push({
					javaClass : "org.geomajas.layer.feature.SearchCriterion",
					attributeName : "$id",
					operator : "=",
					value : this.features[i].getId()
				});
			}
			command.addParam("criteria", criteria);
			command.addParam("featureIncludes", 1); // 1=attributes, 2=geometry
			var deferred = geomajasConfig.dispatcher.execute(command);
			deferred.addCallback(this, "_synchCallback");
		}
	},

	_synchCallback : function (result) {
		// build map of indexes of features
		var map = {};
		for (var i=0; i<this.features.length; i++) {
			map[this.features[i].getId()] = i;
		}
		// update attributes in features
		var feature;
		if (result.features) {
			for (var i=0; i<result.features.length; i++) {
				feature = result.features[i];
				this.features[map[feature.id]].setAttributes(feature.attributes.map);
			}
		}
	},
	
	setSelectTabFunction : function (/*callback*/ selectTabFunction) {
		this.selectTabFunction = selectTabFunction;
	},
	
	getSelectTabFunction : function () {
		return this.selectTabFunction;
	}
});
