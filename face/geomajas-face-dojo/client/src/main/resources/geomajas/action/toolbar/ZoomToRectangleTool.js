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

dojo.provide("geomajas.action.toolbar.ZoomToRectangleTool");
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.ZoomToRectangleController");

dojo.declare("ZoomToRectangleTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate zooming to a rectangle (DynamicToolbar).
	 * @class Tool for zooming to a drawn rectangle. Uses a
	 * ZoomToRectangleController for mouse-event handling.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarTool
	 * @param id Unique identifier
	 * @param mapWidget MapWidget this tool has effect on.
	 */
	constructor : function (id, mapWidget) {
		/** Unique identifier */
		this.id = id;

		/** The image for this tool's button. */
		this.image = "zoomToRectangleIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.ZoomToRectangleTool;
		
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;

		/** MouseListener that defines the actions for mouse-events on the map */
		this.controller = new ZoomToRectangleController (mapWidget);
	},

	/**
	 * Add the ZoomToRectangleController to the MapWidget. Also changes the
	 * mouse cursor.
	 * @param event Standard browser mouse-event.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController(this.controller);
//		this.mapWidget.setCursor("crosshair");
	},

	/**
	 * Remove the ZoomToRectangleController from the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onDeSelect : function (event) {
		this.selected = false;
//		this.mapWidget.setCursor("default");
		this.mapWidget.setController(null);
	}

});
