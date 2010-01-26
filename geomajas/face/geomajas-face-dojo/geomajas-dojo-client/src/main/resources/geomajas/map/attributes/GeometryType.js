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

dojo.provide("geomajas.map.attributes.GeometryType");
dojo.declare("GeometryType", null, {

	/**
	 * @fileoverview Definition of the geometric type, as defined in the config XSD.
	 * @class Definition of the geometric type, as defined in the config XSD.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
		this.name = null;
		this.crs = null;
		this.editable = false;
	},



	//-------------------------------------------------------------------------
	// GeometryType specific functions:
	//-------------------------------------------------------------------------

	/**
	 * Read the json equivalent of this class, and fill in all the fields.
	 * @param json The json object.
	 */
	fromJSON : function (json) {
		this.name = json.name;
		this.editable = json.editable;
	},



	//-------------------------------------------------------------------------
	// Getters and setters:
	//-------------------------------------------------------------------------

	getName : function () {
		return this.name;
	},

	setName : function (name) {
		this.name = name;
	},

	isEditable: function () {
		return this.editable;
	},

	setEditable : function (editable) {
		this.editable = editable;
	}
});