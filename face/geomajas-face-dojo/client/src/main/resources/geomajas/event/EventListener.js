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