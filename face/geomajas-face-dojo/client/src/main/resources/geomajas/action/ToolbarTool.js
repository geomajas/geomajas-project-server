dojo.provide("geomajas.action.ToolbarTool");
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
		var button = dijit.byId(this.id);
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
