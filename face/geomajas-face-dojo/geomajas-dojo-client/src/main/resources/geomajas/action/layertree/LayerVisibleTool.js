dojo.provide("geomajas.action.layertree.LayerVisibleTool");
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
dojo.require ("geomajas.action.LayerTreeTool");

/**
 * Implementation of the LayerTreeTool "interface", that determines
 * whether or not a layer should be visible.
 */
dojo.declare("LayerVisibleTool", LayerTreeTool, {

	/**
	 * @fileoverview Make a layer visible/invisible (for LayerTree widget).
	 * @class Implementation of the LayerTreeTool "interface", that
	 * determines whether or not a layer should be visible.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends LayerTreeTool
	 * @param layerTree The LayerTree widget this object will be added to.
	 * @param topic Rendering topic of the map.
	 */
	constructor : function (layerTree, topic) {
		/** Reference to a LayerTree widget. */
		this.layerTree = layerTree;
		
		/** Identifier. */
		this.id = layerTree.id + ".layerVisible";
		
		/** The name of the image that represents this tool. */
		this.image = "layerVisibleIcon";
		
		/** The tooltip string reference. */
		this.tooltip = this.tooltipLocale.LayerVisibleTool;
		
		/** A rendering topic from a MapWidget. */
		this.topic = topic;
	},

	/**
	 * This function is called when the LTButton widget, that represents this 
	 * class in the LayerTree, is activated. In this case it will set the layer
	 * currently selected in the LayerTree to visible.<br/>
	 * Also publishes this event on the rendering topic of the MapWidget, this
	 * layer belongs to.
	 * @param event The browser's mouse event.
	 */
	onSelect : function (event) {
		var layer = this.layerTree.getSelected();
		if (layer != null && (layer instanceof VectorLayer || layer instanceof RasterLayer)) {
			if(!layer.isVisible()) {
				layer.setVisible (true);
			}
		}
	},

	/**
	 * This function is called when the LTButton widget, that represents this
	 * class in the LayerTree, is deactivated. In this case the layer, selected
	 * in the LayerTree, will be set to invisible.<br/>
	 * Also publishes this event on the rendering topic of the MapWidget, this
	 * layer belongs to.
	 * @param event The browser's mouse event.
	 */
	onDeSelect : function (event) {
		var layer = this.layerTree.getSelected();
		if (layer != null && (layer instanceof VectorLayer || layer instanceof RasterLayer)) {
			if(layer.isVisible()) {
				layer.setVisible (false);
			}
		}
	},

	/**
	 * Returns true if the layer is visible. False otherwise.
	 * @param layer The layer object we need to check for visibility.
	 * @returns true or false; visible or not. In case of "null", false is
	 *          returned.
	 */
	getSelectionByLayer : function (layer) {
		if (layer == null || !(layer instanceof VectorLayer || layer instanceof RasterLayer)) {
			return false;
		}
		return layer.isVisible();
	},

	getEnabledByLayer : function (layer) {
		if (layer == null || !(layer instanceof VectorLayer || layer instanceof RasterLayer)) {
			return false;
		}
		if (layer instanceof VectorLayer) {
			return layer.checkScaleVisibility();
		} else {
			return true;
		}
	}
});
