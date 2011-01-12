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

dojo.provide("geomajas.event.MouseListener");
dojo.require("geomajas.event.EventListener");

dojo.declare("MouseListener", EventListener, {

	/**
	 * @fileoverview Interface for dealing with default MouseEvents.
	 * @class General interface for listeners to MouseEvents.
	 * All mouseevents supported in the DOM specification, are
	 * supported here as well.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends EventListener
	 */
	constructor : function () {
	},

	/**
	 * Override me!. Every type of EventListener should return a unique name.
	 */
	getName : function () {
		return "mouseListener";
	},

	/**
	 * This function is called on a "onClick" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
	},

	/**
	 * This function is called on a "onMouseOver" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseEntered : function (/*HtmlMouseEvent*/event) {
	},

	/**
	 * This function is called on a "onMouseOut" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseExited : function (/*HtmlMouseEvent*/event) {
	},

	/**
	 * This function is called on a "onMouseDown" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mousePressed : function (/*HtmlMouseEvent*/event) {
	},

	/**
	 * This function is called on a "onMouseUp" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseReleased : function (/*HtmlMouseEvent*/event) {
	},

	/**
	 * This function is called on a "onMouseMove" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
	},

	/**
	 * This function is called on a "oncontextmenu" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	contextMenu : function (/*HtmlMouseEvent*/event) {
		event.stopPropagation();
	},
	
	/**
	 * This function is called on a "ondblclick" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	doubleClick : function (event) {
	},

	/**
	 * This function is called when the MouseListener is activated.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	onActivate : function () {
	},
	
	/**
	 * This function is called when the MouseListener is deactivated.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	onDeactivate : function () {
	}
	
});