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

dojo.provide("geomajas.event.vml.VmlMouseListenerSubject");
dojo.require("dojox.collections.Dictionary");
dojo.require("geomajas.event.HtmlMouseEvent");
dojo.require("geomajas.event.ListenerSubject");

dojo.extend(MouseListenerSubject, {

	_afterInit : function () {
		this.lastMoveEvent = null;
		this.lastDownEvent = null;
		this.cleaningUp = false;		

		this.listeners = new dojox.collections.Dictionary();
		
		
		//DVB: subject has a 0,0 offset from parent,test with parent map DIV which has an offset
		//left and top to the page border
		/*this.offset = new Coordinate(
			this._findposx(this.subject) + 1,
			this._findposy(this.subject) + 1
			);
		log.debug("vml offset = " + this.offset.getX() +","+  this.offset.getY());
		*/
		
		
		dojo.connect(this.subject, "onclick", this, "click");
		dojo.connect(this.subject, "onmousedown", this, "mousedown");
		dojo.connect(this.subject, "onmouseup", this, "mouseup");
		dojo.connect(this.subject, "onmousemove", this, "mousemove");
		dojo.connect(this.subject, "onmouseover", this, "mouseover");
		dojo.connect(this.subject, "onmouseout", this, "mouseout");
		dojo.connect(this.subject, "oncontextmenu", this, "contextmenu");
		dojo.connect(this.subject, "ondblclick", this, "doubleclick");
		// always stop propagation of this event !!! 
		dojo.connect(this.subject, "ondragstart", this, "dragstart");
		
		
		// We would rather use dojo.addOnUnload(this, "cleanUp"); here, but dojo unloading seems buggy
		if(dojo.isIE) {
			window.attachEvent ("onunload",  dojo.hitch(this,"cleanUp"));
		}		
	},

	/**
	 * Cleanup function that destroys the links between JS/dojo and DOM objects to avoid
	 * the memory leak bug in Internet Explorer.
	 */
	cleanUp : function () {
		this.cleaningUp = true;
		this.lastMoveEvent = null;
		this.lastDownEvent = null;
	},

	/**
	 * Add a new MouseListener object to the subject VML element.
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

	dragstart : function (evt) {
		log.info("VmlMouseListenerSubject.dragstart(event)");
		var event = new HtmlMouseEvent(evt,this.offset);
		if(this.isStopPropagation()){
			event.stopPropagation();
		}		
	},

	mousedown : function (evt) {
		log.info("VmlMouseListenerSubject.mousedown(event)");
		if(this.cleaningUp) {
			return;
		}
		var event = new HtmlMouseEvent(evt,this.offset);
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
		log.info("VmlMouseListenerSubject.mouseup(event)");
		if(this.cleaningUp) {
			return;
		}
		var event = new HtmlMouseEvent(evt,this.offset);
		var e = this.listeners.getIterator();
		var doClick = (this.lastDownEvent && event.getPosition().equals(this.lastDownEvent.getPosition()));
		this.lastDownEvent = null;
		while(e.get()) {
			log.info("mouseReleased(event)");
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
		if(this.cleaningUp) {
			return;
		}
		var event = new HtmlMouseEvent(evt,this.offset);
		
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
		//log.info("VmlMouseListenerSubject.mouseover(event)");	
		if(this.cleaningUp) {
			return;
		}
		var event = new HtmlMouseEvent(evt,this.offset);
		var e = this.listeners.getIterator();
		while(e.get()) {
			 e.element.value.mouseEntered(event);
		}
		if(this.isStopPropagation()){
			event.stopPropagation();
		}
	},

	mouseout : function (evt) {
		//log.info("VmlMouseListenerSubject.mouseout(event)");	
		if(this.cleaningUp) {
			return;
		}
		var event = new HtmlMouseEvent(evt,this.offset);
		var e = this.listeners.getIterator();
		while(e.get()) {
			 e.element.value.mouseExited(event);
		}
		if(this.isStopPropagation()){
			event.stopPropagation();
		}
	},

	contextmenu : function (evt) {
		//log.info("VmlMouseListenerSubject.contextmenu(event)");	
		if(this.cleaningUp) {
			return;
		}
		var event = new HtmlMouseEvent(evt,this.offset);
		var e = this.listeners.getIterator();
		while(e.get()) {
			 e.element.value.contextMenu(event);
		}
		if(this.listeners.count == 0 && this.preventContext){
			event.stopPropagation();
		}
	},

/*	_findposx: function (obj) { 
	    var curleft = 0; 
	    if (obj.offsetParent) 
	    { 
	        while (obj.offsetParent) 
	        { 
	            curleft += obj.offsetLeft;
	            curleft += obj.clientLeft; 
	            obj = obj.offsetParent; 
	        } 
	    } 
	    else if (obj.x) 
	        curleft += obj.x; 
	         
	    return curleft; 
	}, 
	
	
	_findposy : function (obj) { 
	    var curtop = 0; 
	    if (obj.offsetParent) 
	    { 
	        while (obj.offsetParent) 
	        { 
	            curtop += obj.offsetTop;
	            curtop += obj.clientTop; 
	            obj = obj.offsetParent; 
	        } 
	    } 
	    else if (obj.y) 
	        curtop += obj.y; 
	     
	    return curtop; 
	},
	*/
	setOffset : function (/*Coords*/ coords)
	{
		this.offset = coords;
	}
	
	
});