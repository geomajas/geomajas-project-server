dojo.provide("tutorial.AttributesManager");
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

dojo.declare("AttributesManager", ConfigManager, {
	
	applicationId : "tutorial",
	
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
		mapWidget.getMapModel().setSelectedLayer("sampleFeaturesMap.countries110m");
		var layer = mapWidget.getMapModel().getLayerById("sampleFeaturesMap.countries110m");
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
			var layer = map.getMapModel().getLayerById("sampleFeaturesMap.countries110m");
			if (layer) {
				var updateAction = new ShowTableAction(null, map, null, null);
				updateAction.refreshTable(flt, layer);
			}
			dijit.byId("loader").hideLoader();
		}
		catch(e) {
			log.error("initTable error");
			for (var i in e) log.error(e[i]);
		}
	}
});
