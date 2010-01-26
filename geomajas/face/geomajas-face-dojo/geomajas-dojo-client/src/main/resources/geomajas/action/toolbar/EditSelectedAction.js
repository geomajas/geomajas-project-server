/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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