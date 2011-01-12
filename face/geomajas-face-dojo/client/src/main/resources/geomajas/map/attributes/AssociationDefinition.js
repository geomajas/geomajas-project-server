dojo.provide("geomajas.map.attributes.AssociationDefinition");
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
				return attributeValue[identifierName].value;
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

	getAssociationIdentifierType : function () {
		if (this.object != null) {
			return this.object.identifier.type.value;
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
	 * Each association has an attribute used for labeling. This is always the
	 * first attribute in the associated type. This function will return it.
	 */
	getAssociationAttribute : function () {
		if (this.object != null) {
			var assoAttrs = this.object.attributes.list;
			// The first attribute is used as labeling attribute.
			return assoAttrs[0];
		}
		return null;
	},

	/**
	 * Each association has an attribute used for labeling. This is always the
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
	
	getAssociationAttributeType : function () {
		var associated = this.getAssociationAttribute();
		if (associated != null) {
			return associated.type.value;
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
			return this.object.attributes.list;
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
