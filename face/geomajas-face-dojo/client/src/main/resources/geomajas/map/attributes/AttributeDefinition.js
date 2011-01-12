dojo.provide("geomajas.map.attributes.AttributeDefinition");
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
