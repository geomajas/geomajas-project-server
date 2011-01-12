dojo.provide("geomajas.action.menu.editing.StartSplittingAction");
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
