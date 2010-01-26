dojo.provide("geomajas.action.ToolbarAction");
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
		var button = dijit.byId (this.id); // TODO: fout fout fout!
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
