dojo.provide("geomajas.widget.attributes.AttributeEditorFactory");
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
dojo.require("geomajas.widget.attributes.AssociationAttributeStore");

dojo.declare("AttributeEditorFactory", null, {

	create : function (id , atDef, feature, srcNodeRef) {
		log.info("attribute def editing "+atDef.getName()+" type = "+atDef.getType());
		var params = {
			id : id,
			atDef : atDef, 
			feature : feature,
			value : this._createValue(atDef, feature),
			trim : true
		};

		var validator = atDef.getValidator();
		if (validator) {
			if (validator.constraints) {
				params["constraints"] = validator.constraints;
			}
			if (validator.toolTip) {
				params["promptMessage"] = validator.toolTip;
			}
			if (validator.errorMessage) {
				params["invalidMessage"] = validator.errorMessage;
			}
			if (validator.errorMessage) {
				params["rangeMessage"] = validator.errorMessage;
			}
			if (validator.required != null) {
				params["required"] = validator.required;
			}
		}

		if (atDef.getType() == "string" || atDef.getType() == "url" || atDef.getType() == "imgurl") {
			return this._createStringEditor(params,srcNodeRef);
		} else if (atDef.isInteger()) {
			return this._createIntegerEditor(params,srcNodeRef);
		} else if (atDef.getType() == "float" || atDef.getType() == "double") {
			return this._createFloatEditor(params,srcNodeRef);
		} else if (atDef.getType() == "currency") {
			return this._createCurrencyEditor(params,srcNodeRef);
		} else if (atDef.getType() == "boolean") {
			return this._createBooleanEditor(params,srcNodeRef);
		} else if (atDef.getType() == "date") {
			return this._createDateEditor(params,srcNodeRef);
		} else if (atDef.getType() == "many-to-one"){
			return this._createManyToOneEditor(params,srcNodeRef);
		} else if (atDef.getType() == "one-to-many"){
			var widget = this._createCompositionEditor(params,srcNodeRef);
			return widget;
		}
		return null;
	},

	getEditorValue : function (id , atDef) {
		var widget = dijit.byId(id);
		if (widget == null) {
			return null;
		}
		var value = widget.getValue ? widget.getValue() : widget.value;
		if (atDef.getType() == "string" || atDef.getType() == "url" || atDef.getType() == "imgurl") {
			return value;
		} else if (atDef.isInteger()) {
			if (isNaN(value)) {
				return null;
			}
			return parseInt(value);
		} else if (atDef.getType() == "float" || atDef.getType() == "double") {
			if (isNaN(value)) {
				return null;
			}
			return parseFloat(value);
		} else if (atDef.getType() == "currency") {
			if (isNaN(value)) {
				return null;
			}
			return parseFloat(value);
		} else if (atDef.getType() == "boolean") {
			if (widget.checked) { // widget.checked can be undefined.
				return true;
			}
			return false;
		} else if (atDef.getType() == "date") {
			return value;
		} else if(atDef.getType() == "many-to-one"){
			return this._getBeanFromStoreItem(atDef, widget);
		} else if (atDef.getType() == "one-to-many") {
			return value;
		}
	},

	/**
	 * @private
	 */
	_createStringEditor : function (params,srcNodeRef) {
		var extra = {
			trim: true
		};
		return new dijit.form.ValidationTextBox(dojo.mixin(extra,params), srcNodeRef);
	},

	/**
	 * @private
	 */
	_createIntegerEditor : function (params,srcNodeRef) {
		var extra = {
			trim: true,
			constraints:{places:0,min:0,max:99999999},
			maxLength: 15
		};
		return new dijit.form.NumberTextBox(dojo.mixin(extra,params), srcNodeRef);
	},

	/**
	 * @private
	 */
	_createFloatEditor : function (params,srcNodeRef) {
		var extra = {
			trim: true,
			//constraints:{min:0,max:99999999},
			places: 2,
			maxLength: 15
		};
		return new dijit.form.NumberTextBox(dojo.mixin(extra,params), srcNodeRef);
	},

	/**
	 * @private
	 */
	_createCurrencyEditor : function (params,srcNodeRef) {
		var extra = {
			trim: true,
			constraints:{min:0,max:99999999},
			currency: "EUR",
			maxLength: 15
		};
		return new dijit.form.CurrencyTextBox(dojo.mixin(extra,params), srcNodeRef);
	},

	/**
	 * @private
	 */
	_createBooleanEditor : function (params,srcNodeRef) {
		var extra = {
			checked: params.value
		};
		return new dijit.form.CheckBox(dojo.mixin(extra,params), srcNodeRef);
	},

	/**
	 * @private
	 */
	_createDateEditor : function (params,srcNodeRef) {
		var extra = {
			trim: true
		};
		return new dijit.form.DateTextBox(dojo.mixin(extra,params), srcNodeRef);
	},

	/**
	 * @private
	 */
	_createManyToOneEditor : function (params,srcNodeRef) {
		var store = new AssociationAttributeStore(params.atDef);
		var extra = {
			trim: true,
			searchAttr: params.atDef.getAssociationAttributeName(),
			store: store
		};
		return new geomajas.widget.attributes.NullableFilteringSelect(dojo.mixin(extra,params), srcNodeRef);
	},	

	/**
	 * @private
	 */
	_createCompositionEditor : function (params,srcNodeRef) {
		var extra = {};
		return new geomajas.widget.attributes.CompositionFloater(dojo.mixin(extra,params), srcNodeRef);
	},

	/**
	 * @private
	 */
	_createValue : function (atDef, feature) {
		var primitive = "";
		if (feature != null) {
			var value = feature.getAttributeValue(atDef.getName());
			primitive = atDef.getPrimitiveValue(value);
		}
		return primitive;
	},

	/**
	 * @private
	 */
	_createValidatorString : function (atDef, feature) {
		if (atDef.getValidator() != null && atDef.getValidator() != "") {
			return atDef.getValidator();
		}
		else {
			return null;
		}
	},

	/**
	 * A store item cannot be serialized to JSON by dojo, so we transform
	 * what we need to a simple bean.
	 * @private
	 */
	_getBeanFromStoreItem : function (atDef, widget) {
		var item = widget.item;
		if (!item) {
			// This happens in case the user has not changed the selectbox:
			var store = widget.params.store;
			var val = widget.value;
			var saveItem = function (theItem){
				item = theItem;
			}
			store.fetchItemByIdentity({identity:val, onItem:saveItem});
		}
		if (!item) {
			return null;
		}
		var bean = {};
		var javaClass = item.javaClass;
		if (javaClass instanceof Array) {
			bean["javaClass"] = javaClass[0];
		} else {
			bean["javaClass"] = javaClass;
		}

		var identifierName = atDef.getAssociationIdentifierName();
		var identifier = item[identifierName];
		if (identifier instanceof Array) {
			bean[identifierName] = identifier[0];
		} else {
			bean[identifierName] = identifier;
		}

		var attrs = atDef.getAssociationAttributeList();
		for (var i=0; i<attrs.length; i++) {
			var name = attrs[i].name;
			var value = item[name];
			if (value instanceof Array) {
				bean[name] = value[0];
			} else {
				bean[name] = value;
			}
		}
		return bean;
	}
});
