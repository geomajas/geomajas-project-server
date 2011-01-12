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

dojo.provide("geomajas.event.MouseScrollListenerSubject");
dojo.require("geomajas.event.HtmlMouseEvent");
dojo.require("geomajas.event.ListenerSubject");

dojo.declare("MouseScrollListenerSubject", ListenerSubject, {

	/**
	 * @fileoverview ListenerSubject for mouse-scroll events.
	 * @class This listener subject for scrolling events only allows 1 listener
	 * to be active at a time. This means that adding a second listener, will
	 * automatically remove the first one. This is because a scroll-event
	 * is caught by the window object: there can be no other subject.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param subject The DOM element we are to hang the listeners on.
	 * @extends ListenerSubject
	 */
	constructor : function (/*Element*/subject) {
		this.subject = subject;
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
	
	scroll : function (evt) {	
	},
	
	destroy : function () {
		if(this.subject){
			dojo._destroyElement(this.subject);
			delete this.subject;
		}
	}

	
});