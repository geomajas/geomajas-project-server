dojo.provide("geomajas.action.menu.editing.DeleteFeatureAction");
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
dojo.require("dojo.i18n");
dojo.require("dojo.string");
dojo.requireLocalization("geomajas.action", "tooltips");

dojo.declare("DeleteFeatureAction", Action, {

	/**
	 * @fileoverview Delete a feature (rightmouse menu).
	 * @class Action that delete's a feature. (rightmouse-menu)
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
		this.text = "Delete feature";
	},

	/**
	 * If there is exactly one feature selected, this function will delete it.
	 * @param event The {@link HtmlMouseEvent} from clicking the action.
	 */
	actionPerformed : function (event) {
		var selection = this.mapWidget.getMapModel().getSelection();
		if (selection.count == 1) {
			var feature = selection.item(0).clone();
			var tooltipLocale = dojo.i18n.getLocalization("geomajas.action", "tooltips");
			if (confirm(tooltipLocale.DeleteFeatureAction)) {
				this.mapWidget.getMapModel().getFeatureEditor().startEditing([feature], null);
				var saveAction = new SaveEditingAction ("saveAction", this.mapWidget, null);
				saveAction.actionPerformed(event);
			}
		} else {
			alert("There should be exactly 1 feature selected."); // TODO: not nl only!!
		}		
	}
});
