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