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

dojo.provide("geomajas.event.vml.VmlMouseScrollListenerSubject");
dojo.require("dojox.collections.Dictionary");
dojo.require("geomajas.event.HtmlMouseEvent");
dojo.require("geomajas.event.ListenerSubject");

dojo.extend(MouseScrollListenerSubject, {

	_afterInit : function () {
		this.listeners = new dojox.collections.Dictionary();
		dojo.connect(this.subject, "onmousewheel", this, "scroll");
	},

	/**
	 * Add a new MouseScrollListener object to the subject SVG element.
	 * @param listener The MouseScrollListener object to be added to the list.
	 */
	addListener : function (/*MouseScrollListener*/listener) {
		if (!this.listeners.contains(listener.getName())) {
			this.listeners.add(listener.getName(), listener);
		}
	},

	/**
	 * Remove an existing MouseScrollListener object from the list. If the listener was not
	 * found, nothing happens.
	 * @param listener The MouseScrollListener object to be removed from the list. 
	 */
	removeListener : function (/*MouseScrollListener*/listener) {
		if (this.listeners.contains(listener.getName())) {
			this.listeners.remove(listener.getName());
		}
	},

	scroll : function (evt) {	
		var event = new HtmlMouseEvent(evt); 
		var e = this.listeners.getIterator();
		while(e.get()) {
		 	e.element.value.mouseScrolled(event);
		}
	}

});