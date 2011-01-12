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

dojo.provide("geomajas.controller.NavigateController");
dojo.require("geomajas.event.MouseListener");

dojo.declare("NavigateController", MouseListener, {

	/**
	 * @fileoverview Mouselistener for panning by clicking.
	 * @class MouseListener implementation for panning by clicking. Places the
	 * camera in the clicked coordinates.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget Reference to the MapWidget.
	 */
	constructor : function (mapWidget) {
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "NavigateController";
	},

	/**
	 * Sets the camera on the clicked position. This will cause the map to redraw.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			var view = this.mapWidget.getMapView();
			var point = event.getPosition();
			var trans = new WorldViewTransformation(view);
			var worldPoint = trans.viewPointToWorld(point);
			view.setCenterPosition(worldPoint);
		}
	}
});