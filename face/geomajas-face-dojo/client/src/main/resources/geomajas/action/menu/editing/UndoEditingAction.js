dojo.provide("geomajas.action.menu.editing.UndoEditingAction");
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

dojo.declare("UndoEditingAction", Action, {

	/**
	 * @fileoverview Undo the last editing step (rightmouse menu).
	 * @class Undo one editing step.
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
		this.text = "Undo last step";
	},

	/**
	 * All steps in editing are simple commands to be executed. All of these
	 * commands also have an "undo" function. This function calls {@link FeatureEditor#undoLast}
	 * and takes care of redrawing the {@link FeatureTransaction} object.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();

		dojo.publish (this.mapWidget.getRenderTopic(), [trans, "delete"]);
		this.mapWidget.getMapModel().getFeatureEditor().undoLast(trans);
		dojo.publish (this.mapWidget.getRenderTopic(), [trans, "all"]);

		this.editController.removeDragLines();
		this.editController.refreshGeomInfo();
	}
});
