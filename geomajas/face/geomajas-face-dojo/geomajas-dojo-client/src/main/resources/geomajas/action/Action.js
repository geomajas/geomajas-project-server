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

dojo.provide("geomajas.action.Action");
dojo.declare("Action", null, {

	/**
	 * @fileoverview Basic action interface used in toolbars, layertrees, rightmouse menus, etc.
 	 * @class General interface for an action.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 */
	constructor : function () {
		/** Unique identifier */
		this.id = null;

		/** The action can be displayed by this image. */
		this.image = "";
		
		/** The action can be displayed as text only. */
		this.text = "";
		
		/** Is the action enabled or not? */
		this.enabled = true;
	},

	/**
	 * The action's execution function. This function is called when the
	 * action's button is clicked. Needs to be overridden!
	 * @param event Standard browser mouseevent.
	 */
	actionPerformed : function (event) {
		alert("ToolbarAction:actionPerformed : " + event);
	},
	
	// Getters and setters:

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

	getText : function () {
		return this.text;
	},

	setText : function (text) {
		this.text = text;
	},

	isEnabled : function () {
		return this.enabled;
	},

	setEnabled : function (enabled) {
		this.enabled = enabled;
	}
});
