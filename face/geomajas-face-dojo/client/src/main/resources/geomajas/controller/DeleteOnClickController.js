dojo.provide("geomajas.controller.DeleteOnClickController");
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
dojo.require("geomajas.event.MouseListener");

dojo.declare("DeleteOnClickController", MouseListener, {

	/**
	 * @fileoverview Mouselistener that deletes a given object onclick.
	 * @class Mouselistener that deletes a given object onclick.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends MouseListener
	 * @param mapWidget Reference to the MapWidget.
	 * @param object The object to delete from the map.
	 */
	constructor : function (mapWidget, object) {
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;
		
		this.object = object;
	},

	/**
	 * Return a unique name.
	 */
	getName : function () {
		return "DeleteOnClickController";
	},

	/**
	 * Delete the object in question onclick.
	 */
	mouseClicked : function (event) {
		if (event.getButton() != event.statics.RIGHT_MOUSE_BUTTON) {
			dojo.publish (this.mapWidget.getRenderTopic(), [this.object, "delete"]);
		}
	}
});
