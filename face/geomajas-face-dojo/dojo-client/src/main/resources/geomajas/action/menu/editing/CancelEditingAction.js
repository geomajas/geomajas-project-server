dojo.provide("geomajas.action.menu.editing.CancelEditingAction");
/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
