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