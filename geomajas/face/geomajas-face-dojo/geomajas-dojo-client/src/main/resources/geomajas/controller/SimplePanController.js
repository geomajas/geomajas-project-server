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