dojo.provide("geomajas.action.menu.ToggleSelectionAction");
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
dojo.declare("ToggleSelectionAction", Action, {

	/**
	 * @fileoverview Toggle a feature's selection state (rightmouse menu).
	 * @class Action that toggles selection for a MouseEvent.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Action
	 * @param id The action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 * @param singleSelection Boolean that determines whether or not multiple
	 *                        selections are possible. 
	 * @param priorityToSelectedLayer: Boolean; should  only features from the active layer be selectable?
	 * @paran pixelTolerance Number of pixels that describes the tolerance allowed when trying to select features
	 */
	constructor : function (id, mapWidget, singleSelection, priorityToSelectedLayer, pixelTolerance) {
		/** @private */
		this.mapWidget = mapWidget;

		/** If this is true, only one feature can be selected at a time. */
		this.singleSelection = singleSelection;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed by this image. */
		this.image = "";

		/** The action can be displayed as text only. */
		this.text = "Toggle Selection";

		/** Is the action enabled or not? */
		this.enabled = true;

		if (pixelTolerance) {
			this.pixelTolerance = pixelTolerance;
		} else {
			this.pixelTolerance = 5;
		}

		if (priorityToSelectedLayer) {
			this.priorityToSelectedLayer = priorityToSelectedLayer;
		} else {	
			this.priorityToSelectedLayer = false;
		}
		this.currSelectFromActiveLayerOnly = this.selectFromActiveLayerOnly;

		this.transform = new WorldViewTransformation(this.mapWidget.getMapView());
	},

	/**
	 * Toggle selection for the feature clicked upon.
	 * @param event Standard browser mouseevent.
	 */
	actionPerformed : function (event) {
		// Create a feature reference object. By default this will search all visible layers.
		var featureReference = new FeatureReference({event:event});
		featureReference.setBuffer(this._calculateBufferFromTolerance());

		// If we need to check for the selected layer:
		var layer = this.mapWidget.getMapModel().getSelectedLayer();
		if (this.priorityToSelectedLayer && layer != null && layer instanceof VectorLayer) {
			if (!layer.checkVisibility()) {
				return;
			}

			// Proceed only if a layer is selected, and if it is visible:
			if (layer != null && layer.checkVisibility()){
				// Change the list of layers to search in: selected layer only!
				featureReference.setLayerIds([layer.getLayerId()]);
			}
		}

		// Execute the search, and call _applyOnFeature for features found:
		this.mapWidget.getMapModel().applyOnFeatureReference(featureReference, dojo.hitch(this, "_applyOnFeature"));

	},

	_applyOnFeature: function (feature){
		if (this.singleSelection) {
			if (feature.isSelected()) {
				dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "deselect", feature ]);
			} else {
				dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "deselectAll", null ]);
				dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "select", feature ]);
			}
		} else {
			dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "toggle", feature ]);
		}
	},

	_calculateBufferFromTolerance : function () {
		var c1 = this.transform.viewPointToWorld(new Coordinate(0, 0));
		var c2 = this.transform.viewPointToWorld(new Coordinate(this.pixelTolerance, 0));
		var line = new LineSegment(c1, c2);
		return line.getLength();
	}
});
