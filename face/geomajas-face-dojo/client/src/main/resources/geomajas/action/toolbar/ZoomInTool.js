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

dojo.provide("geomajas.action.toolbar.ZoomInTool");
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.ZoomOnClickController");

dojo.declare("ZoomInTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate zooming in by clicking (DynamicToolbar).
	 * @class Tool for zooming in. Uses a ZoomOnClickController for mouse-event
	 * handling.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends ToolbarTool
	 * @param id The identifier. Will become the button's name.
	 * @param mapWidget The MapWidget this has effect on.
	 * @param zoomFactor The zooming delta. Must be greater then 1.
	 */
	constructor : function (id, mapWidget, delta) {
		/** Unique identifier */
		this.id = id;

		/** The image for this tool's button. */
		this.image = "zoomInIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.ZoomInTool;
		
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;

		/** MouseListener that defines the actions for mouse-events on the map */
		this.controller = new ZoomOnClickController (mapWidget.getMapView(), parseFloat(delta), id);
	},

	/**
	 * Add the ZoomOnClickController to the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController (this.controller);
	},

	/**
	 * Remove the ZoomOnClickController from the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onDeSelect : function (event) {
		this.selected = false;
		this.mapWidget.setController(null);
	}

});