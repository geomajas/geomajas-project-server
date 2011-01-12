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

dojo.provide("geomajas.widget.experimental.Profiler");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojox.grid.Grid");
dojo.require("dojox.grid._data.model");

dojo.declare("geomajas.widget.experimental.Profiler", [dijit._Widget, dijit._Templated], {

	widgetsInTemplate : true,

	templatePath : dojo.moduleUrl("geomajas.widget.experimental", "html/Profiler.html"),

	/** The grid holding the profiling results. */
	grid : null,

	/** The array of profiler data. */
	data : null,

	/** The map that is to be profiled. */
	mapWidget : null,
	
	/** Total nr of layers to fetch */
	layerFetchCount : 0,
	
	/** Precise moment the fetching of data started. */
	startTime : null,

	postCreate : function () {
		// Initialize the grid:
		this.grid = dijit.byId(this.id + ":grid");
		if (this.grid) {
			var thead = [];

			thead.push({ name: "Nr tiles",    width: "20%" });
			thead.push({ name: "Nr points",   width: "20%" });
			thead.push({ name: "Cache size",  width: "20%" });
			thead.push({ name: "Server time", width: "20%" });
			thead.push({ name: "Total time",  width: "20%" });

			var view = { cells: [ thead ]};
			this.grid.setStructure([ view ]);

			this.data = [];
			this.grid.setModel(null);
		}

		var clearBTN = dijit.byId(this.id + ":clear");
		if (clearBTN != null) {
			clearBTN.onClick = dojo.hitch(this, "_clear");
		}
		var layoutBTN = dijit.byId(this.id + ":layout");
		if (layoutBTN != null) {
			layoutBTN.onClick = dojo.hitch(this, "_layout");
		}
		this.layout();
	},

	layout : function () {
		var container = dijit.byId(this.id+":container");
		container.layout();
		var pane = dijit.byId(this.id + ":gridPane");
		pane.startup();
	},

	/**
	 * This widget is initialized with the layer to which it must connect to
	 * the onFetchDone event. 
	 */
	init : function (mapWidget) {
		if (mapWidget != null) {
			this.mapWidget = mapWidget;
			var layers = mapWidget.getMapModel().getLayerList();
			for (var i=0; i<layers.count; i++) {
				var layer = layers.item(i);
				if (layer.getLayerType() != geomajas.LayerTypes.RASTER) {
					this.connect(layer.getFeatureStore().cache, "onFetchDone", dojo.hitch(this, "_layerFetched"));
					this.connect(layer.getFeatureStore().cache, "onTileFetching", dojo.hitch(this, "_onTileFetching"));
					this.connect(layer.getFeatureStore().cache, "onTileFetched", dojo.hitch(this, "_onTileFetched"));
				}
			}
			this.connect (mapWidget.getMapModel(), "accept", dojo.hitch(this, "_onRenderStart"));
		}
		this.layout();
	},


	//-------------------------------------------------------------------------
	// Private functions.
	//-------------------------------------------------------------------------

	/**
	 * @private
	 * Called when the clear button is clicked. Clears the grid.
	 */
	_clear : function () {
		if (this.grid) {
			this.data = [];
			var model = new dojox.grid.data.Table(null, this.data);
			this.grid.setModel(model);
			this.grid.render();
		}
	},
	
	/**
	 * @private
	 * Connected to the fetching of a single tile. Needed to get the correct nr of tiles.
	 */
	_onTileFetching : function () {
		this.nrTiles++;
	},

	/**
	 * @private
	 * Connect to the returning of a tile. Needed to get the server execution time.
	 * This execution time on the server does not include the parsing from and to JSON.
	 */
	_onTileFetched : function (executionTime) {
		this.serverTime += executionTime;
	},

	/**
	 * @private
	 */
	_onRenderStart : function () {
		this.layerFetchCount = 0;
		var layers = this.mapWidget.getMapModel().getLayerList();
		for (var i=0; i<layers.count; i++) {
			var layer = layers.item(i);
			if (layer.getLayerType() != geomajas.LayerTypes.RASTER && layer.checkVisibility()) {
				this.layerFetchCount++;
				log.debug ("Got to fetch layer "+layer.getId());
			}
		}
		this.startTime = new Date();
		log.debug ("Starting to fetch " + this.layerFetchCount + " layers.");

		this.nrTiles = 0;
		this.nrPoints = 0;
		this.cacheSize = 0;
		this.serverTime = 0;
	},

	/**
	 * @private
	 */
	_layerFetched : function (layerId) {
		var layer = this.mapWidget.getMapModel().getLayerById(layerId);
		if (layer != null && layer.checkVisibility()) {
			this.layerFetchCount--;
			log.debug ("Layer "+layerId+" has been fetched: "+this.layerFetchCount+" to go.");

			// Updating cache size:
			this.cacheSize += layer.getFeatureStore().getElementCount();
			
			// Updating nr points:
			var features = layer.getFeatureStore().getElements().getValueList();
			for (var i=0; i<features.length; i++) {
				this.nrPoints += features[i].getGeometry().getNumPoints();
			}
		}

		// Fetching done - add a row to the grid:
		if (this.layerFetchCount == 0) {

			// Calculating total time:
			var endTime = new Date();
			var totalTime = endTime.getTime() - this.startTime.getTime();

			// Now add this row:
			var row = [this.nrTiles, this.nrPoints, this.cacheSize, this.serverTime, totalTime];
			this.data.push(row);
			if (this.grid && this.mapWidget) {
				var model = new dojox.grid.data.Table(null, this.data);
				this.grid.setModel(model);
				this.grid.render();
			}
		}
	}
});