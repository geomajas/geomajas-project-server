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
dojo.provide("geomajas.action.toolbar.MouseInfoTool");

dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.MouseInfoController");
dojo.requireLocalization("geomajas.action.toolbar", "mouseInfoTool");

/**
 * This tool acts as a listener tool and will attach itself to the MapWidget through mapWidget.addListener(..)
 * @author Kristof Heirwegh, Balder Van Camp
 */
dojo.declare("MouseInfoTool", ToolbarTool, {
	
	// -- i18n --
	tooltip : "",
	
	constructor : function (id, mapWidget, showViewCoords, showWorldCoords, left, right, top, bottom, opacity) {
		/** Unique identifier. */
		this.id = id;

		/** This tool's image representation. */
		this.image = "mouseInfoToolIcon";

		/** Reference to the tooltip. */
		var widgetLocale = dojo.i18n.getLocalization("geomajas.action.toolbar", "mouseInfoTool");
		this.tooltip = widgetLocale.tooltip;

		this.controller = new MouseInfoController(mapWidget, widgetLocale, showViewCoords, showWorldCoords, left, right, top, bottom, opacity);

		this.mapWidget = mapWidget;
	},

	onSelect : function (event) {
		this.selected = true;
		this.controller.createBalloon();
		this.mapWidget.addListener (this.controller);
	},

	onDeSelect : function (event) {
		this.selected = false;
		this.mapWidget.removeListener(this.controller);
		this.controller.killBalloon();
	}
});
