dojo.provide("geomajas.action.toolbar.ZoomToFeatureAction");
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

dojo.require("geomajas.action.ToolbarAction");

dojo.declare("ZoomToFeatureAction", ToolbarAction, {

	constructor : function (id, mapWidget, feature) {
		/** Unique identifier */
		this.id = id;

		/** The CSS class for the action image. */
		this.image = "zoomToSelectionIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.ZoomToFeatureAction;

		this.mapWidget = mapWidget;
		this.feature = feature;

		this.text = "Zoom to fit";
	},

	actionPerformed : function (event) {
		if (this.mapWidget != null && this.feature != null) {
			var geometry = this.feature.getGeometry();
			var bounds = geometry.getBounds();
			
			// First add 10% to bounds:
			var len = 0;
			if (bounds.getWidth() > bounds.getHeight()) {
				len = bounds.getWidth() * 0.1;
			} else {
				len = bounds.getHeight() * 0.1;
			}
			bounds = bounds.buffer(len);
			if (bounds.getWidth() == 0 || bounds.getHeight() == 0) {
				bounds = bounds.buffer(1);
			}

			dojo.publish(this.mapWidget.getMapView().getExternalRenderTopic(), [{
				event:"setBounds",
				x:bounds.getX(), 
				y:bounds.getY(), 
				width:bounds.getWidth(), 
				height:bounds.getHeight(),
				option:geomajas.ZoomOption.ZOOM_OPTION_LEVEL_FIT
			}]);
		}
	},

	getText : function () {
		return this.text;
	}
});
