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

dojo.provide("geomajas.action.toolbar.ZoomInAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("ZoomInAction", ToolbarAction, {

	/**
	 * @fileoverview Zoom in (DynamicToolbar).
	 * @class This toolbar action will zoom in on click.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarAction
	 * @param id Unique identifier
	 * @param mapView The MapView object behind the MapWidget.
	 * @param delta The delta value by which to scale. Should be bigger then 1.
	 */
	constructor : function (id, mapView, delta) {
		/** Unique identifier. */
		this.id = id;

		/** This tool's image representation. */
		this.image = "zoomInIcon";

		/** Reference to the tooltip. */
		this.tooltip = this.tooltipLocale.ZoomInAction;
		
		/** MapView object to zoom on. */
		this.mapView = mapView;
		if (delta) {
			this.delta = parseFloat(delta);
		} else {
			this.delta = 2;
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