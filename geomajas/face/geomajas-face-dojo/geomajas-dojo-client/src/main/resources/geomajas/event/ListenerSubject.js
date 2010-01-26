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

dojo.provide("geomajas.event.ListenerSubject");
dojo.declare("ListenerSubject", null, {

	/**
	 * @fileoverview Connection point for EventListener objects.
	 * @class Interface for elements that can be subject to certain listeners.
	 * Once such an element is subject to listeners, it is possible to add
	 * and remove these listeners at any time.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * Add a new listener to this subject.
	 * @param listener An EventListener implementation.
	 */
	addListener : function (/*EventListener*/listener) {
		alert("ListenerSubject:addListener , implement me !");
	},

	/**
	 * Remove an existing listener from this subject.
	 * @param listener An EventListener implementation.
	 */
	removeListener : function (/*EventListener*/listener) {
		alert("ListenerSubject:removeListener , implement me !");
	}

});
