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

dojo.provide("geomajas.action.toolbar.ShowDefaultPrintAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("ShowDefaultPrintAction", ToolbarAction, {

	/**
	 * @fileoverview Shows the default PrintWidget.
	 * @class This toolbar action will show a DefaultPrintWidget instance. 
	 * 
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarAction
	 * @param id Unique identifier
	 * 
	 */
	constructor : function (id, mapWidget, downloadMethod, pageSize) {
		/** Unique identifier */
		this.id = id;

		/** The CSS class for the action image. */
		this.image = "showDefaultPrintIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.ShowDefaultPrintAction;

		this.text = "Fetch PDF";
		
		this.mapWidget = mapWidget;
		
		this.printMap = null;
		
		this.dialog = null;
		
		/** The method a pdf is downloaded, inline (0) or save-as (1) **/
		if(downloadMethod) {
			this.downloadMethod = downloadMethod;
		} else {
			this.downloadMethod = 0;
		}

		/** The page size **/
		this.pageSize = pageSize;
		
		this.floater = null;
	},

	/**
	 * The action's execution function. This function is called when the
	 * action's button is clicked.
	 * @param event Standard browser mouseevent.
	 */
	actionPerformed : function (event) {
		if (!dijit.byId("defaultPrintFloater")) {
			this.floater = new geomajas.widget.FloatingPane({
				id:"defaultPrintFloater",
				title: "Print",
				dockable: true,
				maxable: false,
				closable: true,
				resizable: true
			}, null);
			this.floater.startup();
			var div = dojo.body();
			if (geomajasConfig.connectionPoint) {
				var div = dojo.byId(geomajasConfig.connectionPoint);
			}
			div.appendChild (this.floater.domNode);
		}
		this.floater.resize({ w:400, h:250, l:200, t:40 });
		this.floater.show();
		this.floater.setContent ("<div dojoType=\"geomajas.widget.DefaultPrintWidget\" id=\"defaultPrinter\"></div>");
		var printer = dijit.byId("defaultPrinter");
		printer.setSourceMap(this.mapWidget);
		this.floater.bringToTop();
	},

	getText : function () {
		return this.text;
	}
});