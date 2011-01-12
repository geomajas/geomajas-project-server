dojo.provide("geomajas.action.menu.editing.SaveEditingAction");
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

dojo.declare("SaveEditingAction", Action, {

	/**
	 * @fileoverview Save the current edited state (rightmouse menu).
	 * @class Save the current FeatureTransaction.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 * @param editController The active editing-controller.
	 */
	constructor : function (id, mapWidget, editController) {
		/** @private */
		this.mapWidget = mapWidget;
			
		/** @private */
		this.editController = editController;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Save feature";
	},

	/**
	 * Save = apply + cancel.
	 * Starts the workflow, and cleans the map up.
	 * @param event The {@link HtmlMouseEvent} from clicking the action.
	 */
	actionPerformed : function (event) {
		// APPLY
		var featureTransaction = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		var layer = featureTransaction.getLayer();
		var handler = layer.getWorkflowHandler();
		var workflow = handler.startWorkflow(featureTransaction);
		workflow.start();
		
		// CANCEL
		var cancel = new CancelEditingAction(this.menuId+".cancel", this.mapWidget, this.editController);
		cancel.actionPerformed(event);
	}
	
});
