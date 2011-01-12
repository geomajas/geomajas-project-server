dojo.provide("geomajas.config.ConfigManager");
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
dojo.provide("geomajas.config.ConfigManager");

dojo.require("dojox.collections.Dictionary");

/**
 * Als dit een builder pattern moet worden, kunnen we het misschien hernoemen naar ConfigDirector ofzo?
 */
dojo.declare("ConfigManager", null, {

	statics : { cfgReqClass: "org.geomajas.command.dto.GetConfigurationRequest", cfgCmd: "command.configuration.Get" },

	/**
	 * Unique id of the application
	 */
	applicationId : null,
	
	/**
	 * @class First version of a manager type object for majas configuration.
	 *
	 * @constructor
	 * @param dispatcher Command dispatcher object.
	 */
	constructor : function (dispatcher) {
		/** Command dispatcher object. */
		if(dispatcher){
			this.dispatcher = dispatcher;
		} else {
			geomajasConfig.dispatcher = new CommandDispatcher();
			this.dispatcher = geomajasConfig.dispatcher;
		}
		this.serverBase = geomajasConfig.serverBase;
		this.mapWidget = null;
		this.config = null;
		this.count = 0;
		this.toolbars = [];
	},

	getConfigWithJson : function () {
		var command = new JsonCommand(this.statics.cfgCmd, this.statics.cfgReqClass, null, false);
		command.addParam("applicationId", this.applicationId);
		var deferred = this.dispatcher.execute(command);
		deferred.addCallback(this,"configureApplication");
	},

	configureApplication : function (result) {
		try {
			log.info("configuring application "+result);
			this.count++;
			// MAPS:
			if(this.config == null){
				this.config = result.application;
			}
			var allLoaded = true;
			var maps = this.config.maps.list;
			log.info("checking "+maps.length+" map widgets");
			for (var i=0; i< maps.length; i++) {
				log.info("checking "+maps[i].id);
				var mapWidget = dijit.byId (maps[i].id);
				log.info("mapWidget="+mapWidget);
				if(typeof mapWidget != 'undefined'){ 
					if(!mapWidget.isLoaded()){
						allLoaded = false;
						break;
					}
				}
			}
			if(!allLoaded){
				log.info("waiting 50 millis for retry...");
				if(this.count < 10){
					setTimeout(dojo.hitch(this, "configureApplication"), 50);
				}
				return;
			}
			log.info("configuring application "+maps.length+" maps");
			for (var i=0; i< maps.length; i++) {
				try {
					this._configureMap(maps[i]);
				} catch (e) {
					log.error("exception !!! ");
					for (var i in e) log.error(i +":"+ e[i]);
				}			
			}

			log.info("configuration done");
			this.onConfigDone();
		} catch (e) {
			log.error("configure application error");
			for (var i in e) log.error(e[i]);
		}
		return result;			
	},

	/**
	 * @private
	 */
	_configureMap : function (mapInfo) {
		log.debug("configureMap : "+mapInfo.id);
		var mapWidget = dijit.byId (mapInfo.id);
		if (!mapWidget) {
			log.error ("configureMap : no map widget found");
			return;
		}
		this.mapWidget = mapWidget;
        // pixel width from screen dpi
        mapWidget.setPixelWidth(0.0254/this.config.screenDpi);

		// Map specific data:
		var mapModel = new MapModel (mapInfo.id, new DefaultWorkflowFactory (this.dispatcher), mapInfo.crs, mapInfo.precision, mapInfo.unitLength);
        mapWidget.setMapModel(mapModel);

		var initialBox = new Bbox();
		initialBox.set(mapInfo.initialBounds.x, mapInfo.initialBounds.y, mapInfo.initialBounds.width, mapInfo.initialBounds.height);
		// resolution list
		if(mapInfo.resolutions){
			for(var i = 0; i < mapInfo.resolutions.list.length; i++){
				log.info("resolution = "+mapInfo.resolutions.list[i]);
			}
			mapWidget.getMapView().setResolutions(mapInfo.resolutions.list);
		}
		mapWidget.getMapView().setMaximumScale(parseFloat(mapInfo.maximumScale));
		mapWidget.getGraphics().setPrecision(mapInfo.precision);
		mapWidget.getMapView().applyBbox(initialBox);
		mapWidget.setBackgroundColor (mapInfo.backgroundColor);

		var lineStyle = new ShapeStyle (mapInfo.lineSelectStyle.fillColor, mapInfo.lineSelectStyle.fillOpacity,
                mapInfo.lineSelectStyle.strokeColor, mapInfo.lineSelectStyle.strokeOpacity,
                mapInfo.lineSelectStyle.strokeWidth, mapInfo.lineSelectStyle.dashArray, mapInfo.lineSelectStyle.symbol);
		var pointStyle = new ShapeStyle (mapInfo.pointSelectStyle.fillColor, mapInfo.pointSelectStyle.fillOpacity,
                mapInfo.pointSelectStyle.strokeColor, mapInfo.pointSelectStyle.strokeOpacity,
                mapInfo.pointSelectStyle.strokeWidth, mapInfo.pointSelectStyle.dashArray,
                mapInfo.pointSelectStyle.symbol);
		var polyStyle = new ShapeStyle (mapInfo.polygonSelectStyle.fillColor, mapInfo.polygonSelectStyle.fillOpacity,
                mapInfo.polygonSelectStyle.strokeColor, mapInfo.polygonSelectStyle.strokeOpacity,
                mapInfo.polygonSelectStyle.strokeWidth, mapInfo.polygonSelectStyle.dashArray,
                mapInfo.polygonSelectStyle.symbol);
		mapWidget.setLineSelectStyle (lineStyle);
		mapWidget.setPointSelectStyle (pointStyle);
		mapWidget.setPolygonSelectStyle (polyStyle);

		if (mapInfo.scaleBarEnabled == true) {
			mapWidget.enableScaleBar();
		}
		if (mapInfo.panButtonsEnabled == true) {
			mapWidget.enablePanButtons();
		}
		if (mapInfo.displayUnitType != null) {
			mapWidget.unitType = mapInfo.displayUnitType.value;
		}
		log.debug ("configureMap : widget intialized");
		// Create all the layers
		//var layers = mapInfo.layers.list;
		var layers = mapInfo.layers.list;
		var layersById = new dojox.collections.Dictionary();
		for(var i = 0; i < layers.length; i++){
			var layer = this._createLayer(mapInfo.id, layers[i], mapWidget, mapModel);
			layersById.add(layer.getId(),layer);
            mapModel.registerLayer(layer);
		}
		
		// set maxBounds
		if (mapInfo.maxBounds) {
			var mapMaxBounds = new Bbox(mapInfo.maxBounds.x, mapInfo.maxBounds.y, mapInfo.maxBounds.width, mapInfo.maxBounds.height);
			var viewMaxBounds = mapWidget.getMapView().getMaxBounds();
			if (viewMaxBounds && viewMaxBounds.contains(mapMaxBounds))
				mapWidget.getMapView().setMaxBounds(mapMaxBounds);
		}
		
		// recursively configure the layer tree nodes
		if (mapInfo.layerTree) {
			var nodeConfig = mapInfo.layerTree.treeNode;
			log.debug ("configureMap : configuring layer tree "+mapInfo.id);
			var node = this._configureLayerTreeNodeRecursively(mapInfo.id, nodeConfig, mapModel);
			mapModel.setRootNode(node);
			node.id=mapInfo.id+".root";
			this._configureLayerTree(mapInfo);
			log.debug(node.getChildren());
			log.debug(node.getChildren().count);
		}

		log.debug ("configureMap : setting overview ref");
		if (mapInfo.overview != "" && mapInfo.overview != null) {
			log.debug("configureMap : setting overview to "+mapInfo.overview);
			mapWidget.setOverviewReference(mapInfo.overview);
		}

        log.debug ("configureMap : configuration done, trigger rendering");
        dojo.publish (mapWidget.getRenderTopic(), [mapModel, "all"]);

        // TOOLBARS:
        var bar = this._configureToolbar(mapInfo);
        if(bar != null){
            this.toolbars.push(bar);
        }
	},

	/**
	 * @private
	 */
	_configureLayerTreeNodeRecursively : function (mapId, treeNodeConfig, mapModel) {
		var node = new LayerTreeNode (treeNodeConfig.label, treeNodeConfig.label, eval(treeNodeConfig.expanded));
		// loop over layers
		var layers = treeNodeConfig.layers.list;
		for(var i = 0; i < layers.length; i++){
            var layer = mapModel.getLayerById(mapId+"."+layers[i].serverLayerId);
            if (null == layer) log.error("LayerTree referenced layer " + layers[i].serverLayerId);
			node.addChild(layer);
		}
		// or loop children and go deeper
		if (treeNodeConfig.treeNodes) {
			var children = treeNodeConfig.treeNodes.list;
			for(var i = 0; i < children.length; i++){
				node.addChild(this._configureLayerTreeNodeRecursively(mapId,children[i],mapModel));
			}
		}
		return node;
	},

	/**
	 * @private
	 */
	_createLayer : function (mapId, config, mapWidget, mapModel) {
		if (config.layerType.value == geomajas.LayerTypes.RASTER) {
			return this._configureRasterLayer(mapId, config, mapWidget, mapModel);
		} else {
			return this._configureVectorLayer(mapId, config, mapModel);
		}
	},

	/**
	 * @private
	 */
	_configureVectorLayer : function (mapId, lc, mapModel) {
        log.debug("_configureVectorLayer="+mapId+"."+lc.serverLayerId);
		var layer = new VectorLayer(mapId, lc.serverLayerId, mapModel);
		layer.setLabel (lc.label);
		layer.setLayerType (lc.layerType.value);
        layer.setVisible(lc.visible);
		layer.setMaxExtent(lc.maxExtent);
		layer.setMinViewScale(lc.viewScaleMin);
		layer.setMaxViewScale(lc.viewScaleMax);
		
		// Style:
		var style = new NamedStyleInfo();
		style.fromJSON(lc.namedStyleInfo);
		layer.setNamedStyle(style);

		// EditPermissions:
		var permissions = new EditPermissions();
		permissions.setCreatingAllowed(lc.creatable);
		permissions.setUpdatingAllowed(lc.updatable);
		permissions.setDeletingAllowed(lc.deletable);
		layer.setEditPermissions(permissions);

		// Filters:
		if (lc.layerInfo.filter) {
			layer.setFilterEnabled(true);
			layer.setFilterString(lc.layerInfo.filter);
		}

		// attributes:
		var geometryType = new GeometryType();
		geometryType.fromJSON(lc.featureInfo.geometryType);

		var view = new dojox.collections.Dictionary();
		var viewArray = lc.featureInfo.attributes.list;
		for (var j=0; j<viewArray.length; j++) {
			var at = viewArray[j];
			var atd = null;
			if(at.feature){
				atd = new AssociationDefinition (layer, at.name, at.label, at.validator, at.type.value, at.editable, at.identifying, at.hidden, at.feature);
			} else {
				atd = new AttributeDefinition (layer, at.name, at.label, at.validator, at.type.value, at.editable, at.identifying, at.hidden);
			}
			view.add (at.name, atd);
		}
		var featureInfo = new FeatureInfo(view, geometryType);
		layer.setFeatureInfo(featureInfo);

		// snappingrules:
		var srArray = lc.snappingRules.list;
		var srList = new dojox.collections.ArrayList();
		for (var j=0; j<srArray.length; j++) {
			var rule = new SnappingRule(srArray[j].distance, srArray[j].layer, srArray[j].type);
			srList.add(rule);
		}
		layer.setSnappingRules(srList);

		layer.init();
        layer.setVisible(lc.visible);
        if (lc.snappingRules.mode) {
        	layer.getSnapper().setMode(lc.snappingRules.mode);
        }
		log.info("created vector layer "+lc.serverLayerId);
		return layer;
	},

	/**
	 * @private
	 */
	_configureRasterLayer : function (mapId, lc, mapWidget, mapModel) {
		log.debug("_configureRasterLayer="+mapId+"."+lc.serverLayerId);
		var layer = new RasterLayer(mapId, lc.serverLayerId, mapWidget, mapModel);

		if (null != lc.layerInfo.dataSourceName && lc.layerInfo.dataSourceName.indexOf("@GoogleLayer") > 0) {
			log.debug("setting google image factory");
			layer.setImageFactory(new GoogleImageFactory(lc.layerInfo.dataSourceName));
		}
		layer.setLabel (lc.label);
		layer.setLayerType (lc.layerType.value);
        layer.setVisible(lc.visible);
		layer.setMaxExtent(lc.maxExtent);
		layer.setMinViewScale(lc.viewScaleMin);
		layer.setMaxViewScale(lc.viewScaleMax);
		layer.setStyle(new PictureStyle(lc.style));
		layer.init();
        layer.setVisible(lc.visible);
		return layer;
	},

	/**
	 * @private
	 */
	_configureToolbar : function (mapInfo) {
		var mapId = mapInfo.id;
		var mapWidget = dijit.byId(mapId);
		if (mapWidget != null) {
			log.info("configuring toolbar for "+mapId);
			var tbFactory = new ToolbarFactory(mapWidget, this.dispatcher, this.serverBase);
			return tbFactory.createToolbar (mapInfo.toolbar);
		}
		log.info("no toolbar configured, missing map "+mapId);
		return null;
	},

	/**
	 * @private
	 */
	_configureLayerTree : function (mapInfo) {
		var mapId = mapInfo.id;
		var mapWidget = dijit.byId (mapId);
		if (mapWidget != null) {
			log.info("configuring layertree for "+mapId);
			var ltFactory = new LayerTreeFactory(mapWidget, this.dispatcher);
			return ltFactory.createLayerTree(mapInfo.layerTree);
		}
		log.info("no layertree configured, missing map "+mapId);
		return null;
	},

	onConfigDone : function () {
		try {
			if (typeof(initFrames) != "undefined") {
				log.info("initFrames");
				initFrames();
			}
			//geomajasConfig.printManager = new PrintTemplateManager();
			dojo.forEach(this.toolbars, function(bar){ bar.onConfigDone(); });
			log.info("onConfigDone");
		} catch (e) {
			log.error("onConfigDone error");
			for (var i in e) log.error(e[i]);
		}			
	}
});
