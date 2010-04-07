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