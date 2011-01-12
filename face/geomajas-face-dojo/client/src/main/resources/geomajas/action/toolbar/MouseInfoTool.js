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
