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

dojo.provide("geomajas.event.MouseScrollListener");
dojo.require("geomajas.event.EventListener");

dojo.declare("MouseScrollListener", EventListener, {

	/**
	 * @fileoverview Interface for dealing with mousescroll events.
	 * @class Listener interface for the mouse-scroll events supported in the
	 * DOM specification.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends EventListener
	 */
	constructor : function () {
	},

	/**
	 * Returns a unique name.
	 */
	getName : function () {
		return "mouseScrollListener";
	},

	/**
	 * Function to be executed when the subject detects a mouse-scroll-event.
	 * @param event The HtmlMouseEvent object.
	 */
	mouseScrolled : function (/*HtmlMouseEvent*/event) {
	}

});