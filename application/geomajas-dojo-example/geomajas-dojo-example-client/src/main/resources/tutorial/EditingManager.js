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
dojo.provide("tutorial.EditingManager");

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
		var layer = mapWidget.getMapModel().getLayerById ("sampleEditingMap.structures");
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

