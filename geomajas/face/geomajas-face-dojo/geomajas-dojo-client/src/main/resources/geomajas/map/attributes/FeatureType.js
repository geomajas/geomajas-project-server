dojo.provide("geomajas.map.attributes.FeatureType");
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
dojo.require("geomajas.map.attributes.GeometryType");

dojo.declare("FeatureInfo", null, {

	/**
	 * @fileoverview Attribute handler for a vector layer.
	 * @class Attribute handler for a vector layer.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param attributes {@link AttributeDefinition} dictionary. Attribute
	 *                   names are used as key.
	 * @param geometryType The geometric type config object.
	 */
	constructor : function (attributes, geometryType) {
		if (attributes) {
			this.attributes = attributes;
		} else {
			this.attributes = new dojox.collections.Dictionary();
		}
		
		this.geometryType = geometryType;
	},



	//-------------------------------------------------------------------------
	// FeatureInfo specific functions:
	//-------------------------------------------------------------------------

	/**
	 * Returns a dictionary of all the identifying attributes.
	 * Attribute-names are used as keys.
	 */
	getIdentifyingAttributes : function () {
		var map = new dojox.collections.Dictionary();
		var atDefs = this.attributes.getValueList();
		for (var i=0; i<atDefs.length; i++) {
			var atDef = atDefs[i];
			if (atDef.isIdentifying()) {
				map.add(atDef.getName(), atDef);
			}
		}
		return map;
	},

	getVisibleIdentifyingAttributes : function () {
		var map = new dojox.collections.Dictionary();
		var atDefs = this.attributes.getValueList();
		for (var i=0; i<atDefs.length; i++) {
			var atDef = atDefs[i];
			if (atDef.isIdentifying() && !atDef.isHidden()) {
				map.add(atDef.getName(), atDef);
			}
		}
		return map;
	},

	/**
	 * Return an {@link AttributeDefinition} object by it's label, or null if
	 * it can't be found.
	 */
	getAttributeByLabel : function (label) {
		var atDefs = this.attributes.getValueList();
		for (var i=0; i<atDefs.length; i++) {
			var atDef = atDefs[i];
			if (atDef.getLabel() == label) {
				return atDef;
			}
		}
		return null;		
	},

	/**
	 * Return an {@link AttributeDefinition} object by it's name, or null if
	 * it can't be found.
	 */
	getAttributeByName : function (name) {
		return this.attributes.item(name);
	},



	//-------------------------------------------------------------------------
	// Getters and setters:
	//-------------------------------------------------------------------------

	/**
	 * Returns a dictionary of all the attributes.
	 */
	getAttributes : function () {
		return this.attributes;
	},

	getVisibleAttributes : function () {
		var map = new dojox.collections.Dictionary();
		var atDefs = this.attributes.getValueList();
		for (var i=0; i<atDefs.length; i++) {
			var atDef = atDefs[i];
			if (!atDef.isHidden()) {
				map.add(atDef.getName(), atDef);
			}
		}
		return map;
	},

	/**
	 * Sets a new dictionary of attributes.
	 */
	setAttributes : function () {
		this.attributes = attributes;
	},

	/**
	 * Returns the GeometryType.
	 */
	getGeometryType : function () {
		return this.geometryType;
	},

	/**
	 * Sets a new GeometryType.
	 */
	setGeometryType : function () {
		this.geometryType = geometryType;
	}
});
