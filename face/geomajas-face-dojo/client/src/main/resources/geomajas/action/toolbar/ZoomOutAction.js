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

dojo.provide("geomajas.action.toolbar.ZoomOutAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("ZoomOutAction", ToolbarAction, {

	/**
	 * @fileoverview Zoom out (DynamicToolbar).
	 * @class This toolbar action will zoom out on click.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarAction
	 * @param id Unique identifier
	 * @param mapView The MapView object behind the MapWidget.
	 * @param delta The delta value by which to scale. Should be smaller then 1.
	 */
	constructor : function (id, mapView, delta) {
		/** Unique identifier. */
		this.id = id;

		/** This tool's image representation. */
		this.image = "zoomOutIcon";

		/** Reference to the tooltip. */
		this.tooltip = this.tooltipLocale.ZoomOutAction;
		
		/** MapView object to zoom on. */
		this.mapView = mapView;
		if (delta) {
			this.delta = parseFloat(delta);
		} else {
			this.delta = 0.5;
		}
	},

	/**
	 * The action's execution function. This function is called when the
	 * action's button is clicked. It changes the zoom level of the mapView 
	 * object.
	 * @param event Standard browser mouseevent.
	 */
	actionPerformed : function (event) {
		this.mapView.scale(this.delta, geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CHANGE);
	}
});