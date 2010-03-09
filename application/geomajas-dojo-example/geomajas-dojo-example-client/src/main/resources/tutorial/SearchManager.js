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
dojo.provide("tutorial.SearchManager");

dojo.declare("SearchManager", ConfigManager, {
	
	applicationId : "tutorial",

	/**
	 * @class First version of a manager type object for demo sample.
	 *
	 * @constructor
	 * @param dispatcher Command dispatcher object.
	 */
	constructor : function (dispatcher) {
		/** Command dispatcher object. */
		geomajasConfig.dispatcher = new CommandDispatcher ();
		this.dispatcher = geomajasConfig.dispatcher;
		this.tableInitialized = false;
	},

	onLoad : function () {
		// Get the map through configuration:
		dijit.byId("activityDiv").init(geomajasConfig.dispatcher);
		// after all connects !!!
		this.getConfigWithJson(); // Fetch the configuration for application "demo".
	},

	onConfigDone : function () {
		var mapWidget = dijit.byId("sampleSearchMap");
		mapWidget.scaleStyle.setFillColor("#C0C0C0");
		var flt = dijit.byId("searchFLT");
		flt.setMapWidget(mapWidget);
		var legend = dijit.byId("sampleSearchLegend");
		legend.setMapModel(mapWidget.getMapModel());

		// ScaleSelect in the toolbar:
		var scaleSelect = new geomajas.widget.ScaleSelect({id:"scaleWidget"}, document.createElement("div"));
		var toolbar = dijit.byId("sampleSearchToolbar");
		toolbar.addSeparator();
		toolbar.addChild(scaleSelect);
		scaleSelect.setMapWidget(mapWidget);

		var layer = mapWidget.getMapModel().getLayerById ("sampleSearchMap.countries");
		dijit.byId("loader").init(layer);
		dojo.connect(layer.featureStore.cache, "onFetchDone", this, "initTable");
		dojo.publish(mapWidget.getRenderTopic(), [layer, "all"]);

		// Automatic labelling:
		dojo.connect(mapWidget.getMapView(), "setCurrentScale", this, "onZoom");
	},

	/**
 	* Fill the FeatureListTable widget with the features of the "provinces" layer.
 	* Also make sure that it reacts to selection events.
	*/
 	initTable : function() {
		if (this.tableInitialized) {
			return;
		}
		// Add the features, and render the table:
		var map = dijit.byId("sampleSearchMap");
		var flt = dijit.byId("searchFLT");
		flt.setMapWidget(map);

		// Make the table listen to selection events:
		flt.enableSelection(map.getMapModel().getSelectionTopic()); // Listens to the selection-topic!

		var layer = dijit.byId("sampleSearchMap").getMapModel().getLayerById("sampleSearchMap.countries");
		if (layer) {
			var updateAction = new ShowTableAction(null, map, null, null);
			updateAction.refreshTable(flt, layer);
		}
	},
	
	onZoom : function(scale) {
		var mapWidget = dijit.byId("sampleSearchMap");
		var countries = mapWidget.getMapModel().getLayerById("sampleSearchMap.countries");
		if (scale > 15) {
			countries.setLabeled(true);
		} else {
			countries.setLabeled(false);
		}
	},
	
	showSearch : function () {
		if (dijit.byId("searchPanel") != null) {
			return;
		}

		var map = dijit.byId("sampleSearchMap");
		var panel = new SearchPanel({id:"searchPanel"},null);
		panel.startup();
		panel.setMapWidget (map);
		panel.initTable(null, "searchFLT");
		var div = dojo.byId(geomajasConfig.connectionPoint);
		div.appendChild(panel.domNode);
		panel.resize({ w:590, h:290, l:40, t:40 });
		panel.show();
		panel.bringToTop();
	},
	
	setFilter : function(filterString) {
		// First we need to reset some things:
		var mapWidget = dijit.byId("sampleSearchMap");
		dojo.publish(mapWidget.getMapModel().getSelectionTopic(), [ "deselectAll", null ]); // deselect everything
		var layer = mapWidget.getMapModel().getLayerById("sampleSearchMap.countries");
		layer.getFeatureStore().clear(); // Clear the feature store
		dojo.publish(mapWidget.getRenderTopic(), [ layer, "delete"]); // delete the layer on the map.

		// Only then we apply or remove the filter:
		if (filterString == null || filterString == "") {
			layer.setFilterString(null);
			layer.setFilterEnabled(false);
		} else {
			layer.setFilterString(filterString);
			layer.setFilterEnabled(true);
		}

		// And redraw the layer on the map:
		dojo.publish(mapWidget.getRenderTopic(), [ layer, "all"]);
		dijit.byId("searchFLT").reset(); // reset the table
	}

});

