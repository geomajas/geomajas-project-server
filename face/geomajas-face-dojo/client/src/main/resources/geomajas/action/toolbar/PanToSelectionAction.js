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

dojo.provide("geomajas.action.toolbar.PanToSelectionAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("PanToSelectionAction", ToolbarAction, {

	constructor : function (id, mapWidget) {
		/** Unique identifier */
		this.id = id;

		/** The CSS class for the action image. */
		this.image = "panToSelectionIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.PanToSelectionAction;

		this.mapWidget = mapWidget;

		this.text = "Pan to selection";
	},

	actionPerformed : function (event) {
		var selection = this.mapWidget.getMapModel().getSelection();
		var bounds = null;
		for (var i=0; i<selection.count; i++) {
			var feature = selection.item(i);
			var geometry = feature.getGeometry();
			if (bounds == null) {
				bounds = geometry.getBounds();
			} else {
				bounds = bounds.union(geometry.getBounds());
			}
		}
		if (bounds != null) {
			this.mapWidget.getMapView().setCenterPosition(bounds.getCenterPoint());
		}
	},

	getText : function () {
		return this.text;
	}
});