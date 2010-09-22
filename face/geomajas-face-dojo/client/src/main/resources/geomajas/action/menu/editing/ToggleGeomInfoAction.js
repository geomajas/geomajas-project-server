dojo.provide("geomajas.action.menu.editing.ToggleGeomInfoAction");
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

dojo.declare("ToggleGeomInfoAction", Action, {

	constructor : function (id, mapWidget, editController) {
		/** @private */
		this.mapWidget = mapWidget;

		this.editController = editController;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Toggle info";
//		if (editController.getGeomInfo() == null) {
//			this.text = "Show info";
//		} else {
//			this.text = "Hide info";
//		}
	},

	/**
	 * Simply changes the {@link EditingController}'s mode field.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		var geomInfo = this.editController.getGeomInfo();
		if (geomInfo == null) {
			// Create:
			geomInfo = this._createGeomInfoBalloon(event);
			this.editController.setGeomInfo(geomInfo);

			// Then show on the map:
			if (geomInfo) {
				geomInfo.render(this.mapWidget.domNode);
			}
		} else {
			// Remove from the map:
			geomInfo.destroy();

			// Then remove from controller:
			this.editController.setGeomInfo(null);
		}
	},

	/**
	 * @private
	 */
	_createGeomInfoBalloon : function (event) {
		var ft = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (ft != null) {
			var geometry = ft.getNewFeatures()[0].getGeometry();
			var balloon = new geomajas.widget.TextBalloon({id:"geomInfo"}, document.createElement("div"));
			balloon.setPosition(new Coordinate(10, 10));
			balloon.setText("Geometric info:<br/>Length: "+geometry.getLength().toFixed(2)+"<br/>Area: "+geometry.getArea().toFixed(2));
			return balloon;
		}
		return null;
	}
});
