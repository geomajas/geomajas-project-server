dojo.provide("geomajas.action.menu.editing.CancelEditingAction");
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

dojo.declare("CancelEditingAction", Action, {

	/**
	 * @fileoverview Cancel editing, and clean up (rightmouse menu).
	 * @class Action that stops the editing and also removes any
	 * FeatureTransaction from the FeatureEditor.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 * @param editController The active editing-controller.
	 */
	constructor : function (id, mapWidget, editController) {
		/** @private */
		this.mapWidget = mapWidget;

		/** @private */
		this.editController = editController;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		if (editController && editController.isDrawOnlyMode()) {
			this.text = "Cancel Drawing";
		} 
		else {
			this.text = "Cancel Editing";
		}
	},

	/**
	 * Cancel's editing, and also removes the FeatureTransaction object from
	 * the map.
	 * @param event The {@link HtmlMouseEvent} from clicking the action.
	 */
	actionPerformed : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null) {
			if (this.editController != null) {
				this.editController.removeDragLines();
				this.editController.setGeometryIndex(null);

				if (this.editController.getGeomInfo() != null) {
					var toggleGeom = new ToggleGeomInfoAction("toggleGeomInfo", this.mapWidget, this.editController);
					toggleGeom.actionPerformed(null);
				}
			}
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "delete"]);
			this.mapWidget.getMapModel().getFeatureEditor().stopEditing(trans);
		}
	}
});
