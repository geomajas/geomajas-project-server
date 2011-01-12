dojo.provide("geomajas.map.attributes.AttributeFormatterFactory");
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
					if (attributeValue == null || attributeValue.list == null) {
						return "";
					}
					var simpleValue = [];
					for (var i = 0; i < attributeValue.list.length; i++) {
						var associationName = atDef.getAssociationAttributeName(); 
						simpleValue.push("{" + attributeValue.list[i].attributes.map[associationName].value + "}");
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
