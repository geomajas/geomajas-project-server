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

dojo.provide("geomajas.action.toolbar.ZoomOutTool");
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.ZoomOnClickController");

dojo.declare("ZoomOutTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate zooming out by clicking (DynamicToolbar).
	 * @class Tool for zooming out. Uses a ZoomOnClickController for mouse
	 * event handling.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarTool
	 * @param id The identifier. Will become the button's name.
	 * @param mapWidget The MapWidget this activeSetteble has effect on.
	 * @param zoomFactor The zooming delta. Must be smaller then 1.
	 */
	constructor : function (id, mapWidget, delta) {
		/** Unique identifier */
		this.id = id;

		/** The image for this tool's button. */
		this.image = "zoomOutIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.ZoomOutTool;
		
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