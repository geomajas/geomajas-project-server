dojo.provide("geomajas.map.attributes.AssociationDefinition");
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
dojo.require("geomajas.map.attributes.AttributeDefinition");

dojo.declare("AssociationDefinition", AttributeDefinition, {

	/**
	 * @fileoverview Definition of an association attribute in a layer's configuration.
	 * @class Definition of an association attribute in a layer's configuration.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param name The attribute's real name.
	 * @param label The attribute's label.
	 * @param validator The attribute's validing regular expression. 
	 */
	constructor : function (layer, name, label, validator, type, editable, identifying, hidden, object) {
		/** The attribute's associated object */
		if (object) {
			this.object = object;
		} else {
			this.object = null;
		}
	},

	//-------------------------------------------------------------------------
	// Overriding AttributeDefinition functions:
	//-------------------------------------------------------------------------

	isAssociation : function () {
		return true;
	},

	getPrimitiveValue : function (attributeValue) {
		// many-to-one: return the identifier.
		if (this.type == "many-to-one") {
			var identifierName = this.getAssociationIdentifierName();
			if(attributeValue != null){
				return attributeValue[identifierName];
			} else {
				return null;
			}
		} 

		// one-to-many: return an array of hasmaps:
		else if (this.type == "one-to-many") {
			return attributeValue;
		}
		return attributeValue;
	},

	//-------------------------------------------------------------------------
	// Association specific functions:
	//-------------------------------------------------------------------------

	/**
	 * Return the name of the associated type. This basically is a javaclass.
	 */
	getAssociationType : function () {
		if (this.object != null) {
			return this.object.name;
		}
		return null;
	},

	/**
	 * Return the identifying attribute of an association attribute.
	 */
	getAssociationIdentifier : function () {
		if (this.object != null) {
			return this.object.identifier;
		}
		return null;
	},

	/**
	 * Return the name of the identifying attribute of an association
	 * attribute.
	 */
	getAssociationIdentifierName : function () {
		if (this.object != null) {
			return this.object.identifier.name;
		}
		return null;
	},

	/**
	 * Each association has an attribute used for labelling. This is always the
	 * first attribute in the associated type. This function will return it.
	 */
	getAssociationAttribute : function () {
		if (this.object != null) {
			var assoAttrs = this.object.attributes.attributeOrAssociation.list;
			// The first attribute is used as labelling attribute.
			return assoAttrs[0];
		}
		return null;
	},

	/**
	 * Each association has an attribute used for labelling. This is always the
	 * first attribute in the associated type. This function will return it's
	 * name.
	 */
	getAssociationAttributeName : function () {
		var associated = this.getAssociationAttribute();
		if (associated != null) {
			return associated.name;
		}
		return null;
	},

	/**
	 * Each association has an attribute used for labelling. This is always the
	 * first attribute in the associated type. This function will return it's
	 * label. PDG: Not used!
	 */
	getAssociationAttributeLabel : function () {
		var associated = this.getAssociationAttribute();
		if (associated != null) {
			return associated.label;
		}
		return null;
	},

	/**
	 * Returns the full list of attributes of the associated object as an array.
	 */
	getAssociationAttributeList : function () {
		if (this.object != null) {
			return this.object.attributes.attributeOrAssociation.list;
		}
		return null;
	},

	//-------------------------------------------------------------------------
	// Getters and setters:
	//-------------------------------------------------------------------------

	getObject : function () {
		return this.object;
	},

	setObject : function (object) {
		this.object = object;
	}
});
