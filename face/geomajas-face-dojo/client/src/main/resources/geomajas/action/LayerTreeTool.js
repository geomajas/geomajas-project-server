dojo.provide("geomajas.action.LayerTreeTool");
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
dojo.require("geomajas.action.ToolbarTool");

dojo.declare("LayerTreeTool", ToolbarTool, {

	/**
	 * @fileoverview Baseclass for tools for a LayerTree widget.
	 * @class General form (i would like to say "interface", but it's
	 * javascript...) of an Tool for the LayerTree widget.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarTool
	 */
	constructor : function () {
		/** Always requires a reference to a LayerTree widget. */
		this.layerTree = null;
	},

	/**
	 * class in the LayerTree, is activated.
	 * @param event The browser's mouse event.
	 */
	onSelect : function (event) {
		alert("LayerTreeTool:onSelect : " + event);
	},

	/**
	 * This function is called when the LTButton widget, that represents this
	 * class in the LayerTree, is deactivated.
	 * @param event The browser's mouse event.
	 */
	onDeSelect : function (event) {
		alert("LayerTreeTool:onDeSelect : " + event);
	},
	
	/**
	 * When a new Layer is selected in the LayerTree widget, the LayerTree
	 * widget needs to check all it's LTButtons (and thus
	 * LayerTreeTools) whether or not they should be activated. This
	 * can be determined from the return value of this function.
	 * @param layer The layer in question.
	 * @returns true or false. True meaning the ASButton that represents this
	 *            object should be active.
	 */
	getSelectionByLayer : function (layer) {
		return true;
	},

	/**
	 * Similar to the "getSelectionByLayer" function, this function is also
	 * called when the selection in the LayerTree changes. The return value
	 * determines whether or not the LTButton that represents this object
	 * should be enabled or disabled.
	 * @param layer The layer in question.
	 * @returns true or false.
	 */
	getEnabledByLayer : function (layer) {
		if (layer == null || !(layer instanceof VectorLayer || layer instanceof RasterLayer)) {
			return false;
		}
		return true;
	},

	// Getters and setters:

	getLayerTree : function () {
		return this.layerTree;
	},
	
	setLayerTree : function (layerTree) {
		this.layerTree = layerTree;
	}
});
