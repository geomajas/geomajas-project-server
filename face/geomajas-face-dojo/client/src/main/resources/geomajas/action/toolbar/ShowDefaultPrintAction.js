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
				dockable: false,
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
		this.floater.resize({ w:400, h:230, l:200, t:60 });
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