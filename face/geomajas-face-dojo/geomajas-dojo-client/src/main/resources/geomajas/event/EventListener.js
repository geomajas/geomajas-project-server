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

dojo.provide("geomajas.event.EventListener");
dojo.declare("EventListener", null, {

	/**
	 * @fileoverview Basic event listener interface.
	 * @class General interface for a Listener. A listener can be registered to
	 * a ListenerSubject an executes certain functions when the subject
	 * receives certain events.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor: function () {
	},

	/**
	 * This function is used to identify listeners when they are added to
	 * a ListenerSubject. Always make sure that each Listener added to a subject
	 * has a unique name!
	 * @returns The name for this listeners as a String.
	 */
	getName : function () {
		alert("EventListener:getName, implement me !");
	}
});