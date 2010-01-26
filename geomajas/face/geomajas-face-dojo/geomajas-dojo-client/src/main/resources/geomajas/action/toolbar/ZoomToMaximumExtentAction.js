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
dojo.provide("geomajas.action.toolbar.ZoomToMaximumExtentAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("ZoomToMaximumExtentAction", ToolbarAction, {

	constructor : function (id, mapWidget, excludeRasterLayers, includeLayers) {
		/** Unique identifier */
		this.id = id;

		/** The CSS class for the action image. */
		this.image = "zoomToMaximumExtentIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.ZoomToMaximumExtentAction;

		this.mapWidget = mapWidget;
		this.text = "Zoom to maximum extent";
		this.excludeRasterLayers = (excludeRasterLayers==null ? true : excludeRasterLayers);
		this.includeLayers = includeLayers;
	},

	getText : function () {
		return this.text;
	},

	actionPerformed : function (event) {
		var bounds = geomajasConfig.zoomMaxExtentBounds;
		if (bounds == null) {
			this._getMaxExtent();
		} else {
			this._zoomToMaxExtent();
		}
	},
	
	_getMaxExtent : function () {
		var command = new JsonCommand("command.configuration.UserMaximumExtent","org.geomajas.extension.command.dto.UserMaximumExtentRequest", null, false);
		command.addParam("mapId", this.mapWidget.getMapModel().getId());
		command.addParam("excludeRasterLayers", this.excludeRasterLayers);
		command.addParam("includeLayers", this.includeLayers);
		var deferred = geomajasConfig.dispatcher.execute(command);
		deferred.addCallback(this, "_jsonCallback");
		geomajasConfig.zoomMaxExtentBounds = null;
	},
	
	_jsonCallback : function (/*result*/ result) {
		geomajasConfig.zoomMaxExtentBounds = result.bounds;
		this._zoomToMaxExtent();
	},
	
	_zoomToMaxExtent : function () {
		var bounds = geomajasConfigzoomMaxExtentBounds;
		if (bounds != null) {
			log.info("setBounds: x,y,w,h" + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height);
			dojo.publish(this.mapWidget.getMapView().getExternalRenderTopic(), [{event:"setBounds", x:bounds.x, y:bounds.y, width:bounds.width, height:bounds.height, option:geomajas.ZoomOption.ZOOM_OPTION_LEVEL_FIT}]);
		} else {
			console.warn("Cannot zoom to maximum extent: bounds is not set.");
		}
	}
});
