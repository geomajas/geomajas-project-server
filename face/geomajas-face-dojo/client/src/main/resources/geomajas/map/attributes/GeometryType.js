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