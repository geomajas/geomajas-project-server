dojo.provide("geomajas.action.Action");
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
dojo.declare("Action", null, {

	/**
	 * @fileoverview Basic action interface used in toolbars, layertrees, rightmouse menus, etc.
 	 * @class General interface for an action.
	 * @author Pieter De Graef
	 *
	 * @constructor 
	 */
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
