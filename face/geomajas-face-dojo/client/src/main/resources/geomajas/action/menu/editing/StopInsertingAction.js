dojo.provide("geomajas.action.menu.editing.StopInsertingAction");
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

dojo.declare("StopInsertingAction", Action, {

	/**
	 * @fileoverview Set the edit controller in drag mode (rightmouse menu).
	 * @class Sets the {@link EditingController} in DRAG_MODE.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 * @param editController The active {@link EditingController}.
	 */
	constructor : function (id, mapWidget, editController) {
		/** @private */
		this.mapWidget = mapWidget;

		/** @private */
		this.editController = editController;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Stop inserting";
	},

	/**
	 * Simply changes the {@link EditingController}'s mode field to DRAG_MODE.
	 * Also removes any draglines.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		this.editController.setMode(this.editController.statics.DRAG_MODE);
		this.editController.removeDragLines();
	}
});
