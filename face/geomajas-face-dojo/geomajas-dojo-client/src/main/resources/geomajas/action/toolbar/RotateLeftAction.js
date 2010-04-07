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