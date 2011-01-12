dojo.provide("geomajas.action.layertree.LayerVisibleTool");
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
