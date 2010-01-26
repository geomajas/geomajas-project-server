dojo.provide("geomajas.widget.attributes.OperatorSelect");
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
dojo.require("dijit.form.FilteringSelect");

/**
 * <p>
 * Extension of dojo's 'dijit.form.FilteringSelect' widget that shows the
 * operators supported for a certain type of attribute in a selectbox.
 * Simply apply an {@link AttributeDefinition} object, and this widget will
 * display a list of operators depending on it's type.
 * </p>
 * <p>
 * For example: When the attribute type is 'integer', the following operators
 * are shown: '=', '<>', '<', '<=', '>', '>='.
 * </p>
 * @author Pieter De Graef
 */
dojo.declare("geomajas.widget.attributes.OperatorSelect", dijit.form.FilteringSelect, {

	layer : null,
	attributeDefinition : null,

	/**
	 * Sets a new layer object. This is only needed if you intend to use
	 * the 'setAttributeLabel' or 'setAttributeName' functions to set
	 * AttributeDefinitions.
	 * @param layer VectorLayer object.
	 */
	setLayer : function (layer) {
		this.layer = layer;
	},

	/**
	 * Sets an AttributeDefinition object. This will change to operator list
	 * available.
	 */
	setAttributeDefinition : function (attributeDefinition) {
		this.attributeDefinition = attributeDefinition;
		this._onAttributeDefinitionChange();
	},

	/**
	 * Uses the label of an AttributeDefinition to search for the correct
	 * AttributeDefinition object. In order to do this, the layer has to be
	 * set first! Calls 'setAttributeDefinition'.
	 */
	setAttributeLabel : function (label) {
		if (this.layer) {
			var attribute = this.layer.getFeatureType().getAttributeByLabel(label);
			this.setAttributeDefinition(attribute);
		}
	},

	/**
	 * Uses the name of an AttributeDefinition to search for the correct
	 * AttributeDefinition object. In order to do this, the layer has to be
	 * set first! Calls 'setAttributeDefinition'.
	 */
	setAttributeName : function (name) {
		if (this.layer) {
			var attribute = this.layer.getFeatureType().getAttributeByName(name);
			this.setAttributeDefinition(attribute);
		}
	},

	/**
	 * @private
	 */
	_onAttributeDefinitionChange : function () {
		if (this.attributeDefinition) {
			var type = this.attributeDefinition.getType();
			var jsonData = {identifier:"identifier", items: [] };
			if (type == "string" || type == "url" || type == "imgurl") {
				jsonData.items.push({name:"contains", identifier:"contains"});
				jsonData.items.push({name:"like", identifier:"like"});
			} else if (type == "integer" || type == "long" || type == "short") {
				jsonData.items.push({name:"=", identifier:"="});
				jsonData.items.push({name:"<>", identifier:"<>"});
				jsonData.items.push({name:"<", identifier:"<"});
				jsonData.items.push({name:"<=", identifier:"<="});
				jsonData.items.push({name:">", identifier:">"});
				jsonData.items.push({name:">=", identifier:">="});
			} else if (type == "float" || type == "double" || type == "currency") {
				jsonData.items.push({name:"=", identifier:"="});
				jsonData.items.push({name:"<>", identifier:"<>"});
				jsonData.items.push({name:"<", identifier:"<"});
				jsonData.items.push({name:"<=", identifier:"<="});
				jsonData.items.push({name:">", identifier:">"});
				jsonData.items.push({name:">=", identifier:">="});
			} else if (type == "boolean") {
				jsonData.items.push({name:"=", identifier:"="});
				jsonData.items.push({name:"<>", identifier:"<>"});
			} else if (type == "date") {
				jsonData.items.push({name:"=", identifier:"="});
				jsonData.items.push({name:"<>", identifier:"<>"});
				jsonData.items.push({name:"<", identifier:"<"});
				jsonData.items.push({name:"<=", identifier:"<="});
				jsonData.items.push({name:">", identifier:">"});
				jsonData.items.push({name:">=", identifier:">="});
			} else if (type == "many-to-one") {
				jsonData.items.push({name:"=", identifier:"="});
				jsonData.items.push({name:"<>", identifier:"<>"});
			}
			this.store = new dojo.data.ItemFileWriteStore( { data: jsonData } );
			this.setValue(jsonData.items[0].name); // Set the first operator as value
		}
	}
});
