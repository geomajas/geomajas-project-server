dojo.provide("geomajas.action.layertree.LabelStyleAction");
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
dojo.require ("geomajas.action.LayerTreeAction");

dojo.declare("LabelStyleAction", LayerTreeAction, {

	/**
	 * @fileoverview Activate/Deactivate labelling (for LayerTree widget).
	 * @class Implementation of the LayerTreeTool class, for the
	 * LayerTree widget. This activesettable determines whether or not the
	 * labels for a layer should be visible or not.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends LayerTreeTool
	 * @param layerTree The LayerTree widget where this object should be
	 *                  added.
	 */
	constructor : function (layerTree) {
		/** Reference to the LayerTree widget */
		this.layerTree = layerTree;

		/** Identifier */
		this.id = layerTree.id + ".labelStyle";

		/** The name of the image that is to represent this tool. */
		this.image = "labelStyleIcon";

		/** Tooltip */
		this.tooltip = this.tooltipLocale.LabelStyleAction;
	},

	actionPerformed : function (event) {
	},
	
	getEnabledByLayer : function (layer) {
		if (layer == null || !(layer instanceof VectorLayer)) {
			return false;
		}
		return true;
	}
});
