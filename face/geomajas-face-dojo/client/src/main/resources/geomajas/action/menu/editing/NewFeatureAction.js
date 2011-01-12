dojo.provide("geomajas.action.menu.editing.NewFeatureAction");
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

dojo.declare("NewFeatureAction", Action, {

	/**
	 * @fileoverview Create a new feature (rightmouse menu).
	 * @class Create a feature.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 * @param editController The active editing-controller.
	 */
	constructor : function (id, mapWidget, editingController) {
		/** @private */
		this.mapWidget = mapWidget;

		/** @private */
		this.editingController = editingController;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Create new feature";
	},

	/**
	 * Create a new FeatureTransaction, that can be used for the creation of
	 * a new feature. Basically this function calls {@link FeatureEditor#startEditing}.
	 * @param event The {@link HtmlMouseEvent} from clicking this action.
	 */
	actionPerformed : function (event) {
		this.mapWidget.getMapModel().getFeatureEditor().startEditing(null, null);
		this.editingController.setGeometryIndex(null);
	}
});
