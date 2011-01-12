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

dojo.provide("geomajas.controller.ZoomOnScrollController");
dojo.require("geomajas.event.MouseScrollListener");

dojo.declare("ZoomOnScrollController", MouseScrollListener, {

	/**
	 * @fileoverview MouseScrollListener for zooming by scrolling the mousewheel.
	 * @class Controller that zooms a map in and out by scrolling the
	 * mousewheel. Since a scroll-event is captured at the highest level
	 * (meaning at the window object), only one scroll-listener at a time
	 * is recommended. These events are caught anywhere in the browser, not
	 * just above the map you want zooming on.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param mapView The MapView object on which we want to zoom.
	 * @param zoomInDelta The delta zooming factor for zooming in.
	 *                  Determines how much zooming is done by each scroll.
	 * @param zoomOutDelta The delta zooming factor for zooming out.
	 *                  Determines how much zooming is done by each scroll.
	 */
	constructor : function (mapView, zoomInDelta, zoomOutDelta) {
		/** Reference to the MapView object on which we apply zooming. */
		this.mapView = mapView;

		this.zoomInDelta = zoomInDelta;

		this.zoomOutDelta = zoomOutDelta;
	},
	
	/**
	 * Returns a unique name.
	 */
	getName : function () {
		return "ZoomOnScrollController";
	},

	/**
	 * Function called when the user uses the scrollwheel. Zooms in or out,
	 * depending on the direction.
	 * @param event The HtmlMouseEvent object.
	 */
	mouseScrolled : function (/*HtmlMouseEvent*/event) {
		var direction = event.getScrollDirection();
		if (direction == 1) {
			this.mapView.scale(this.zoomInDelta, geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CHANGE);
		} else if (direction == -1) {
			this.mapView.scale(this.zoomOutDelta, geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CHANGE);
		}
	}
});