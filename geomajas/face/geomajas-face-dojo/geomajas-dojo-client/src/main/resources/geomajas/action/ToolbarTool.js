dojo.provide("geomajas.action.ToolbarTool");
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
dojo.require("dojo.i18n");
dojo.require("dojo.string");
dojo.requireLocalization("geomajas.action", "tooltips");

dojo.declare("ToolbarTool", null, {

	/**
	 * @fileoverview Baseclass for tools for a DynamicToolbar widget.
	 * @class General interface for a tool in the toolbar that needs
	 * enabling/disabling.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
		/** Unique identifier */
		this.id = null;

		/** The image for this tool's button. */
		this.image = "";

		/** The tooltip reference. */
		this.tooltip = "";

		/** Determines whether or not this tool is currently active. */
		this.selected = false;

		/** Is this tool currently enabled or not? */
		this.enabled = true;

		/** The dojo internationalization object for tooltips. */
		this.tooltipLocale = dojo.i18n.getLocalization("geomajas.action", "tooltips");
	},

	init : function (mapWidget) {
		dojo.connect(mapWidget, "onSetController", this, "_onSetController");
	},

	/**
	 * Function called when this tool is selected.
	 * @param event Standard browser mouse-event.
	 */
	onSelect : function (event) {
		alert("ToolbarTool:onSelect : " + event);
	},

	/**
	 * Function called when this tool is deselected.
	 * @param event Standard browser mouse-event.
	 */
	onDeSelect : function (event) {
		alert("ToolbarTool:onDeSelect : " + event);
	},

	select : function () {
		this.selected = true;
	},

	deselect : function () {
		this.selected = false;
	},

	/**
	 * Set's the button's enabled state. When disabled, it is no longer
	 * possible to select or deselect this tool.
	 * @param enabled Boolean that determines the enabled state.
	 */
	setEnabled : function (enabled) {
		this.enabled = enabled;
		var button = dijit.byId(this.id); // TODO: fout fout fout! Via een connect in de DynamicToolbar op moment van toevoegen.
		if (button != null) {
			if (this.enabled) {
				button.setDisabled(false);
			} else {
				button.setDisabled(true);
			}
		}
	},

	/**
	 * @private
	 */
	_onSetController : function(oldController, newController) {
		if (this.controller != null && this.controller != undefined
				&& this.mapWidget != null) {
			if (newController != null
					&& newController.getName() == this.controller.getName()) {
				if (!this.isSelected()) {
					this.select();
				}
			}
			if (oldController != null
					&& oldController.getName() == this.controller.getName()) {
				if (this.isSelected()) {
					this.deselect();
				}
			}
		}
	},

	// Getters and setters:

	isEnabled : function () {
		return this.enabled;
	},

	getId : function () {
		return this.id;
	},

	setId : function (id) {
		this.id = id;
	},

	getImage : function () {
		return this.image;
	},

	setImage : function (image) {
		this.image = image;
	},

	getTooltip : function () {
		return this.tooltip;
	},

	setTooltip : function (tooltip) {
		this.tooltip = tooltip;
	},

	isSelected : function () {
		return this.selected;
	},

	setSelected : function (selected) {
		this.selected = selected;
	},
	
	onConfigDone : function () {
		log.info("post config for toolbar tool "+this.id);
	}
});
