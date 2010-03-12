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
dojo.provide("geomajas.controller.internal.MouseListenerController");
dojo.require("geomajas.event.MouseListener");

dojo.declare("MouseListenerController", MouseListener, {
	/**
	 * @fileoverview Controller that delegates MouseEvents to all Controllers added to this.
	 * @class All mouseevents supported in the DOM specification, are
	 * supported here as well.
	 * @author Balder Van Camp
	 *
	 * @constructor
	 * @extends MouseListener
	 */
	constructor : function (mapWidget) {
		this.mapWidget = mapWidget;
		this.listeners = new dojox.collections.Dictionary();
	},

	getName : function () {
		return "geomajasControllerInternalMouseListenerController";
	},

	/**
	 * This function is called on a "onClick" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseClicked : function (/*HtmlMouseEvent*/event) {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].mouseClicked(event);
		}
	},

	/**
	 * This function is called on a "onMouseOver" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseEntered : function (/*HtmlMouseEvent*/event) {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].mouseEntered(event);
		}
	},

	/**
	 * This function is called on a "onMouseOut" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseExited : function (/*HtmlMouseEvent*/event) {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].mouseExited(event);
		}
	},

	/**
	 * This function is called on a "onMouseDown" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mousePressed : function (/*HtmlMouseEvent*/event) {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].mousePressed(event);
		}
	},

	/**
	 * This function is called on a "onMouseUp" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseReleased : function (/*HtmlMouseEvent*/event) {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].mouseReleased(event);
		}
	},

	/**
	 * This function is called on a "onMouseMove" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	mouseMoved : function (/*HtmlMouseEvent*/event) {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].mouseMoved(event);
		}
	},

	/**
	 * This function is called on a "oncontextmenu" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	contextMenu : function (/*HtmlMouseEvent*/event) {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].contextMenu(event);
		}
	},
	
	/**
	 * This function is called on a "ondblclick" event.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	doubleClick : function (event) {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].doubleClick(event);
		}
	},

	/**
	 * This function is called when the MouseListener is activated.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	onActivate : function () {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].onActivate(event);
		}
	},
	
	/**
	 * This function is called when the MouseListener is deactivated.
	 * @param {HtmlMouseEvent} event A HtmlMouseEvent object.
	 */
	onDeactivate : function () {
		var list = this.listeners.getValueList();
		for (var i=0;i < list.length;i++) {
			list[i].onDeactivate(event);
		}
	},
	
	/**
	 * Add a new MouseListener object.
	 * @param listener The MouseListener object to be added to the list.
	 */
	addListener : function (/*MouseListener*/listener) {
		if (!this.listeners.contains(listener.getName())) {
			this.listeners.add(listener.getName(), listener);
		}
	},

	/**
	 * Remove an existing MouseListener object from the list. If the listener was not
	 * found, nothing happens.
	 * @param listener The MouseListener object to be removed from the list. 
	 */
	removeListener : function (/*MouseListener*/listener) {
		if (this.listeners.contains(listener.getName())) {
			this.listeners.remove(listener.getName());
		}
	}
});