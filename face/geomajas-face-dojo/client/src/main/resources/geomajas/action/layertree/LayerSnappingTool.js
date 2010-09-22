dojo.provide("geomajas.action.layertree.LayerSnappingTool");
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
