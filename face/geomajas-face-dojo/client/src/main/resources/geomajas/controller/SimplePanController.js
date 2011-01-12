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

dojo.provide("geomajas.controller.SimplePanController");
dojo.require("geomajas.event.MouseListener");

dojo.declare("SimplePanController", MouseListener, {

	/**
	 * @fileoverview MouseListener for a MapWidget's panbuttons.
	 * @class MouseListener implementation for panning.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapView Reference to the MapView on which to pan.
	 * @param panDelta {@link Coordinate} object that determines the normalized delta.
	 */
	constructor : function (mapView, /*Coordinate*/panDelta) {
		/** Reference to the MapWidget. */
		this.mapView = mapView;
		this.panDelta = panDelta;
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "SimplePanController";
	},

	/**
	 * Simply click to move by the predetermined delta.
	 */
	mouseClicked : function (event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			var bbox = this.mapView.getCurrentBbox();
			var w = bbox.getWidth() / 2;
			var h = bbox.getHeight() / 2;

			this.mapView.translate(this.panDelta.getX()*w, this.panDelta.getY()*h);
			event.stopPropagation();
		}
	},

	mousePressed : function (event) {
		event.stopPropagation();
	}
});