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

dojo.provide("geomajas.controller.ZoomOnClickController");
dojo.require("geomajas.event.MouseListener");
dojo.require("geomajas.spatial.transform.WorldViewTransformation");

dojo.declare("ZoomOnClickController", MouseListener, {

	/**
	 * @fileoverview MouseListener for zooming by clicking.
	 * @class MouseListener implementation for zooming on a click event.
	 * Whether it zooms in or out, depends on the given scaleFactor.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapView Reference to a MapWidget's viewing object.
	 * @param scaleFactor Determines the difference in scale.
	 */
	constructor : function (mapView, scaleFactor, name) {
		/** Reference to a MapWidget's MapView object. */
		this.mapView = mapView;
		
		/** The scale-delta. Bigger then 1 zooms in, smaller then 1 zooms out. */
		this.scaleFactor = scaleFactor;
		
		this.name = name;
	},

	/**
	 * Return a unique name for this MouseListener.
	 */
	getName : function () {
		if (this.name) {
			return this.name;
		}
		return "ZoomOnClickController";
	},

	/**
	 * Zooms in or out around the position where the user clicked.
	 * @param event A HtmlMouseEvent object.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		var transform = new WorldViewTransformation(this.mapView);

		var viewPosition = event.getPosition();
		var worldPosition = transform.viewPointToWorld(viewPosition);
		
		this.mapView.getCamera().setPosition (worldPosition.getX(), worldPosition.getY());
		this.mapView.scale(this.scaleFactor, geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CHANGE);
	}

});