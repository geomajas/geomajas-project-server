dojo.provide("geomajas.action.menu.editing.ToggleGeomInfoAction");
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
