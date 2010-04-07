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
			var layer = this.mapModel.getSelectedLayer ();
			if (layer == null) {
				log.error ("FeatureEditor:startEditing => geen laag geselecteerd! (knop zou disabled moeten zijn)");
				return null;
			}
			newFeatures = [new Feature ()];
			newFeatures[0].setLayer(layer);
			if (layer.getLayerType() == geomajas.LayerTypes.POINT) {
				newFeatures[0].setGeometry (this.factory.createPoint(null));
			} else if (layer.getLayerType() == geomajas.LayerTypes.LINESTRING) {
				newFeatures[0].setGeometry (this.factory.createLineString(null));
			} else if (layer.getLayerType() == geomajas.LayerTypes.MULTILINESTRING) {
				var lineString = this.factory.createLineString(null);
				newFeatures[0].setGeometry (this.factory.createMultiLineString([lineString]));
			} else if (layer.getLayerType() == geomajas.LayerTypes.POLYGON) {
				var ring = this.factory.createLinearRing(null);
				newFeatures[0].setGeometry (this.factory.createPolygon(ring, null));
			} else if (layer.getLayerType() == geomajas.LayerTypes.MULTIPOLYGON) {
				var ring = this.factory.createLinearRing(null);
				var poly = this.factory.createPolygon(ring, null);
				newFeatures[0].setGeometry (this.factory.createMultiPolygon([poly]));
			}
			this.featureTransaction = new FeatureTransaction(null, newFeatures, layer, this.mapModel.getCrs());
		} else if (newFeatures == null || newFeatures.length == 0) { // DELETE (oldFeatures will not be empty, because that would get us in the first 'if')
			this.featureTransaction = new FeatureTransaction(oldFeatures, null, oldFeatures[0].getLayer(), this.mapModel.getCrs());
		} else { // EDIT EXISTING
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
	}

});