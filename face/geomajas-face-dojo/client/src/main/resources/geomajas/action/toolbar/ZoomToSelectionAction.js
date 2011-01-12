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

dojo.provide("geomajas.action.toolbar.ZoomToSelectionAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("ZoomToSelectionAction", ToolbarAction, {

	constructor : function (id, mapWidget) {
		/** Unique identifier */
		this.id = id;

		/** The CSS class for the action image. */
		this.image = "zoomToSelectionIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.ZoomToSelectionAction;

		this.mapWidget = mapWidget;

		this.text = "Zoom to selection";
	},

	actionPerformed : function (event) {
	
		var selection = this.mapWidget.getMapModel().getSelection();
		var bounds = null;
		
		for (var i=0; i<selection.count; i++) {
			var feature = selection.item(i);
			
			var featBounds = feature.getGeometry().getBounds();
			if (featBounds.getWidth() == 0 || featBounds.getHeight() == 0) {
				/* Force non-zero bounds, because bounds.union will not take features into account with zero width or height */
				featBounds = featBounds.buffer(1);
			}
			
			if (bounds == null) {
				bounds = featBounds;
			} else {
				bounds = bounds.union(featBounds);
			}
		}
		if (bounds != null) {
			// add 10% to bounds:
			var len = 0;
			if (bounds.getWidth() > bounds.getHeight()) {
				len = bounds.getWidth() * 0.1;
			} else {
				len = bounds.getHeight() * 0.1;
			}
			bounds = bounds.buffer(len);				
			// Set new bounds on the map 
			dojo.publish(this.mapWidget.getMapView().getExternalRenderTopic(), [{
				event:"setBounds",
				x:bounds.getX(), 
				y:bounds.getY(), 
				width:bounds.getWidth(), 
				height:bounds.getHeight(),
				option:geomajas.ZoomOption.ZOOM_OPTION_EXACT
			}]);

			var layer = selection.item(0).getLayer();
			var newScale = this.mapWidget.getMapView().getCurrentScale();  /* pixels/unit-length */ 
			 
			if (newScale > layer.getMaxViewScale()/2.0) {
				newScale = layer.getMaxViewScale()/2.0;
				var minScale = layer.getMinViewScale(); /* pixels/unit-length */
				if (newScale < minScale) {
					newScale = minScale;
				}
				dojo.publish(this.mapWidget.getMapView().getExternalRenderTopic(), [{
						event:"scale",
						scale: newScale
					}]);
			}
			/* recenter (only needed in case setBounds failed */
			this.mapWidget.getMapView().setCenterPosition(bounds.getCenterPoint());
		}
	},

	getText : function () {
		return this.text;
	}
});