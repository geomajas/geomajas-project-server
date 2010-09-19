dojo.provide("geomajas.action.menu.editing.DeleteLinearRingAction");
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

dojo.declare("DeleteLinearRingAction", Action, {

	/**
	 * @fileoverview Action for deleting a hole from a polygon (rightmouse menu).
	 * @class Right mouse menu action for deleting a hole ({@link LinearRing})
	 * from a polygon.
	 * @author Pieter De Graef
	 * @constructor
	 * @extends Action
	 * @param id Unique identifier.
	 * @param mapWidget The map widget on which the ring is rendered.
	 */
	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Delete hole";
	},

	/**
	 * Actually delete a ring from the FeatureTransaction.
	 */
	actionPerformed : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		var target = event.getTargetId();
		var index = trans.getLineStringIndexById(target);
		if (index != null && index[1].length > 0) {
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "delete"]);
			var command = new RemoveRingCommand (index);
			trans.execute (command);
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "all"]);
		}
	}
});
