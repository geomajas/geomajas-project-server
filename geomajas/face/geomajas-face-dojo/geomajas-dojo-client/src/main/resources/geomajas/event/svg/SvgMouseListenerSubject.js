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

dojo.provide("geomajas.event.svg.SvgMouseListenerSubject");
dojo.require("dojox.collections.Dictionary");
dojo.require("geomajas.event.HtmlMouseEvent");
dojo.require("geomajas.event.ListenerSubject");

dojo.extend(MouseListenerSubject, {

	_afterInit : function () {
		this.lastMoveEvent = null;
		this.lastDownEvent = null;

		this.listeners = new dojox.collections.Dictionary();
		this.subject.addEventListener ("click",     dojo.hitch(this,"click"),     false);
		this.subject.addEventListener ("mousedown", dojo.hitch(this,"mousedown"), false);
		this.subject.addEventListener ("mouseup",   dojo.hitch(this,"mouseup"),   false);
		this.subject.addEventListener ("mousemove", dojo.hitch(this,"mousemove"), false);
		this.subject.addEventListener ("mouseover", dojo.hitch(this,"mouseover"), false);
		this.subject.addEventListener ("mouseout",  dojo.hitch(this,"mouseout"),  false);
		this.subject.addEventListener ("contextmenu", dojo.hitch(this,"contextmenu"),  false);
		this.subject.addEventListener ("dblclick",  dojo.hitch(this,"doubleclick"),  false);
	},

	/**
	 * Add a new MouseListener object to the subject SVG element.
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
	},

	click : function (evt) {	
	},

	doubleclick : function (evt) {
		var event = new HtmlMouseEvent(evt,this.offset);
		var e = this.listeners.getIterator();
		while(e.get()) {
		 	e.element.value.doubleClick(event);
		}
	},

	mousedown : function (evt) {
		var event = new HtmlMouseEvent(evt);
		this.lastDownEvent = event;
		if(event.getButton() != event.statics.RIGHT_MOUSE_BUTTON){
			var e = this.listeners.getIterator();
			while(e.get()) {
			 	e.element.value.mousePressed(event);
			}
		}
		if(this.isStopPropagation()){
			event.stopPropagation();
		}
	},

	mouseup : function (evt) {
		var event = new HtmlMouseEvent(evt);
		var e = this.listeners.getIterator();
		var doClick = (this.lastDownEvent && event.getPosition().equals(this.lastDownEvent.getPosition()));
		this.lastDownEvent = null;
		while(e.get()) {
			e.element.value.mouseReleased(event);
			if(doClick){
			 	e.element.value.mouseClicked(event);
			}
		}
		if(this.isStopPropagation()){
			event.stopPropagation();
		}
	},

	mousemove : function (evt) {
		var event = new HtmlMouseEvent(evt);
		if(this.lastMoveEvent && event.getPosition().equals(this.lastMoveEvent.getPosition())){
			return;
		}
		this.lastMoveEvent = event;
		var aDate = new Date();
		var diff = parseFloat(aDate.getTime() - this.getDate().getTime());
		if (diff >= parseFloat(1000/this.getFps())) { // time is in microseconds.
			var e = this.listeners.getIterator();
			while(e.get()) {
			 	e.element.value.mouseMoved(event);
			}
			this.setDate(aDate);
		}
		if(this.isStopPropagation()){
			event.stopPropagation();
		}
	},

	mouseover : function (evt) {
		var event = new HtmlMouseEvent(evt);
		var e = this.listeners.getIterator();
		while(e.get()) {
			 e.element.value.mouseEntered(event);
		}
		if(this.isStopPropagation()){
			event.stopPropagation();
		}
	},

	mouseout : function (evt) {
		var event = new HtmlMouseEvent(evt);
		var e = this.listeners.getIterator();
		while(e.get()) {
			 e.element.value.mouseExited(event);
		}
		if(this.isStopPropagation()){
			event.stopPropagation();
		}
	},

	contextmenu : function (evt) {
		var event = new HtmlMouseEvent(evt);
		var e = this.listeners.getIterator();
		while(e.get()) {
			 e.element.value.contextMenu(event);
		}
		if(this.listeners.count == 0 && this.preventContext){
			event.stopPropagation();
		}
	}
});