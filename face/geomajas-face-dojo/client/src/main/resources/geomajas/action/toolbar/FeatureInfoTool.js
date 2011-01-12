dojo.provide("geomajas.action.toolbar.FeatureInfoTool");
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
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.FeatureInfoController");

dojo.declare("FeatureInfoTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate feature info controller (DynamicToolbar).
	 * @class This tool activates a mode wherein the user can get the
	 * identifying attributes (see serverside config for vector layers)
	 * from a feature by clicking on it.
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
		this.tooltip = this.tooltipLocale.FeatureInfoTool;

		/** @private */
		this.controller = new FeatureInfoController(mapWidget);

		/** @private */
		this.mapWidget = mapWidget;
	},

	/**
	 * Adds a {@link FeatureInfoController} to the map.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController(this.controller);
		this.mapWidget.setCursor("help");
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