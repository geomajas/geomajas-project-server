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