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
dojo.provide("tutorial.AssociationManager");

dojo.declare("AssociationManager", ConfigManager, {
	
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
		this.lastX = 0;
		this.lastY = 0;
	},

	onLoad : function () {
		// Get the map through configuration:
		dijit.byId("activityDiv").init(geomajasConfig.dispatcher);

		dijit.byId("changeStyleFloater").minimize();
		dijit.byId("measureFloater").minimize();
		dijit.byId("attributeFloater").minimize();
		dijit.byId("tableFloater").minimize();

		// after all connects !!!
		this.getConfigWithJson(); // Fetch the configuration for application "demo".
	},

	onConfigDone : function () {
		this.inherited(arguments);
		var mapWidget = dijit.byId("beansMap");
		mapWidget.scaleStyle.setFillColor("#C0C0C0");

		// ScaleSelect in the toolbar:
		var scaleSelect = new geomajas.widget.ScaleSelect({id:"scaleWidget"}, document.createElement("div"));
		var toolbar = dijit.byId("beansToolbar");
		toolbar.addSeparator();
		toolbar.addChild(scaleSelect);
		scaleSelect.setMapWidget(mapWidget);

		dojo.connect(mapWidget.getMapModel(), "setSelectedLayer", this, "setMainMapTitle");

		var layer = mapWidget.getMapModel().getLayerById("beansMap.beans");
		dijit.byId("loader").init(layer);

		// Legend:
		var legend = dijit.byId("mainLegend");
		legend.setMapModel(mapWidget.getMapModel());

		// FeatureListTable:
		var flt = dijit.byId("mainTable");
		flt.setMapWidget(mapWidget);
	},

	/**
 	* set main map title
	*/
	setMainMapTitle : function(layerId) {
		var div = document.getElementById("mainMapTitle");
		while (div.firstChild) {
			div.removeChild(div.firstChild);
		}
		var node = null;
		if (layerId == null) {
			node = document.createTextNode("Main Map - no selected layer");
		} else {
			var mapWidget = dijit.byId("beansMap");
			var layer = mapWidget.getMapModel().getLayerById(layerId);
			if (layer != null) {
				node = document.createTextNode("Main Map - selected layer : "+layer.getLabel());
			} else{
				mapFloater.setTitle("Main Map");
				node = document.createTextNode("Main Map");
			}
		}
		div.appendChild(node);
	},
	
	openFeatureListTable : function() {
		var table = dijit.byId("tableFloater");
		this._setFloaterPosition(table);
		table.show();
		table.bringToTop();
	},

	openLayerStyle : function() {
		if (dijit.byId("stylePanel") != null) {
			return;
		}

		var map = dijit.byId("beansMap");
		var panel = new LayerStylePanel({id:"stylePanel"},null);
		panel.startup();
		panel.setMapWidget (map);
		panel.setLegend(dijit.byId("mainLegend"));
		var div = dojo.byId(geomajasConfig.connectionPoint);
		div.appendChild(panel.domNode);
		panel.resize({ w:550, h:370, l:40, t:40 });
		this._setFloaterPosition(panel);
		panel.show();
		panel.bringToTop();
	},
	
	_setFloaterPosition : function(floater){
		if (floater == null) {
			return;
		}
		floater.domNode.style.top = this.lastY;
		floater.domNode.style.left = this.lastX;

		if (this.lastX == 200) {
			this.lastX = 0;
		} else {
			this.lastX += 25;
		}
		this.lastY = this.lastX;
	}

});

