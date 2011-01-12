dojo.provide("geomajas.action.menu.editing.InsertPointAction");
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

dojo.declare("InsertPointAction", Action, {

	/**
	 * @fileoverview Insert a point from a feature's geometry (rightmouse menu).
	 * @class Insert a point in a feature's geometry.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 * @param index Every point in a geometry has an index.
	 * @param position Coordinate holding the position for the new point.
	 */
	constructor : function (id, mapWidget, index, position) {
		/** @private */
		this.mapWidget = mapWidget;

		/** @private */
		this.index = index;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Insert point";

		/** The point's position. */
		this.position = position;
	},

	/**
	 * Execute a {@link InsertCoordinateCommand} on the feature in the
	 * {@link FeatureTransaction}, stored in the {@link FeatureEditor}. Also
	 * redraws the {@link FeatureTransaction}.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		if (trans != null) {
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "delete"]);
			var command = new InsertCoordinateCommand (this.index, this.position);
			trans.execute (command);
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "all"]);
		}
	}
});
