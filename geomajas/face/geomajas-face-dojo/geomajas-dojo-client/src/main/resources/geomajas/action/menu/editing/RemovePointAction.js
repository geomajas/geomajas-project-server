dojo.provide("geomajas.action.menu.editing.RemovePointAction");
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

dojo.declare("RemovePointAction", Action, {

	/**
	 * @fileoverview Remove a point from a feature's geometry (rightmouse menu).
	 * @class Remove a point.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 * @param index Every point in a geometry has an index.
	 */
	constructor : function (id, mapWidget, index) {
		/** @private */
		this.mapWidget = mapWidget;

		/** @private */
		this.index = index;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Remove point";
	},

	/**
	 * Execute a {@link RemoveCoordinateCommand} on the feature in the
	 * {@link FeatureTransaction}, stored in the {@link FeatureEditor}.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "delete"]);
			var command = new RemoveCoordinateCommand (this.index);
			trans.execute (command);
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "all"]);

			var curCon = this.mapWidget.getCurrentController();
			if (curCon != null) {
				var geomInfo = curCon.getGeomInfo();
				if (geomInfo != null) {
					curCon.refreshGeomInfo();
				}
			}
		}
	}
});
