dojo.provide("geomajas.action.toolbar.MergePolygonTool");
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
dojo.require("geomajas.controller.editing.MergePolygonController");

dojo.declare("MergePolygonTool", ToolbarTool, {

	constructor : function (id, mapWidget) {
		/** Unique identifier */
		this.id = id;

		/** This tool's image representation. A pencil. */
		this.image = "mergePolygonIcon";

		/** Reference to the tooltip. */
		this.tooltip = this.tooltipLocale.MergePolygonTool;

		/** The controller used during editing mode. */
		this.controller = new MergePolygonController(mapWidget);
		
		/** @private */
		this.mapWidget = mapWidget;
	},

	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController (this.controller);
	},

	onDeSelect : function (event) {
		this.selected = false;
		this.mapWidget.setController(null);

		var cancelAction = new CancelSplittingAction(this.controller.getMenuId()+".cancel", this.mapWidget);
		cancelAction.actionPerformed(event);		
	}
});