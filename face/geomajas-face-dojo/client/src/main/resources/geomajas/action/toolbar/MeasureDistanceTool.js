dojo.provide("geomajas.action.toolbar.MeasureDistanceTool");
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
dojo.require("geomajas.controller.SimpleMeasureDistanceController");
dojo.require("geomajas.controller.MeasureDistanceController");
dojo.require("geomajas.controller.SnappingHelpController");

dojo.declare("MeasureDistanceTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate measuring distance mode (DynamicToolbar).
	 * @class Tool for measuring distances. Uses a MeasureDistanceController
	 * behind the scenes.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarTool
	 * @param id Unique identifier
	 * @param mapWidget MapWidget this tool has effect on.
	 */
	constructor : function (id, mapWidget, simple, useSnapHelp) {
		/** Unique identifier */
		this.id = id;
		
		/** The image for this tool's button. */
		this.image = "measureDistanceIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.MeasureDistanceTool;

		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;

		/** MouseListener that defines the actions for mouse-events on the map */
		if (simple == "true") {
			this.controller = new SimpleMeasureDistanceController (this.mapWidget, this.tooltipLocale.MeasureDistanceMessage, useSnapHelp);
		} else {
			this.controller = new MeasureDistanceController (this.mapWidget, this.tooltipLocale.MeasureDistanceMessage, useSnapHelp);
		}
	},

	/**
	 * Add the MeasureDistanceController to the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController(this.controller);
	},

	/**
	 * Remove the MeasureDistanceController from the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onDeSelect : function (event) {
		this.selected = false;
		this.mapWidget.setController(null);
		this.controller.removeGraphicalContent();
	}
});