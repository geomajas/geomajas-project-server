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

dojo.provide("geomajas.action.toolbar.RotateLeftAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("RotateLeftAction", ToolbarAction, {

	/**
	 * @fileoverview Rotate counterclockwise (DynamicToolbar).
	 * @class This toolbar action will rotate the map to the left.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarAction
	 * @param id Unique identifier
	 * @param mapView The MapView object behind the MapWidget.
	 * @param angle The angle by which to rotate (radial).
	 */
	constructor : function (id, mapView, angle) {
		/** Unique identifier. */
		this.id = id;

		/** This tool's image representation. */
		this.image = "rotateLeftIcon";

		/** Reference to the tooltip. */
		this.tooltip = this.tooltipLocale.RotateLeftAction;
		
		/** MapView object to rotate on. */
		this.mapView = mapView;
		if (angle) {
			this.angle = angle;
		} else {
			this.angle = 0.3;
		}
	},

	/**
	 * The action's execution function. This function is called when the
	 * action's button is clicked. It changes the zoom level of the mapView 
	 * object.
	 * @param event Standard browser mouseevent.
	 */
	actionPerformed : function (event) {
		this.mapView.rotate(this.angle);
	}
});