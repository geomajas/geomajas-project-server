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

dojo.provide("geomajas.action.toolbar.EditSelectedAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("EditSelectedAction", ToolbarAction, {

	constructor : function (id, mapWidget) {
		/** Unique identifier */
		this.id = id;

		/** The CSS class for the action image. */
		this.image = "editSelectedIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.EditSelectedAction;

		this.mapWidget = mapWidget;

		this.text = "Edit attributes of selected object";
		
		this.dialog = null;
		
		this.feature = null;
	},

	actionPerformed : function (event) {
		var selection = this.mapWidget.getMapModel().getSelection();
		var bounds = null;
		if(selection.count == 1){
			this.feature = selection.item(0).clone();
			if (this.dialog == null) {
				this.dialog = new geomajas.widget.FeatureEditDialog({id:this.id+":dialog", title:"Feature detail", feature:this.feature});
				dojo.connect (this.dialog, "execute", dojo.hitch(this, "_onExecute"));
			} else {
				this.dialog.setFeature(this.feature);
			}
			this.dialog.show();
		}
	},

	/**
	 * @private
	 */
	_onExecute : function () {
		log.info("on execute");
		var newFeature = this.dialog.feature;
		if (newFeature == null) {
			log.error ("Adjusted feature could not be retrieved.");
			return;
		}
		log.info("start editing");
		this.mapWidget.getMapModel().getFeatureEditor().startEditing([this.feature], [newFeature]);
		var saveAction = new SaveEditingAction ("saveAction", this.mapWidget, null);
		saveAction.actionPerformed(null);
		log.info("done editing");
	},

	getText : function () {
		return this.text;
	}
});