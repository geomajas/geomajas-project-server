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
dojo.provide("tutorial.AttributesManager");

dojo.declare("AttributesManager", ConfigManager, {

	/**
	 * @class First version of a manager type object for attributes sample.
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
		this.getConfigWithJson(); // Fetch the configuration
	},

	onConfigDone : function () {
		this.inherited(arguments);
 		// Attach the legend to the map (this should be done from xml)
		var legend = dijit.byId("sampleFeaturesLegend");
		var mapWidget = dijit.byId("sampleFeaturesMap");
		legend.setMapModel(mapWidget.getMapModel());
		mapWidget.getMapModel().setSelectedLayer("sampleFeaturesMap.provinces");
		var layer = mapWidget.getMapModel().getLayerById("sampleFeaturesMap.provinces");
		layer.setLabeled(true);
		dojo.connect(layer.featureStore.cache, "onFetchDone", this, "initTable");
		dijit.byId("loader").init(layer);
		dojo.publish(mapWidget.getRenderTopic(), [layer, "all"]);

		// ScaleSelect in the toolbar:
		var scaleSelect = new geomajas.widget.ScaleSelect({id:"scaleWidget"}, document.createElement("div"));
		var toolbar = dijit.byId("sampleFeaturesMapToolbar");
		toolbar.addSeparator();
		toolbar.addChild(scaleSelect);
		scaleSelect.setMapWidget(mapWidget);
	},

	/**
 	* Fill the FeatureListTable widget with the features of the "provinces" layer.
 	* Also make sure that it reacts to selection events.
	*/
 	initTable : function() {
		try {
			if (this.tableInitialized) {
				return;
			}
			var flt = dijit.byId("sampleFeaturesFLT");
			var map = dijit.byId("sampleFeaturesMap");
			// Make the table listen to selection events:
			flt.enableSelection(map.getMapModel().getSelectionTopic()); // Listens to the selection-topic!

			// Add the features, and render the table:
			flt.setMapWidget(map);
			var layer = map.getMapModel().getLayerById("sampleFeaturesMap.provinces");
			if (layer) {
				flt.setLayer(layer); // Needed for the table's header.
				var fs = layer.getFeatureStore();
				var el = fs.getElements(); // Get all features.
				var features = el.getValueList();
				for (var i=0; i<features.length; i++) {
					var feature = features[i];
					flt.addFeature(feature); // Add them to the table one by one.
					this.tableInitialized = true;
				}
			flt.render(); // Render the table!
			}
			dijit.byId("loader").hideLoader();
		}
		catch(e) {
			log.error("initTable error");
			for (var i in e) log.error(e[i]);
		}
	}

});

