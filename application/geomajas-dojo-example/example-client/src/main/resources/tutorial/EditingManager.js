dojo.provide("tutorial.EditingManager");
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

dojo.declare("EditingManager", ConfigManager, {
	
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
	},

	onLoad : function () {
		// Get the map through configuration:
		dijit.byId("activityDiv").init(geomajasConfig.dispatcher);
		// after all connects !!!
		this.getConfigWithJson(); // Fetch the configuration for application "demo".
	},

	onConfigDone : function () {
		this.inherited(arguments);
		var mapWidget = dijit.byId("sampleEditingMap");
		var legend = dijit.byId("legend");
		legend.setMapModel(mapWidget.getMapModel());
		var layer = mapWidget.getMapModel().getLayerById ("sampleEditingMap.lakes110m");
		dijit.byId("loader").init(layer);
	},

	selectManualPage : function (page) {
		dijit.byId('edit_manual').selectChild('edit_manual:quick_help');
		dijit.byId('edit_manual:container').selectChild(page);
		var anim = dojo.animateProperty({ 
			node: "edit_manual:container", 
			duration: 500,
			properties: { 
				backgroundColor:	{ start: "#668822", end: "#FFFFFF" },
				color: 				{ start: "#668822", end: "#000000" } 
			} 
		});
		anim.play();
	}

});

