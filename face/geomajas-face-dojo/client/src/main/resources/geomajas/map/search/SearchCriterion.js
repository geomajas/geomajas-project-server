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

dojo.provide("geomajas.map.search.SearchCriterion");
dojo.declare("SearchCriterion", null, {

	/**
	 * @fileoverview One criterion for a search request.
	 * @class One criterion for a search request.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param attributeDefinition The {@link AttributeDefinition} object we
	 *                            are to search for.
	 * @param operator The search operator as a string (like, =, <, ...).
	 * @param value The value literal. Can be complex in case the attribute is
	 *              an association.
	 */
	constructor : function (attributeDefinition, operator, value) {
		this.attributeDefinition = attributeDefinition;
		this.operator = operator;
		this.value = value;
	},

	//-------------------------------------------------------------------------
	// SearchCriterion specific functions:
	//-------------------------------------------------------------------------

	/**
	 * Transforms the criterion to a string.
	 */
	toString : function () {
		return this.attributeDefinition.getName()+" "+this.operator+" "+this.value;
	},

	/**
	 * Prepares this searchcriterion for sending to the server through JSON.
	 * The serverside representation of this class, accept attributeName,
	 * operator and value, all as string. This means that in case of an
	 * association, we need to send the primitive value!
	 * @return Returns a JSON serializable object.
	 */
	toJSON : function () {
		var primitiveValue = this.value;
		var attributeName = this.attributeDefinition.getName();
		if (this.attributeDefinition.isAssociation()) {
			attributeName += "." + this.attributeDefinition.getAssociationIdentifierName();
			primitiveValue = this.value[this.attributeDefinition.getAssociationIdentifierName()];
		}
		return {
			javaClass : "org.geomajas.layer.feature.SearchCriterion",
			attributeName : attributeName,
			operator : this.operator,
			value : primitiveValue
		};
	},



	//-------------------------------------------------------------------------
	// Getters and setters:
	//-------------------------------------------------------------------------

	getAttributeDefinition : function () {
		return this.attributeDefinition;
	},

	setAttributeDefinition : function (attributeDefinition) {
		this.attributeDefinition = attributeDefinition;
	},

	getOperator : function () {
		return this.operator;
	},

	setOperator : function (operator) {
		this.operator = operator;
	},

	getValue : function () {
		return this.value;
	},

	setValue : function (value) {
		this.value = value;
	}
});