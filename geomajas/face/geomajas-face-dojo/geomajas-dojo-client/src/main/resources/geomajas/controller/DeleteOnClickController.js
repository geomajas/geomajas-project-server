dojo.provide("geomajas.controller.DeleteOnClickController");
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
