dojo.provide("geomajas.widget.attributes.OperatorSelect");
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
