dojo.provide("geomajas.map.attributes.AttributeFormatterFactory");
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
dojo.declare("AttributeFormatterFactory", null, {

	/**
	 * @fileoverview Creates formatter functions for attributes.
	 * @class Class that creates formatter functions for attributes, based on
	 * the attribute definition.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
		/** The default formatting function; returns the input value. */
		this.defaultFormatter = function (attributeValue) {
			return attributeValue;
		};
	},

	/**
	 * Create a formatting function for a specific type of attribute. This
	 * formatting function in turn returns a string.
	 *
	 * @param atDef An attribute definition object.
	 * @return Return a formatting function for the attribute definition.
	 */
	create : function (atDef) {
		var formatter = this.defaultFormatter;
		if (atDef != null) {
			var type = atDef.type;
			if (type == "date" || (type.value != null && type.value == "date")) {
				formatter = function (attributeValue) {
					if (!attributeValue || attributeValue == null) {
						return "";
					}
					return dojo.date.locale.format(attributeValue, {selector:"date"});
				};
			} else if (type == "float" || type == "double") {
				formatter = function (attributeValue) {
					if (attributeValue == null) {
						return "";
					}
					return dojo.number.format(attributeValue);
				};
			} else if (type == "currency") {
				formatter = function (attributeValue) {
					if (attributeValue == null) {
						return "";
					}
					return dojo.currency.format(attributeValue, {currency:"EUR"});
				};
			} else if (type == "many-to-one") {
				formatter = function (attributeValue) {
					if (attributeValue == null) {
						return "";
					}
					var associationName =  atDef.getAssociationAttributeName();
					return attributeValue.attributes.map[associationName].value;
				};
			} else if (type == "one-to-many") {
				formatter = function (attributeValue){
					if (attributeValue == null) {
						return "";
					}
					var simpleValue = [];
					for (var i = 0; i < attributeValue.length; i++) {
						var result = [];
						for (var key in attributeValue[i]) {
							if (key != "id" && key != "javaClass"){
								result.push(key + "=" + attributeValue[i][key]);
							}
						}
						simpleValue.push("{" + result.join(", ") + "}");
					}
					return simpleValue;
				};
			} else if (type == "url") {
				formatter = function (attributeValue) {
					if(attributeValue == null || attributeValue == '') {
						return "";
					}
					return "<a href=\""+ attributeValue + "\" target=\"_new\">" + attributeValue + "</a>";
				};
			} else if (type == "imgurl") {
				formatter = function (attributeValue) {
					if(attributeValue == null || attributeValue == '') {
						return "";
					}
					return "<a href=\""+ attributeValue + "\" target=\"_new\"><img src=\""+ attributeValue + "\" style=\"height: 100px\" /></a>";
				};
			}
		}
		return formatter;
	},
	
	/**
	 * return css.style if needed
	 */
	getStyle : function (atDef) {
		if (atDef != null) {
			var type = atDef.type;
			if (type == "date" || (type.value != null && type.value == "date")) {
				return "text-align:center;";
				
			} else if (type == "float" || type == "double") {
				return "text-align:right;";
				
			} else if (type == "currency") {
				return "text-align:right;";
				
			} else if (type == "many-to-one") {
				
			} else if (type == "one-to-many") {
				
			}
		}
		return null;
	}
});
