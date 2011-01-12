dojo.provide("geomajas.action.menu.editing.ToggleEditingModeAction");
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

dojo.declare("ToggleEditingModeAction", Action, {

	/**
	 * @fileoverview Toggle between editing modes (rightmouse menu).
	 * @class Toggle between insert and drag mode.
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
		this.text = "";
		if (editController.getMode() == editController.statics.INSERT_MODE) {
			this.text = "Stop inserting";
		} else if (editController.getMode() == editController.statics.DRAG_MODE) {
			this.text = "Continue inserting";
		}
	},

	/**
	 * Simply changes the {@link EditingController}'s mode field.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		if (this.editController.getMode() == this.editController.statics.INSERT_MODE) {
			this.editController.setMode(this.editController.statics.DRAG_MODE);
			this.editController.removeDragLines();
		} else {
			var featureTransaction = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
			this.editController.setMode(this.editController.statics.INSERT_MODE);
			this.editController.createDragLines(featureTransaction, event);
		}
	}
});
