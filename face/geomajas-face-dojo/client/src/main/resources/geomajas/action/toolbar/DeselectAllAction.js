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