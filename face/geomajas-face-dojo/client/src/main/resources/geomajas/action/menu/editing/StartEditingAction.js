dojo.provide("geomajas.action.menu.editing.StartEditingAction");
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

dojo.declare("StartEditingAction", Action, {

	/**
	 * @fileoverview Start editing a feature (rightmouse menu).
	 * @class Start editing a feature.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 */
	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Start editing";
	},

	/**
	 * If there is exactly one feature selected, it can be edited. This
	 * function calls {@link FeatureEditor#startEditing} to do that.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		var selection = this.mapWidget.getMapModel().getSelection();
		if (selection.count == 1) {
			var feature = selection.item(0).clone();
			if(feature.isClipped()){
				alert("Clipped features cannot be edited, try zooming out first");
			} else {
				var ft = this.mapWidget.getMapModel().getFeatureEditor().startEditing([feature], [feature]);
				dojo.publish(this.mapWidget.getRenderTopic(), [ft, "all"]);

				curCon = this.mapWidget.getCurrentController();
				if (curCon == null || !(curCon instanceof  EditingController)) {
					curCon = new EditingController (this.mapWidget)
					this.mapWidget.setController(curCon);
				}
				curCon.setMode(curCon.statics.DRAG_MODE);
				// seems to work for all geom types ???
				curCon.setGeometryIndex([0]);
			}
		} else {
			alert("Er moet exact 1 feature geselecteerd zijn.");
		}
	}
});
