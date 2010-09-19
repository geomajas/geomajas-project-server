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