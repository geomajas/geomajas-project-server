dojo.provide("geomajas.action.menu.editing.DeleteFeatureAction");
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
