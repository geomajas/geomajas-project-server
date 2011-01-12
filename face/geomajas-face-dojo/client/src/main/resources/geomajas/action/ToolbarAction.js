dojo.provide("geomajas.action.ToolbarAction");
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
dojo.require("dojo.i18n");
dojo.require("dojo.string");
dojo.requireLocalization("geomajas.action", "tooltips");

dojo.declare("ToolbarAction", Action, {

	/**
	 * @fileoverview Baseclass for actions for a DynamicToolbar widget.
	 * @class General interface for a button/action. This can be used for
	 * actions in a DynamicToolbar, or even right-click menu's.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 * @extends Action
	 */
	constructor : function () {
		/** The tooltip reference. */
		this.tooltip = "";

		/** The dojo internationalization object for tooltips. */
		this.tooltipLocale = dojo.i18n.getLocalization("geomajas.action", "tooltips");
	},

	// Overriding...

	/**
	 * Set's the button's enabled state. When disabled, it is no longer
	 * possible to select or deselect this action.
	 * @param enabled Boolean that determines the enabled state.
	 */
	setEnabled : function (enabled) {
		this.enabled = enabled;
		var button = dijit.byId (this.id);
		if (button != null) {
			if (this.enabled) {
				button.setDisabled(false);
			} else {
				button.setDisabled(true);
			}
		}
	},

	// Getters and setters:

	getTooltip : function () {
		return this.tooltip;
	},

	setTooltip : function (tooltip) {
		this.tooltip = tooltip;
	},
	
	onConfigDone : function () {
		log.info("post config for action "+this.id);
	}
});
