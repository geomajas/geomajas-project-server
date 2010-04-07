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

dojo.provide("geomajas.action.toolbar.SplitPolygonTool");
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.editing.SplitPolygonController");

dojo.declare("SplitPolygonTool", ToolbarTool, {

	constructor : function (id, mapWidget) {
		/** Unique identifier */
		this.id = id;

		/** This tool's image representation. A pencil. */
		this.image = "splitPolygonIcon";

		/** Reference to the tooltip. */
		this.tooltip = this.tooltipLocale.SplitPolygonTool;

		/** The controller used during editing mode. */
		this.controller = new SplitPolygonController(mapWidget);
		
		/** @private */
		this.mapWidget = mapWidget;
	},

	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController(this.controller);
	},

	onDeSelect : function (event) {
		var cancelAction = new CancelSplittingAction(this.controller.getMenuId()+".cancel", this.mapWidget);
		cancelAction.actionPerformed(event);		

		this.selected = false;
		this.mapWidget.setController(null);
	}
});