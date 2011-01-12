dojo.provide("geomajas.action.menu.editing.CancelSplittingAction");
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

dojo.declare("CancelSplittingAction", Action, {

	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Cancel splitting";
	},

	actionPerformed : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null) {
			var curCon = this.mapWidget.getCurrentController();
			if (curCon != null) {
				curCon.setSplitting(false);
				curCon.setInserting(false);
				curCon.removeDragLines();
				if (curCon.getGeomInfo() != null) {
					var toggleGeomInfo = new ToggleSplitInfoAction(curCon.getMenuId()+".info", this.mapWidget, curCon);
					toggleGeomInfo.actionPerformed(null);
				}
			}
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "delete"]);
			this.mapWidget.getMapModel().getFeatureEditor().stopEditing(trans);
		}
	}
});
