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
		var event = new HtmlMouseEvent(evt,this.offset);
		if(this.isStopPropagation()){
			event.stopPropagation();
		}
	},

	doubleclick : function (evt) {
		var event = new HtmlMouseEvent(evt,this.offset);
		var e = this.listeners.getIterator();
		while(e.get()) {
		 	e.element.value.doubleClick(event);
		}
		if(this.isStopPropagation()){
			event.stopPropagation();
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