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

dojo.provide("geomajas.action.toolbar.DeselectAllAction");
dojo.require("geomajas.action.ToolbarAction");

dojo.declare("DeselectAllAction", ToolbarAction, {

	/**
	 * @fileoverview Deselect all features (DynamicToolbar).
	 * @class This toolbar action will deselect on click. This deselection
	 * will actually happen through a dojo topic.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends ToolbarAction
	 * @param id Unique identifier
	 * @param selectionTopic The selection-topic on which this action can
	 *                       publish a "deselectAll" event.
	 */
	constructor : function (id, selectionTopic) {
		/** Unique identifier */
		this.id = id;

		/** The CSS class for the action image. */
		this.image = "deselectAllIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.DeselectAllAction;

		/** Name of a selection-topic. */
		this.selectionTopic = selectionTopic;

		this.text = "Deselect All";
	},

	/**
	 * The action's execution function. This function is called when the
	 * action's button is clicked. It publishes a "deselectAll" event.
	 * @param event Standard browser mouseevent.
	 */
	actionPerformed : function (event) {
		dojo.publish(this.selectionTopic, [ "deselectAll", null ]);
	},

	getText : function () {
		return this.text;
	}
});