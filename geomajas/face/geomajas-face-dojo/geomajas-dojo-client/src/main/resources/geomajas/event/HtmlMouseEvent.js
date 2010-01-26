dojo.provide("geomajas.event.HtmlMouseEvent");
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
dojo.require("geomajas.spatial.Coordinate");
dojo.require("geomajas.event.SimpleEvent");

dojo.declare("HtmlMouseEvent", null, {

	statics : {
		LEFT_MOUSE_BUTTON : dojo.isIE ? 1 : 0,
		RIGHT_MOUSE_BUTTON : 2,
        MIDDLE_BUTTON : dojo.isIE ? 4 : 1
	},

	originalMouseEvent: null,

	/**
	 * @fileoverview Wrapper around the DOM MouseEvent object.
	 * @class Wrapper around the DOM MouseEvent object. Has all sorts of handy
	 * functions to make it easier to handle mouse events. This constructor
	 * takes a MouseEvent object from the browser. It uses dojo to 'fix' the
	 * event (cross-browser compatibility).
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param event A browser's MouseEvent.
	 */
	constructor : function (/*MouseEvent*/event, /*Coordinate*/ offset) {
		this.originalMouseEvent = event;
		 
		/** The wrapped, and fixed (by dojo) event */
		this.event = dojo.fixEvent(event, event.target);
		if(offset){
			this.position = new Coordinate (this.event.clientX - offset.getX(), 
			                                this.event.clientY - offset.getY());
		} else {
			if (dojo.isMozilla) {
				this.position = new Coordinate (this.event.layerX, this.event.layerY);
			} else {
				// Google Chrome:
				this.position = new Coordinate (this.event.offsetX, this.event.offsetY);
			}
		}
	},

	/**
	 * Returns which mouse button was pressed.<br/>
	 * <ul>
	 *     <li>0=left</li>
	 *     <li>2=right</li>
	 * </ul>
	 */
	getButton : function () {
		return this.event.button;
	},

	/**
	 * Get the timestamp of this event as an integer.
	 */
	getWhen : function () {
		return this.event.timeStamp;
	},

	/**
	 * Get the position on the ?browser? of this event.
	 * @returns Returns a Coordinate object, giving the position in viewspace.
	 */
	getPosition : function () {
		return this.position;
	},

	setPosition : function (newPos) {
		this.position = newPos;
	},

	/**
	 * Was the "alt" button pressed while clicking?
	 * @return Returns: true or false.
	 */
	isAltDown : function () {
		return this.event.altKey;
	},

	/**
	 * Was the "shift" button pressed while clicking?
	 * @return Returns: true or false.
	 */
	isShiftDown : function () {
		return this.event.shiftKey;
	},

	/**
	 * Was the "control" button pressed while clicking?
	 * @return Returns: true or false.
	 */
	isCtrlDown : function () {
		return this.event.ctrlKey;
	},

	/**
	 * Get the "ID" attribute of the target dom element.
	 */
	getTargetId : function () {
		var element = this.event.target;
		if (element != null) {
			var id = null;
			try {
				id = element.getAttribute("id");
			} catch (e) {
				// USE element in Google Chrome don't have the getAttribute function.
			}
			var count = 0;
			while (id == null || id == "") {
				if (element.correspondingUseElement) {
					// Google Chrome compatibility:
					element = element.correspondingUseElement;
				} else {
					element = element.parentNode;
				}
				if (element == null || count >= 10) {
					return "";
				}
				try {
					id = element.getAttribute("id");
				} catch (e) {
					// USE element in Google Chrome don't have the getAttribute function.
				}
				count++;
			}
			return id;
		} else {
			return "";
		}
	},

	/**
	 * Get the target dom element.
	 */
	getTargetElement : function () {
		return this.event.target;
	},

	/**
	 * Get the direction the scroll wheel was turning in.
	 * @returns <ul>
	 *             <li>scroll-down: -1</li>
	 *             <li>scroll-up: 1</li>
	 *             <li>no scroll: 0</li>
	 *          </ul>
	 */
	getScrollDirection : function (){
		var delta = 0;
		if (!this.event) this.event = window.event;
		if (this.event.wheelDelta) {
			delta = this.event.wheelDelta/Math.abs(this.event.wheelDelta);
			if (window.opera) delta = -delta;
		} else if (this.event.detail) {
			delta = -this.event.detail/3;
		}
		return delta;
	},

	/**
	 * Stop any further propagation of this event.
	 */
	stopPropagation : function () {
		dojo.stopEvent(this.event);
	},

	getButtonString : function () {
		if(this.getButton() == this.statics.RIGHT_MOUSE_BUTTON){
			return "right";
		}
		else {
			return "left";
		}
	}
});
