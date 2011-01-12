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

dojo.provide("geomajas.map.editing.FeatureEditor");
dojo.require("geomajas._base");

dojo.declare("FeatureEditor", null, {

	/**
	 * @fileoverview Main object responsible for editing features.
	 * @class Main class responsible for editing features. This class stores
	 * one {@link FeatureTransaction} object that represents the edited
	 * feature(s). Furthermore, it only has base functions like startEditing
	 * and stopEditing. The actual editing steps are controlled by the
	 * {@link EditingController} and executed by {@link EditCommand} objects.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param mapModel Reference to the {@link MapModel} object we are editing on.
	 */
	constructor : function (mapModel) {
		this.mapModel = mapModel;
		this.editMapLayerMode = true; /* editing a layer of the map, not used for drawing-only */
		this.layerType = null;
		
		this.featureTransaction = null;
		this.factory = new GeometryFactory(
			mapModel.getSRID(),
			mapModel.getPrecision());
	},

	/**
	 * Create a new FeatureTransaction object.
	 */
	startEditing : function (oldFeatures, newFeatures) {
		if (oldFeatures == null || oldFeatures.length == 0) { // NEW FEATURE
			var layerType = this.layerType;
			var layer = null;
			
			if (this.editMapLayerMode) {
				layer = this.mapModel.getSelectedLayer ();
				if (layer == null) {
					log.error ("FeatureEditor:startEditing => geen laag geselecteerd! (knop zou disabled moeten zijn)");
					return null;
				}
				layerType = layer.getLayerType(); 
			}
			else { /* not editing a layer of the map, used for drawing-only */
				/* Create dummy vector layer (only layerType attribute is used) */
				layer =  new VectorLayer("DummyDrawMap"/*mapId*/, "DummyDrawLayer"/*serverLayerId*/, null/*mapModel*/);
				layer.setLayerType (layerType);
			}
			
			newFeatures = [new Feature ()];
			newFeatures[0].setLayer(layer);
			if (layerType== geomajas.LayerTypes.POINT) {
				newFeatures[0].setGeometry (this.factory.createPoint(null));
			} else if (layerType == geomajas.LayerTypes.LINESTRING) {
				newFeatures[0].setGeometry (this.factory.createLineString(null));
			} else if (layerType == geomajas.LayerTypes.MULTILINESTRING) {
				var lineString = this.factory.createLineString(null);
				newFeatures[0].setGeometry (this.factory.createMultiLineString([lineString]));
			} else if (layerType == geomajas.LayerTypes.POLYGON) {
				var ring = this.factory.createLinearRing(null);
				newFeatures[0].setGeometry (this.factory.createPolygon(ring, null));
			} else if (layerType == geomajas.LayerTypes.MULTIPOLYGON) {
				var ring = this.factory.createLinearRing(null);
				var poly = this.factory.createPolygon(ring, null);
				newFeatures[0].setGeometry (this.factory.createMultiPolygon([poly]));
			}
			this.featureTransaction = new FeatureTransaction(null, newFeatures, layer, this.mapModel.getCrs());
		} else if (newFeatures == null || newFeatures.length == 0) { // DELETE (oldFeatures will not be empty, because that would get us in the first 'if')
			if (!this.editMapLayerMode) {
				log.error ("FeatureEditor:startEditing => a delete is not possible when not in editMapLayerMode! (action should be disabled)");
				return null;
			}
			this.featureTransaction = new FeatureTransaction(oldFeatures, null, oldFeatures[0].getLayer(), this.mapModel.getCrs());
		} else { // EDIT EXISTING
			if (!this.editMapLayerMode) {
				log.error ("FeatureEditor:startEditing => edit an existing feature is not possible when not in editMapLayerMode! (action should be disabled)");
				return null;
			}
			this.featureTransaction = new FeatureTransaction(oldFeatures, newFeatures, oldFeatures[0].getLayer(), this.mapModel.getCrs());
		}
		this.onFeatureTransactionChanged();
		return this.featureTransaction;
	},

	/**
	 * Remove the FeatureTransaction object.
	 */
	stopEditing : function (featureTransaction) {
		this.featureTransaction = null;
		this.onFeatureTransactionChanged();
	},

	/**
	 * Undo last editing command.
	 */
	undoLast : function (featureTransaction) {
		var stack = featureTransaction.getCommandStack();
		var features = featureTransaction.getNewFeatures();
		var command = stack.pop();
		if (command != null) {
			command.undo(features);
		}
		featureTransaction.setCommandStack(stack);
	},

	/**
	 * Undo all steps.
	 * TODO: not yet implemented!
	 */
	undoAll : function (featureTransaction) {
	},

	// Getters:

	/**
	 * This function is often called to see if editing is busy or not.
	 * If this returns "null" there is no feature being edited.
	 */
	getFeatureTransaction : function () {
		return this.featureTransaction;
	},

	/**
	 *  event for feature transaction changes.
	 */
	onFeatureTransactionChanged : function () {
	},
	
	/**
	 *  @param editMapLayerMode: if true set mode to editing a layer of the map, if false used for drawing-only
	 *  @param geomType: desired geometry type if used for drawing-only
	 */	
	setEditMapLayerMode : function (editMapLayerMode, geomType) {
		this.editMapLayerMode = editMapLayerMode;
		if (!editMapLayerMode) {
			this.layerType = geomType;
		}
		else {
			this.layerType = null;
		}
	}

});