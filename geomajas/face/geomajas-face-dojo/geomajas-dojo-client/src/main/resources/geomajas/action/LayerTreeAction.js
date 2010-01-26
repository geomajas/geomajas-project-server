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

dojo.provide("geomajas.action.LayerTreeAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("LayerTreeAction", ToolbarAction, {

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