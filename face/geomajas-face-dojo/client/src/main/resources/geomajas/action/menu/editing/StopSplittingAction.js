dojo.provide("geomajas.action.menu.editing.StopSplittingAction");
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
dojo.require("geomajas.action.Action");

dojo.declare("StopSplittingAction", Action, {

	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Stop inserting";
	},

	actionPerformed : function (event) {
		var curCon = this.mapWidget.getCurrentController();
		curCon.setSplitting(true);
		curCon.setInserting(false);
		curCon.removeDragLines();
	}
});
