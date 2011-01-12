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
