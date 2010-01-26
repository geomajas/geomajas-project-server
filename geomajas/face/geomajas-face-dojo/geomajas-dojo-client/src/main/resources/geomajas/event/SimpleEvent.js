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

dojo.provide("geomajas.event.SimpleEvent");
dojo.require("geomajas.spatial.Coordinate");

dojo.declare("SimpleEvent", null, {

	/**
	 * @fileoverview Simple mouse event wrapper.
	 * @class This constructor takes a {@link HtmlMouseEvent} object, to store
	 * it's most basic and important values. Used when dealing with Rightmouse
	 * menus. Since the HtmlMouseEvent uses dojo's fixEvent function, and that
	 * function keeps an event singleton, we needed something extra to store
	 * the original event in. When the user clicks one of the actions in the
	 * rightmouse menu, it is possible to reach both that click event, as this
	 * simple event which relates to the original event that made the menu show
	 * up (some actions/controller need those coordinates).
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param event A HtmlMouseEvent. We store the most important values.
	 */
	constructor : function (/*HtmlMouseEvent*/event) {
		this.position = event.getPosition();
		this.targetElement = event.getTargetElement();
		this.targetId = event.getTargetId();
		this.cancelBubble = event.event.cancelBubble;
	},

	/**
	 * Get the position on the ?browser? of this event.
	 * @returns Returns a Coordinate object, giving the position in viewspace.
	 */
	getPosition : function () {
		return this.position;
	},

	/**
	 * Get the "ID" attribute of the target dom element.
	 */
	getTargetId : function () {
		return this.targetId;
	},

	/**
	 * Get the target dom element.
	 */
	getTargetElement : function () {
		return this.targetElement;
	}
	
});