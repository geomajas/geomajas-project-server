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