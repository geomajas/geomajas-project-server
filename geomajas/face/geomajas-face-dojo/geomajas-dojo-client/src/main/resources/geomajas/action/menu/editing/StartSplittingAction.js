dojo.provide("geomajas.action.menu.editing.StartSplittingAction");
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

dojo.declare("StartSplittingAction", Action, {

	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Start splitting";
		
		this.factory = new GeometryFactory(
			mapWidget.getMapModel().getSRID(),
			mapWidget.getMapModel().getPrecision()); // @todo this should be based on the layer
	},

	actionPerformed : function (event) {
		var selection = this.mapWidget.getMapModel().getSelection();
		if (selection.count == 1) {
			var feature = new Feature();
			feature.setGeometry(this.factory.createLineString(null));
			var ft = this.mapWidget.getMapModel().getFeatureEditor().startEditing([feature], [feature]);
			var curCon = this.mapWidget.getCurrentController();
			curCon.setSelected (selection.item(0).clone());
			curCon.setSplitting(true);
			curCon.setInserting(true);
		} else {
			alert("Er moet exact 1 feature geselecteerd zijn.");
		}
	}
});
