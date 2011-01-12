dojo.provide("geomajas.action.menu.editing.EditAttributesAction");
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

dojo.declare("EditAttributesAction", Action, {

	constructor : function (id, mapWidget, editController) {
		/** @private */
		this.mapWidget = mapWidget;

		this.editController = editController;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Edit attributes";
		
		/** @private */
		this.dialog = null;
	},

	actionPerformed : function (event) {
		var featureTransaction = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		var feature = featureTransaction.getNewFeatures()[this.editController.getFeatureIndex()];
		if (this.dialog == null) {
			this.dialog = new geomajas.widget.FeatureEditDialog({id:this.id + ":dialog", title:this.text, feature:feature});
		} else {
			this.dialog.setFeature(feature);
		}
		this.dialog.show();
	}
});
