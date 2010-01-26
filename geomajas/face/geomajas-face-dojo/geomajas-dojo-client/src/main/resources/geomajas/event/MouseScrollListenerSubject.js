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