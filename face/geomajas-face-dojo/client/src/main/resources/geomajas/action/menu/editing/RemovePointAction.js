dojo.provide("geomajas.action.menu.editing.RemovePointAction");
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

dojo.declare("RemovePointAction", Action, {

	/**
	 * @fileoverview Remove a point from a feature's geometry (rightmouse menu).
	 * @class Remove a point.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 * @param index Every point in a geometry has an index.
	 */
	constructor : function (id, mapWidget, index) {
		/** @private */
		this.mapWidget = mapWidget;

		/** @private */
		this.index = index;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Remove point";
	},

	/**
	 * Execute a {@link RemoveCoordinateCommand} on the feature in the
	 * {@link FeatureTransaction}, stored in the {@link FeatureEditor}.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "delete"]);
			var command = new RemoveCoordinateCommand (this.index);
			trans.execute (command);
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "all"]);

			var curCon = this.mapWidget.getCurrentController();
			if (curCon != null) {
				var geomInfo = curCon.getGeomInfo();
				if (geomInfo != null) {
					curCon.refreshGeomInfo();
				}
			}
		}
	}
});
