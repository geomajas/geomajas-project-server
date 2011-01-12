dojo.provide("geomajas.map.attributes.FeatureType");
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
