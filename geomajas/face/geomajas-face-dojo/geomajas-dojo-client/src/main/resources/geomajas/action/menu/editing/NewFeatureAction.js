dojo.provide("geomajas.action.menu.editing.NewFeatureAction");
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
