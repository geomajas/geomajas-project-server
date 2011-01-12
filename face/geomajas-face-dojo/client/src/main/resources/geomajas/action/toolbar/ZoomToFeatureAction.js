dojo.provide("geomajas.action.toolbar.ZoomToFeatureAction");
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
