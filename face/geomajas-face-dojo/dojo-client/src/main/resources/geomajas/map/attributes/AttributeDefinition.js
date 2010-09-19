dojo.provide("geomajas.map.attributes.AttributeDefinition");
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
dojo.declare("AttributeDefinition", null, {

	/**
	 * @class Definition of an attribute in a layer's configuration.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param name The attribute's real name.
	 * @param label The attribute's label.
	 * @param validator The attribute's validing regular expression. 
	 */
	constructor : function (layer, name, label, validator, type, editable, identifying, hidden) {
		this.javaClass = "";
		
		/** Reference to the layer this attribute belongs to. */
		this.layer = layer;
		
		/** The attribute's real name. */
		this.name = name;

		/** The attribute's label. */
		this.label = label;

		/** The attribute's type. */
		if (type) {
			this.type = type;
		} else {
			this.type = null;
		}

		/** Is the attribute required ? */
		this.required = false;

		/** The attribute's validing regular expression. */
		if (validator) {
			this.validator = validator;
			if (this.validator.constraints) {
				try {
					this.validator.constraints = this._parseConstraints(this.validator.constraints);
				} catch (e){
					log.error ("Error while parsing constraints for attribute "+this.name);
				}
			}
		} else {
			this.validator = null;
		}	

		/** Is this attribute editable? */
		if (editable) {
			this.editable = editable;
		} else {
			this.editable = null;
		}

		/** Is this attribute identifying? */
		if (identifying) {
			this.identifying = identifying;
		} else {
			this.identifying = null;
		}
		
		if (hidden) {
			this.hidden = hidden;
		} else {
			this.hidden = false;
		}
	},



	//-------------------------------------------------------------------------
	// AttributeDefinition specific functions:
	//-------------------------------------------------------------------------

	isAssociation : function () {
		return false;
	},

	isInteger : function () {
		return this.type == "integer" || this.type == "long" || this.type == "short"; 
	},

	getPrimitiveValue : function (attributeValue) {
		return attributeValue;
	},

	//-------------------------------------------------------------------------
	// Getters and setters:
	//-------------------------------------------------------------------------

	getLayer : function () {
		return this.layer;
	},

	getName : function () {
		return this.name;
	},

	setName : function (name) {
		this.name = name;
	},

	getLabel : function () {
		return this.label;
	},

	setLabel : function (label) {
		this.label = label;
	},

	getValidator : function () {
		return this.validator;
	},

	setValidator : function (validator) {
		this.validator = validator;
	},

	getType : function () {
		return this.type;
	},

	setType : function (type) {
		this.type = type;
	},

	isEditable : function () {
		return this.editable;
	},

	setEditable : function (editable) {
		this.editable = editable;
	},

	isIdentifying : function () {
		return this.identifying;
	},

	setIdentifying : function (identifying) {
		this.identifying = identifying;
	},

	isHidden : function () {
		return this.hidden;
	},

	setHidden : function (hidden) {
		this.hidden = hidden;
	},

	_parseConstraints : function(constraints){
		var result = {};
		for(var i = 0; i < constraints.list.length; i++){
			var constraint = constraints.list[i];
			if (constraint.javaClass="org.geomajas.configuration.DecimalMinConstraintInfo") {
				result.min = constraint.min;
			} else if (constraint.javaClass="org.geomajas.configuration.DecimalMaxConstraintInfo") {
				result.max = constraint.max;
			} else if (constraint.javaClass="org.geomajas.configuration.DigitsConstraintInfo") {
				result.pattern = this._createDigitMask(constraint.integer,constraint.fractional);
			} else if (constraint.javaClass="org.geomajas.configuration.FutureConstraintInfo") {
				result.min = new Date();
			} else if (constraint.javaClass="org.geomajas.configuration.MaxConstraintInfo") {
				result.max = constraint.max;
			} else if (constraint.javaClass="org.geomajas.configuration.MinConstraintInfo") {
				result.min = constraint.min;
			} else if (constraint.javaClass="org.geomajas.configuration.PastConstraintInfo") {
				result.max = new Date();
			} else if (constraint.javaClass="org.geomajas.configuration.PatternConstraintInfo") {
				result.regExp = constraint.regexp;
			} else if (constraint.javaClass="org.geomajas.configuration.SizeConstraintInfo") {
				result.regExp = ".{"+constraint.min+","+constraint.max+"}";
			} else if (constraint.javaClass="org.geomajas.configuration.NotNullConstraintInfo") {
				this.required = true;
			}
		}
		return result;
	},

	_createDigitMask : function(integer, fraction){
		 return dojo.string.rep("#",integer)+"."+ dojo.string.rep("#",fraction);
	}
});
