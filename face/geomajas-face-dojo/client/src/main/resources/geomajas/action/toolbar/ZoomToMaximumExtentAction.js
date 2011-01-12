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
		if (includeLayers && includeLayers != "")
			this.includeLayers = includeLayers.split(",");
		else
			this.includeLayers = [];
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
		var command = new JsonCommand("command.configuration.UserMaximumExtent","org.geomajas.command.dto.UserMaximumExtentRequest", null, false);
		command.addParam("mapId", this.mapWidget.getMapModel().getId());
		command.addParam("excludeRasterLayers", this.excludeRasterLayers);
		command.addParam("layerIds", this.includeLayers);
		command.addParam("crs", "EPSG:"+this.mapWidget.getMapModel().getMapView().getSRID());
		var deferred = geomajasConfig.dispatcher.execute(command);
		deferred.addCallback(this, "_jsonCallback");
		geomajasConfig.zoomMaxExtentBounds = null;
	},
	
	_jsonCallback : function (/*result*/ result) {
		geomajasConfig.zoomMaxExtentBounds = result.bounds;
		this._zoomToMaxExtent();
	},
	
	_zoomToMaxExtent : function () {
		var bounds = geomajasConfig.zoomMaxExtentBounds;
		if (bounds != null) {
			log.info("setBounds: x,y,w,h" + bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height);
			dojo.publish(this.mapWidget.getMapView().getExternalRenderTopic(), [{event:"setBounds", x:bounds.x, y:bounds.y, width:bounds.width, height:bounds.height, option:geomajas.ZoomOption.ZOOM_OPTION_LEVEL_FIT}]);
		} else {
			console.warn("Cannot zoom to maximum extent: bounds is not set.");
		}
	}
});
