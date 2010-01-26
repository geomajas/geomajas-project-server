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

dojo.provide("geomajas.action.toolbar.SelectionTool");
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.SelectionController");

dojo.declare("SelectionTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate selection mode (DynamicToolbar).
	 * @class Tool for measuring distances. Uses a SelectionController behind
	 * the scenes.
	 * @author An Buyle
	 *
	 * @constructor
	 * @extends ToolbarTool
	 * @param id Unique identifier
	 * @param mapWidget MapWidget this tool has effect on.
	 * @param supportEditing Should editing be supported (passed on to the
	 *                       {@link SelectionController}).
	 * @param selectFromActiveLayerOnly: Should  only features from the active layer be selectable?
	  *@param coverageRatio	minimum ratio of area of feature geometry that has to be inside the
	 * 							selection rectangle to select/deselect it.
	 * @param pixelTolerance Number of pixels that describes the tolerance allowed when trying to select features
	 */
	constructor : function (id, mapWidget, supportEditing, selectFromActiveLayerOnly, coverageRatio, pixelTolerance) {
		/** Unique identifier */
		this.id = id;

		/** The image for this tool's button. */
		this.image = "selectionIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.RectangleSelectionTool;
		
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;

		var supportEditingAsBool;
		var selectFromActiveLayerOnlyAsBool;

		if (dojo.isString(supportEditing)) {
			supportEditingAsBool = (supportEditing == "true");
		}
		else { 
			supportEditingAsBool = supportEditing;
		}
		
		if (selectFromActiveLayerOnly && dojo.isString(selectFromActiveLayerOnly)) {
			selectFromActiveLayerOnlyAsBool = (selectFromActiveLayerOnly == "true");
		}
		else { 
			selectFromActiveLayerOnlyAsBool = selectFromActiveLayerOnly;
		}

		if (coverageRatio) {
			if (coverageRatio > 1) {
				this.coverageRatio = 1;
			} else if (coverageRatio < 0.1) {
				this.coverageRatio = 0.1;
			}
		} else {
			this.coverageRatio = 0.7;
		}

		if (pixelTolerance) {
			this.pixelTolerance = pixelTolerance;
		} else {
			this.pixelTolerance = 5;
		}

		/** MouseListener that defines the actions for mouse-events on the map */
		this.controller = new SelectionController(mapWidget, supportEditingAsBool,
				selectFromActiveLayerOnlyAsBool, this.coverageRatio, this.pixelTolerance);		
	},

	/**
	 * Add the SelectionController to the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController(this.controller);
	},

	/**
	 * Remove the SelectionController from the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onDeSelect : function (event) {
		this.selected = false;
		this.mapWidget.setController(null);
	}
});