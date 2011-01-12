dojo.provide("tutorial.AssociationManager");
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

