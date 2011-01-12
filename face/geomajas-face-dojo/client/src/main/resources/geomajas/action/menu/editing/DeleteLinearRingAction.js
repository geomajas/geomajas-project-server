dojo.provide("geomajas.action.menu.editing.DeleteLinearRingAction");
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

dojo.declare("DeleteLinearRingAction", Action, {

	/**
	 * @fileoverview Action for deleting a hole from a polygon (rightmouse menu).
	 * @class Right mouse menu action for deleting a hole ({@link LinearRing})
	 * from a polygon.
	 * @author Pieter De Graef
	 * @constructor
	 * @extends Action
	 * @param id Unique identifier.
	 * @param mapWidget The map widget on which the ring is rendered.
	 */
	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Delete hole";
	},

	/**
	 * Actually delete a ring from the FeatureTransaction.
	 */
	actionPerformed : function (event) {
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		var target = event.getTargetId();
		var index = trans.getLineStringIndexById(target);
		if (index != null && index[1].length > 0) {
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "delete"]);
			var command = new RemoveRingCommand (index);
			trans.execute (command);
			dojo.publish (this.mapWidget.getRenderTopic(), [trans, "all"]);
		}
	}
});
