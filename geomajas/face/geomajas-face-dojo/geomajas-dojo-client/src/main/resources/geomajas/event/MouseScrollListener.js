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