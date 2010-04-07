dojo.provide("geomajas.action.toolbar.PanTool");
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
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.PanController");

dojo.declare("PanTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate pan mode (DynamicToolbar).
	 * @class Tool for panning. Uses a {@link PanController} for
	 * mouse-event handling.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarTool
	 * @param id Unique identifier
	 * @param mapWidget MapWidget this tool has effect on.
	 */
	constructor : function (id, mapWidget, hideLabelsOnDrag) {
		/** Unique identifier */
		this.id = id;

		/** The image for this tool's button. */
		this.image = "panIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.PanTool;
		
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;

		/** MouseListener that defines the actions for mouse-events on the map */
		this.controller = new PanController (mapWidget, hideLabelsOnDrag);
	},

	/**
	 * Add the PanController to the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController(this.controller);
		this.mapWidget.setCursor("move");
	},

	/**
	 * Remove the PanController from the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onDeSelect : function (event) {
		this.selected = false;
		this.mapWidget.setCursor("default");
		this.mapWidget.setController(null);
	}

});
