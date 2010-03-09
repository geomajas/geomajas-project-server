dojo.provide("geomajas.map.MapModel");
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
dojo.require("dojox.collections.Dictionary");
dojo.require("dojox.collections.ArrayList");

dojo.require("geomajas.gfx.PainterVisitable");
dojo.require("geomajas.gfx.PainterVisitor");

dojo.declare("MapModel", PainterVisitable, {

	/**
	 * @fileoverview  The data model behind a MapWidget.
	 * @class The model behind a MapWidget. This model contains layers, which
	 * in turn contain features. They are all visitable by a PainterVisitor.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends PainterVisitable
	 */
	constructor : function (id, workflowFactory, crs, precision, unitLength) {
		this.workflowFactory = workflowFactory;
		this.id = id;
		this.crs = crs;
		this.srid = parseInt(this.crs.split(":").reverse())
		this.precision = precision;
        this.unitLength = unitLength;
		this.selectionTopic = null;
		this.editingTopic = null;
		this.renderTopic = null;
		this.featureEditor = new FeatureEditor(this);		
		this.rootNode = null;
		this.layers = new dojox.collections.Dictionary();
		this.layerOrder = new dojox.collections.ArrayList();
		
		this.maxBounds = null;

		/** @private */
		this.mapView = null;
		this.nrDeferred = 0;
		this.renderHandlers = [];

		this.paintableObjects = new dojox.collections.Dictionary();
	},

	registerLayer : function (layer) {
		layer.setMapModel(this);
		if (layer.getLayerType() != geomajas.LayerTypes.RASTER) {
			layer.setWorkflowHandler(new WorkflowHandler(this.workflowFactory));
		}
		this.layers.add(layer.getId(),layer);
		this.layerOrder.add(layer);

		if (layer instanceof VectorLayer) {
			if (this.maxBounds == null) {
				this.maxBounds = layer.getMaxExtent();
			} else {
				this.maxBounds = this.maxBounds.union(layer.getMaxExtent());
			}
			if (this.mapView != null) {
				this.mapView.setMaxBounds(this.maxBounds);
			}
		}
	},
	
	destroy : function () {
		this.layers = null;
		this.layerOrder = null;
		if (this.renderHandlers != null) {
			for (var i=0; i < this.renderHandlers.length; i++) {
				dojo.disconnect(this.renderHandlers[i]);
			}
		}
		this.renderHandlers = null;
	},

    /**
     * Get the maps Crs.
     * @return Returns the crs.
     */
    getCrs : function () {
        return this.crs;
    },

    /**
     * Get the unit length must be expressed in meters per
     * unit (of a map's CRS).
     */
    getUnitLength : function () {
        return this.unitLength;
    },
    

	// PainterVisitable implementation:

	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit (this);

		var layers = this.layerOrder;
		// always do the layers to accommodate for mapview changes
		// TODO: need more control on the depth of the visit ???
		if(recursive){
			for (var i=0; i<layers.count; i++) {
				var layer = layers.item(i);
				if (layer.checkVisibility(this.mapView.getCurrentScale())) {
					layer.accept(visitor, bbox, recursive);
					if (layer instanceof VectorLayer) {
						this.nrDeferred++;
					}
				} else {
					// JDM: paint the top part of the layer, if not we loose the map order !!!!!
					layer.accept(visitor, bbox, false);
				}
			}			
		}

		// Paint the paintables:
		if (this.paintableObjects != null && this.paintableObjects.count != 0) {
			var keys = this.paintableObjects.getKeyList();
			for (var i=0; i<keys.length; i++) {
				var object = this.paintableObjects.item(keys[i]);
				object.accept(visitor, bbox, recursive);
			}
		}

		// Paint editing feature:
		if (this.featureEditor.getFeatureTransaction() != null) {
			this.featureEditor.getFeatureTransaction().accept(visitor, bbox, false);
		}
	},

	/**
	 * This function is automatically triggered when all the vector layers of 
	 * this map have been rendered.
	 */
	onMapRendered : function (mapId) {
	},

	/**
	 * Enables selection of features, by subscribing to a selection-topic.
	 * @param mapId Name of the map for which selection is meant. The name of
	 *              the selection topic is derived from it.
	 */
	enableSelection : function (/*String*/mapId) {
		this.selectionTopic = mapId + "/selection";
		dojo.subscribe(this.selectionTopic, this, "_onSelectionChange");
	},

	/**
	 * Creates an editing topic, this is necessary if editing is to work
	 * correctly. Should be executed before layers are added!!!!
	 * @param mapId Name of the map for which editing is meant. The name of
	 *              the editing topic is derived from it.
	 */
	enableEditing : function (/*String*/mapId) {
		this.editingTopic = mapId + "/editing";
		dojo.subscribe(this.editingTopic, this, "_onEditingChange");
		this.workflowFactory.setEditingTopic (this.editingTopic);
	},

	/**
	 * Set a newly selected layer. Only one layer can be selected at a time, so
	 * this function first tries to deselected the currently selected (if there
	 * is one).
	 * @param layerId The identifier of the layer to select. If there is no
	 *                layer with this id, nothing will be done. Unless layerId
	 *                equals null, in which case any selected layer will be deselected.
	 */
	setSelectedLayer : function (layerId) {
		if (layerId == null) {
			var selLayer = this.getSelectedLayer();
			if (selLayer != null) {
				selLayer.setSelected(false);
			}
		} else if (this.getLayerById(layerId) != null){
			var selLayer = this.getSelectedLayer();
			if (selLayer != null) {
				selLayer.setSelected(false);
			}
			var layer = this.getLayerById(layerId);
			layer.setSelected (true);
		}
	},

	/**
	 * Searches for the selected layer, and returns it.
	 * @return Returns the selected layer object, or null if none is selected.
	 */
	getSelectedLayer : function () {
		if (!this.rootNode || null == this.rootNode) return null;
		return this._recursiveGetSelection(this.rootNode);
	},

	/**
	 * @private
	 */
	_recursiveGetSelection : function (node) {
		if ((node instanceof VectorLayer || node instanceof RasterLayer) && node.isSelected()) {
			return node;
		}
		for (var i=0; i<node.getChildren().count; i++) {
			var layer = this._recursiveGetSelection(node.getChildren().item(i));
			if (layer != null) {
				return layer;
			}
		}
		return null;
	},

	/**
	 * @private
	 */
	_onSelectionChange : function (event, feature) {
		if (event != null) {
			if (feature != null) {
				var layer = feature.getLayer();
				if (layer != null && layer instanceof VectorLayer) {
					if (event == "select") {
						layer.selectFeature(feature);
						dojo.publish(this.selectionTopic, [ "addElement", feature ]);
					} else if (event == "deselect"){
						layer.deselectFeature(feature);
						dojo.publish(this.selectionTopic, [ "removeElement", feature ]);
					} else if (event == "toggle") {
						if (feature.isSelected()) {
							layer.deselectFeature(feature);
							dojo.publish(this.selectionTopic, [ "removeElement", feature ]);
						} else {
							layer.selectFeature(feature);
							log.info("added "+feature.getId()+" to selection store");
							dojo.publish(this.selectionTopic, [ "addElement", feature ]);
						}
					}
				}
			} else if (event == "deselectAll") {
				var layers = this.getOrderedLayers();
				for (var i=0; i < layers.count; i++) {
					var layer = layers.item(i);
					if (layer instanceof VectorLayer) {
						var selection = layer.getSelectionStore().getValueList();
						for (var j=0; j< selection.length; j++) {
							dojo.publish(this.selectionTopic, [ "deselect", selection[j] ]);
						}
//						var selection = layer.getSelectionStore().getKeyList();
//						for (var j=0; j< selection.length; j++) {
//							var feature = layer.getFeatureStore().getFeatureById(selection[j]);
//							dojo.publish(this.selectionTopic, [ "deselect", feature ]);
//						}
					}
				}
			}
			if (event == "requestSelection" && feature != null) { // hmm, not quite right....
				var layer = feature.getLayer();
				if (layer instanceof VectorLayer) {
					var selection = layer.getSelectionStore().getValueList();
					for (var j=0; j< selection.length; j++) {
						dojo.publish(this.selectionTopic, [ "addElement", selection[j] ]);
					}
//					var selection = layer.getSelectionStore().getKeyList();
//					for (var j=0; j< selection.length; j++) {
//						var feature = layer.getFeatureStore().getFeatureById(selection[j]);
//						dojo.publish(this.selectionTopic, [ "addElement", feature ]);
//					}
				}
			}
		}
	},

	/**
	 * @private
	 */
	_onEditingChange : function (event, ft) {
		// clear the stores
		if (event != null) {
			log.info("MapModel:_onEditingChange : received "+event);
			if (event == "commit") {
				if (ft == null) {
					return;
				}
				// Set the layer property for each feature:
				if (ft.getOldFeatures() != null) {
					var layer = ft.getLayer();
					for (var i=0; i<ft.getOldFeatures().length; i++) {
						ft.getOldFeatures()[i].setLayer(layer);
					}
				}
				if (ft.getNewFeatures() != null) {
					var layer = ft.getLayer();
					for (var i=0; i<ft.getNewFeatures().length; i++) {
						ft.getNewFeatures()[i].setLayer(layer);
					}
				}

				// Now change the mapmodel:
				if (ft.getOldFeatures() == null && ft.getNewFeatures() != null) { // NEW
					log.info("MapModel:_onEditingChange : new feature");
					for (var i=0; i<ft.getNewFeatures().length; i++) {
						var feature = ft.getNewFeatures()[i];
						var layer = feature.getLayer();
						if (layer) {
							// layer.getFeatureStore().addElement(feature);
							layer.getFeatureStore().clear();
						}
					}
				} else if (ft.getOldFeatures() != null && ft.getNewFeatures() == null) { // DELETE
					log.info("MapModel:_onEditingChange : deleting feature");
					for (var i=0; i<ft.getOldFeatures().length; i++) {
						var feature = ft.getOldFeatures()[i];
						dojo.publish (this.selectionTopic, [ "deselect", this.getFeatureById(feature.getId()) ]);
						var layer = feature.getLayer();
						if (layer) {
							// layer.getFeatureStore().removeElement(feature);
							layer.getFeatureStore().clear();
						}
					}
				}
				// EDIT:
				else if (ft.getOldFeatures() != null && ft.getNewFeatures() != null && ft.getOldFeatures().length == 1 && ft.getNewFeatures().length == 1) {
					log.info("MapModel:_onEditingChange : updating feature");
					for (var i=0; i<ft.getOldFeatures().length; i++) {
						var feature = ft.getNewFeatures()[i];
						log.info("MapModel:_onEditingChange : deselecting feature");
						dojo.publish (this.selectionTopic, [ "deselect", this.getFeatureById(feature.getId()) ]);
						log.info("MapModel:_onEditingChange : replacing feature");
						var layer = feature.getLayer();
						if (layer) {
							//layer.getFeatureStore().removeElement(ft.getOldFeatures()[i]);
							//layer.getFeatureStore().addElement(feature);
							layer.getFeatureStore().clear();
						}
					}
				}
				// Split Polygon:
				else if (ft.getOldFeatures() != null && ft.getNewFeatures() != null && ft.getOldFeatures().length == 1 && ft.getNewFeatures().length > 1){ // EDIT : TODO: if nr oldfeatures and nr newfeatures are not equal, this fails....
					log.info("MapModel:_onEditingChange : split polygon");
					var oldFeature = ft.getOldFeatures()[0];
					dojo.publish (this.selectionTopic, [ "deselect", oldFeature ]);
					var layer = oldFeature.getLayer();
					//layer.getFeatureStore().removeElement(oldFeature);
					layer.getFeatureStore().clear();

					for (var i=0; i<ft.getNewFeatures().length; i++) {
						var feature = ft.getNewFeatures()[i];
						var layer = feature.getLayer();
						if (layer) {
							//layer.getFeatureStore().addElement(feature);
							layer.getFeatureStore().clear();
						}
					}
				}
				// Merge polygons:
				else if(ft.getOldFeatures() != null && ft.getNewFeatures() != null && ft.getOldFeatures().length > 1 && ft.getNewFeatures().length == 1) {
					log.info("MapModel:_onEditingChange : merge polygon");
					for (var i=0; i<ft.getOldFeatures().length; i++) {
						var feature = ft.getOldFeatures()[i];
						log.info("MapModel:_onEditingChange : old feature "+feature.getId());
						dojo.publish (this.selectionTopic, [ "deselect", feature ]);
						var layer = feature.getLayer();
						if (layer) {
							log.info("MapModel:_onEditingChange : removing feature  "+feature.getId());
							//layer.getFeatureStore().removeElement(feature);
							layer.getFeatureStore().clear();
						}
					}

					var newFeature = ft.getNewFeatures()[0];
					log.info("MapModel:_onEditingChange : new feature "+ft.getNewFeatures()[0].getId());
					var layer = newFeature.getLayer();
					if (layer) {
						log.info("MapModel:_onEditingChange : adding new feature");
						//layer.getFeatureStore().addElement(newFeature);
						layer.getFeatureStore().clear();
					}
				}
			}
		}
		log.info("MapModel:_onEditingChange : rendering all");
		dojo.publish(this.getRenderTopic(), [ this, "all"]);
		
	},

	/**
	 * TODO: functie uit klasse Feature gebruiken!!
	 * @private
	 */
	_getLayerIdFromTarget : function (target) {
		if (target != null) {
			var layerId = "";
			var count = 0;
			for (var i=0; i<target.length; i++) {
				var c = target.charAt(i);
				if (c == '.') {
					count++;
					if (count == 2) {
						return layerId;
					}
				}
				layerId += c;
			}
		}
		return null;
	},
	
	getId : function () {
		return this.id;
	},
	
	getRootNode : function () {
		return this.rootNode;
	},

	setRootNode : function (rootNode) {
		this.rootNode = rootNode;
	},
	
	setRenderTopic : function (renderTopic) {
		this.renderTopic = renderTopic;
	},

	// Retrieval:
	
	getSelectionTopic : function () {
		return this.selectionTopic;
	},

	getEditingTopic : function () {
		return this.editingTopic;
	},
	
	getRenderTopic : function () {
		return this.renderTopic;
	},

	getLayerCount : function () {
		return this.layerOrder.count;
	},

	/**
	 * Must be the entire id: <map>.<layer>.<feature>
	 */
	getFeatureById : function (/*String*/id) {
		if (id != null) {
			var layerId = this._getLayerIdFromTarget(id);
			var layer = this.getLayerById(layerId);
			if (layer != null && layer instanceof VectorLayer) {
				var fid = null;
				var ok = false;
				while (!ok) {
					var pos = id.lastIndexOf(".");
					if (pos > 0) {
						fid = id.substring(pos+1);
					}
					if (fid != "use" && fid != "label" && fid != "text") { 
						ok = true;
					} else {
						id = id.substring(0, pos);
					}
				}
				return layer.getFeatureStore().getFeatureById(fid);
			}
		}
		return null;
	},

	/**
	 * Must be the entire id: <map>.<layer>.<feature>
	 */
	isFeatureSelected : function (/*String*/id) {
		var layerId = this._getLayerIdFromTarget(id);
		var layer = this.getLayerById(layerId);
		if (layer != null && layer instanceof VectorLayer) {
			var fid = null;
			var ok = false;
			while (!ok) {
				var pos = id.lastIndexOf(".");
				if (pos > 0) {
					fid = id.substring(pos+1);
				}
				if (fid != "use") { 
					ok = true;
				} else {
					id = id.substring(0, pos);
				}
			}
			return layer.getSelectionStore().contains(fid);
		}
		return false;
	},

	applyOnFeatureReference : function (featureReference, callback){
		this.featureReference = featureReference; // awful, I know... no time for anything cleaner atm...
		if (featureReference != null && featureReference.getPosition() != null) {
			this.callback = callback;

			// Create the list of layer ID's:
			var layerIds = [];
			if (featureReference.getLayerIds() == null){
				var list = this.getOrderedLayers();
				for (var i=list.count-1; i>=0; i--){
					var layer = list.item(i);
					if (layer.checkVisibility()){
						layerIds.push(layer.getLayerId());
					}
				}
			} else {
				layerIds = featureReference.getLayerIds();
			}

			// Create the location geometry: 
			var trans = new WorldViewTransformation(this.mapView);
			var factory = new GeometryFactory(this.srid, null);
			var worldPos = trans.viewPointToWorld(featureReference.getPosition());
			var point = factory.createPoint(worldPos);

			// Set-up the search command:
			var command = new JsonCommand("command.feature.SearchByLocation",
                    "org.geomajas.command.dto.SearchByLocationRequest", null, false);
            command.addParam ("crs", this.crs);
			command.addParam ("layerIds", layerIds);
			command.addParam ("location", point);
			command.addParam ("searchType", featureReference.getSearchType());
			command.addParam ("queryType", featureReference.getQueryType());
			command.addParam ("ratio", featureReference.getRatio());
			command.addParam ("featureIncludes", featureReference.getFeatureIncludes());
			if (featureReference.getBuffer() > 0) {
				command.addParam ("buffer", featureReference.getBuffer());
			}

			// Execute search!
			var deferred = geomajasConfig["dispatcher"].execute(command);
			deferred.addCallback(this, "_onGetFeatureByCoordinate");
		}
	},
	
	_onGetFeatureByCoordinate : function (result){
		if (result != null && result.featureMap != null && result.featureMap.map != null) {
			var features = [];
			for (var key in result.featureMap.map) {  /* iterate over each layer */
				var featureArray = result.featureMap.map[key].list;
				for (var i = 0; i< featureArray.length; i++) {  /* iterate over all features of curr layer */
					var feature = new Feature();
					feature.setLayer(this.getLayerById(this.id + "." + key));
					feature.fromJSON(featureArray[i]);
					try {
						this.callback(feature);
					} catch (e) {
						log.error ("MapModel.applyOnFeatureReference : error executing callback on feature "+feature.getId()+".");
					}
					features.push(feature);
				}
			}
			this.featureReference.onSetFeatureResult(features);
		}
	},

	/**
	 * Add a new paintable object to be drawn in world space. Make sure there
	 * is a painter registered that is able to draw this object!
	 */
	addPaintableObject : function (name, object) {
		if (!this.paintableObjects.contains(name)) {
			this.paintableObjects.add(name, object);
			dojo.publish (this.renderTopic, [this, "all"]);
		}
	},

	/**
	 * Remove a paintable object from the list. It is also immediatly deleted
	 * from the map.
	 */
	removePaintableObject : function (name) {
		var object = this.paintableObjects.item(name);
		if (object != null) {
			dojo.publish (this.renderTopic, [object, "delete"]);
			this.paintableObjects.remove(name);
		}
	},

	/**
	 * Retrieve the list of paintable objects. The MapModelPainter needs this
	 * to draw these paintable objects.
	 */
	getPaintableObjects : function () {
		return this.paintableObjects;
	},

	/**
	 * The total number of selected features in all layers of this mapmodel.
	 */
	getSelectionCount : function () {
		return this._recursiveSelectionCount (this.rootNode, 0);
	},
	
	getSelection : function () {
		return this._recursiveSelection (this.rootNode, new dojox.collections.ArrayList());
	},
	
	getLayerById : function (/*String*/id) { 
		return this.layers.item(id);
	},

	getLayerByLabel : function (/*String*/label) {
		var layers = this.getLayerList();
		for (var i=0; i<layers.count; i++) {
			if (layers.item(i).getLabel() == label){
				return layers.item(i);
			}
		}
		return null;
	},

	getLayerList : function () {
		return this._recursiveLayerList(this.rootNode, new dojox.collections.ArrayList(), false);
	},

	getVisibleLayerList : function () {
		return this._recursiveLayerList(this.rootNode, new dojox.collections.ArrayList(), true);
	},

	getOrderedLayers : function () { 
		return this.layerOrder;
	},

	getLayerByFeatureId : function (/*String*/id) { 
		var layerId = this._getLayerIdFromTarget(id);
		return this.getLayerById (layerId);
	},
	
	getFeatureEditor : function () {
		return this.featureEditor;
	},

	getMapView : function () {
		return this.mapView;
	},

	setMapView : function (mapView) {
		this.mapView = mapView;
		mapView.setSRID(this.getSRID());
		mapView.setPrecision(this.getPrecision());
		mapView.setMaxBounds(this.maxBounds);
	},
	
	getSRID : function () {		
		return this.srid;
	},


	getPrecision : function () {
		return this.precision;
	},
	
	/**
	 * @private
	 */
	_recursiveSelectionCount : function (node, count) {
		for (var i=0; i<node.getChildren().count; i++) {
			count = this._recursiveSelectionCount(node.getChildren().item(i), count);
		}
		if (node instanceof VectorLayer) {
			count += node.getSelectionStore().count;
		}
		return count;
	},

	/**
	 * @private
	 */
	_recursiveSelection : function (node, list) {
		for (var i=0; i<node.getChildren().count; i++) {
			list = this._recursiveSelection(node.getChildren().item(i), list);
		}
		if (node instanceof VectorLayer) {
			var temp = node.getSelectionStore().getValueList();
			for (var i=0; i< temp.length; i++) {
				list.add(temp[i]);
			}
//			var temp = node.getSelectionStore().getKeyList();
//			for (var i=0; i< temp.length; i++) {
//				list.add(node.getFeatureStore().getFeatureById(temp[i]));
//			}
		}
		return list;
	},

	/**
	 * @private
	 */
	_recursiveLayerList : function (node, list, visibleOnly) {
		if (node != null) {
			for (var i=0; i<node.getChildren().count; i++) {
				list = this._recursiveLayerList(node.getChildren().item(i), list, visibleOnly);
			}
			if (node instanceof VectorLayer || node instanceof RasterLayer) {
				if (visibleOnly) {
					if (node.checkVisibility()){
						list.add (node);
					}
				} else {
					list.add (node);
				}
			}
		}
		return list;
	}
});
