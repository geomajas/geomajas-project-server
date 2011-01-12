dojo.provide("geomajas.action.layertree.LayerSnappingTool");
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

dojo.declare("LayerSnappingTool", LayerTreeTool, {

	/**
	 * @fileoverview Activate/Deactivate snapping (for LayerTree widget).
	 * @class Implementation of the LayerTreeTool "interface", that
	 * determines whether or not to use snapping for a layer.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends LayerTreeTool
	 * @param layerTree The LayerTree widget this object will be added to.
	 */
	constructor : function (layerTree) {
		/** Reference to a LayerTree widget. */
		this.layerTree = layerTree;

		/** Identifier. */
		this.id = layerTree.id + ".layerSnapping";

		/** The name of the image that represents this tool. */
		this.image = "layerSnappingIcon";

		/** The tooltip string reference. */
		this.tooltip = this.tooltipLocale.LayerSnappingTool;
	},

	/**
	 * This function is called when the LTButton widget, that represents this 
	 * class in the LayerTree, is activated. In this case it will activate
	 * snapping for the selected layer.<br/>
	 * @param event The browser's mouse event.
	 */
	onSelect : function (event) {
		var layer = this.layerTree.getSelected();
		if (layer != null && layer instanceof VectorLayer) {
			var snapper = layer.getSnapper();
			snapper.setActive (true);
		}
	},

	/**
	 * This function is called when the LTButton widget, that represents this
	 * class in the LayerTree, is deactivated. In this case it will deactivate
	 * snapping for the selected layer.<br/>
	 * @param event The browser's mouse event.
	 */
	onDeSelect : function (event) {
		var layer = this.layerTree.getSelected();
		if (layer != null && layer instanceof VectorLayer) {
			layer.getSnapper().setActive (false);
		}
	},

	/**
	 * Returns true if the layer has snapping activated. False otherwise.
	 * @param layer The layer object we need to check.
	 * @returns true or false; snapping active or not. In case of "null",
	 *          false is returned.
	 */
	getSelectionByLayer : function (layer) {
		if (layer == null || !(layer instanceof VectorLayer)) {
			return false;
		}
		return layer.getSnapper().isActive();
	},

	/**
	 * Only vectorlayer that are visible and have at least 1 snapping rule
	 * defined will allow this action to be enabled.
	 */
	getEnabledByLayer : function (layer) {
		if (layer == null) {
			return false;
		} else if (!(layer instanceof VectorLayer)) {
			return false;
		} else if (!layer.isVisible()) {
			return false;
		} else if (layer.getSnappingRules() == null || layer.getSnappingRules().count == 0) {
			return false;
		}
		return true;
	}
});
