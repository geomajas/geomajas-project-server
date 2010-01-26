dojo.provide("geomajas.action.toolbar.LocationInfoTool");
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
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.LocationInfoController");

dojo.declare("LocationInfoTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate location info controller (DynamicToolbar).
	 * @class This tool activates a mode wherein the user can get info
	 * about a specific position on the map by clicking on it.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarTool
	 * @param id Unique identifier
	 * @param mapWidget Reference to the MapWidget object.
	 */
	constructor : function (id, mapWidget) {
		/** Unique identifier. */
		this.id = id;

		/** This tool's image representation. */
		this.image = "featureInfoIcon";

		/** Reference to the tooltip. */
		this.tooltip = this.tooltipLocale.LocationInfoTool;

		/** @private */
		this.controller = new LocationInfoController(mapWidget);

		/** @private */
		this.mapWidget = mapWidget;
	},

	/**
	 * Adds a {@link FeatureInfoController} to the map.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setCursor("crosshair");
		this.mapWidget.setController (this.controller);
	},

	/**
	 * Removes the {@link FeatureInfoController} from the map.
	 */
	onDeSelect : function (event) {
		this.selected = false;
		this.mapWidget.setCursor("default");
		this.mapWidget.setController(null);
		this.controller.killBalloon();
	}
});
