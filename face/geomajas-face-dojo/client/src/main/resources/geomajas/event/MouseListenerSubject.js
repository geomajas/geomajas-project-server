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

dojo.provide("geomajas.event.MouseListenerSubject");
dojo.require("dojox.collections.Dictionary");
dojo.require("geomajas.event.HtmlMouseEvent");
dojo.require("geomajas.event.ListenerSubject");

dojo.declare ("MouseListenerSubject", ListenerSubject, {

	/**
	 * @fileoverview ListenerSubject for standard mouse events.
	 * @class Extension of the ListenerSubject that has a FPS restriction on it
	 * for the MouseMove events. During initalization, an extra integer is
	 * passed that determines how many times a second a MouseMove event may
	 * actually be passed along to the Listeners. Default is 30 frames per
	 * second.
	 *
	 * @author Pieter De Graef
	 * @constructor
	 * @extends ListenerSubject
	 * @param subject Should be a SVG element, not a SVG document! This means
	 *                that the given SVG element is a subject to
	 *                MouseListeners from now on.
	 * @param fps The maximum amount of frames per second.
	 */
	constructor : function (/*Element*/subject, /*Integer*/fps) {
		this.subject = subject;
		if (fps == null || fps == 0) {
			this.fps = 30;
		} else {
			this.fps = fps;
		}
		this.date = new Date();
		this.stopPropagation = true;
		this.preventContext = true;
		this._afterInit ();
	},

	/**
	 * Add a new MouseListener object to the subject SVG element.
	 * @param listener The MouseListener object to be added to the list.
	 */
	addListener : function (/*MouseListener*/listener) {
	},

	/**
	 * Remove an existing MouseListener object from the list. If the listener was not
	 * found, nothing happens.
	 * @param listener The MouseListener object to be removed from the list. 
	 */
	removeListener : function (/*MouseListener*/listener) {
	},

	/**
	 * Stops the further propagation of events in the DOM structure
	 * @param listener The MouseListener object to be removed from the list. 
	*/
	setStopPropagation : function(value) {
		if(value){
			this.stopPropagation = true;
		} else {
			this.stopPropagation = false;
		}
	},

	isStopPropagation : function () {
		return this.stopPropagation;
	},

	/**
	 * Determines the default contextmenu action when there is no listener
	 * registered. So this value is used only when there are no listeners!
	 * @param preventContext Boolean value. If true, no contextmenu will be
	 *                       shown on right mouse click. If false, the default
	 *                       contextmenu is shown.
	 */
	setPreventContext : function (preventContext) {
		this.preventContext = preventContext;
	},

	isPreventContext : function () {
		return preventContext;
	},

	click : function (evt) {	
	},

	mousedown : function (evt) {
	},

	mouseup : function (evt) {
	},

	mousemove : function (evt) {
	},

	mouseover : function (evt) {
	},

	mouseout : function (evt) {
	},

	contextmenu : function (evt) {
	},

	// Getters and setters:

	getDate : function () {
		return this.date;
	},

	setDate : function (date) {
		this.date = date;
	},

	getFps : function () {
		return this.fps;
	},

	setFps : function (fps) {
		this.fps = fps;
	},
	
	setOffset : function (coords) {
		
	},
	
	destroy : function () {
		if(this.subject){
			dojo._destroyElement(this.subject);
			delete this.subject;
		}
	}
	
});