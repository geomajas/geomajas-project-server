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

dojo.provide("geomajas.widget.SearchTable");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit._Container");
dojo.require("dojox.grid.Grid");
dojo.require("dojox.grid._data.model");
dojo.require("dojo.parser");
dojo.require("dijit.form.ValidationTextBox");

dojo.require("geomajas.widget.attributes.AttributeSelect");
dojo.require("geomajas.widget.attributes.OperatorSelect");
dojo.require("geomajas.widget.attributes.AttributeValueWidget");
dojo.requireLocalization("geomajas.widget", "attributes");

dojo.declare("geomajas.widget.SearchTable", [dijit._Widget, dijit._Templated, dijit._Container], {

	widgetsInTemplate : true,
	templatePath : dojo.moduleUrl("geomajas.widget", "html/SearchTable.html"),

	featureTable : null,
	showTableFunction : null,
	layer : null,
	rowCount : 0,
	maxRows : 5,
	maxResultSize : 50,
	resultFeedback : false,
	deselectOnSearch : true,

	attrCMBs : null,
	operCMBs : null,
	valueWidgets : null,
	
	attributeLabel : "Attribute",
	operatorLabel : "Operator",
	valueLabel : "Value",
	
	// Connection handles:
	resetHandle : null,
	attrHandles : null,
	
	constructor : function () {
		this.attrCMBs = [];
		this.operCMBs = [];
		this.valueWidgets = [];

		this.attrHandles = [];
	},

	postMixInProperties : function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
		var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "attributes");
		this.attributeLabel = widgetLocale.attribute;
		this.operatorLabel = widgetLocale.operator;
		this.valueLabel = widgetLocale.value;
	},

	destroy : function (finalize) {
		dojo.disconnect(this.resetHandle);
		while (this.attrHandles.length > 0) {
			dojo.disconnect(this.attrHandles.pop());
		}

		this.inherited(arguments);
	},

	addRow : function () {
		if (this.layer && this.rowCount < this.maxRows) {
			var criterion = this._criterionFromRow(this.rowCount-1);
			if (criterion == null && this.rowCount != 0) {
				log.error ("Current row not filled in properly; you shouldn't create a new row!");
				return;
			}
			var tbody = document.getElementById(this.id+":tbody");
			var tr = document.createElement("tr");
			var td1 = document.createElement("td");
			this.attrCMBs[this.rowCount] = this._attributeComboBox(this.rowCount);
			if (!this.attrCMBs[this.rowCount]) {
				log.error ("Error creating a new row for the SearchTable.");
				return;
			}
			this.operCMBs[this.rowCount] = this._operatorComboBox(this.rowCount);
			this.valueWidgets[this.rowCount] = this._valueWidget(this.rowCount);
			this.attrHandles.push (dojo.connect(this.attrCMBs[this.rowCount], "onChange", dojo.hitch(this.operCMBs[this.rowCount], "setAttributeName")));
			this.attrHandles.push (dojo.connect(this.attrCMBs[this.rowCount], "onChange", dojo.hitch(this.valueWidgets[this.rowCount], "setAttributeName")));

			var div1 = document.createElement("div");
			div1.setAttribute("class", "noBorderTable");
			div1.appendChild(this.attrCMBs[this.rowCount].domNode);
			td1.appendChild(div1);

			var div2 = document.createElement("div");
			div2.setAttribute("class", "noBorderTable");
			div2.appendChild(this.operCMBs[this.rowCount].domNode);
			var td2 = document.createElement("td");
			td2.appendChild(div2);

			var div3 = document.createElement("div");
			div3.setAttribute("class", "noBorderTable");
			div3.appendChild(this.valueWidgets[this.rowCount].domNode);
			var td3 = document.createElement("td");
			td3.appendChild(div3);

			tr.appendChild(td1);
			tr.appendChild(td2);
			tr.appendChild(td3);
			tbody.appendChild(tr);
			this.rowCount++;
		}
	},

	search : function () {
		if (this.layer) {
			if (this.deselectOnSearch){
				dojo.publish(this.layer.getMapModel().getSelectionTopic(), [ "deselectAll", null ]);
			}

			var dispatcher = geomajasConfig.dispatcher;
			var criteria = [];
			for (var i=0; i<this.rowCount; i++) {
				var criterion = this._criterionFromRow(i);
				if (criterion != null){
					criteria.push(criterion.toJSON());
				}
			}

			var command = new JsonCommand("command.feature.Search",
                    "org.geomajas.command.dto.SearchFeatureRequest", null, false);
			command.addParam("layerId", this.layer.layerId);
			command.addParam("crs", this.layer.mapModel.getCrs());
			command.addParam("booleanOperator", "AND");
			command.addParam("criteria", criteria);
			command.addParam("max", this.maxResultSize);
			if (this.layer.isFilterEnabled()) {
				command.addParam("filter", this.layer.getFilterString());
			}
			var deferred = dispatcher.execute(command);
			deferred.addCallback(this, "_searchCallback");
		}
	},

	getCriteria : function () {
		if (this.layer) {
			var dispatcher = geomajasConfig.dispatcher;
			var criteria = [];
			for (var i=0; i<this.rowCount; i++) {
				var criterion = this._criterionFromRow(i);
				if (criterion != null){
					criteria.push(criterion);
				}
			}
		}
		return criteria;
	},

	/**
	 * @private
	 */
	_searchCallback : function (result) {
		var table = dijit.byId(this.featureTable);
		table.setLayer(this.layer);
		table.reset();
		var features = result.features;
		for (var i=0; i<features.length; i++) {
			var feature = new Feature();
			feature.setLayer(this.layer);
			feature.fromJSON(features[i]);
			table.addFeature(feature);
			if (!this.layer.getFeatureStore().contains(feature.getId())) {
				this.layer.getFeatureStore().addElement(feature);
			}
		}
		if(this.showTableFunction) {
			this.showTableFunction();
		}
		table.render();
		if(this.resultFeedback) {
			alert("You search query yielded "+features.length+" results.");
		}
	},

	/**
	 * Resets the widget. First it drops all rows, then it checks if there is a
	 * layer selected, and if so, adds an empty row again.
	 */
	reset : function () {
		while (this.attrHandles.length > 0) {
			dojo.disconnect(this.attrHandles.pop());
		}
		var tbody = document.getElementById(this.id+":tbody");
		while (tbody.firstChild) {
			tbody.removeChild(tbody.firstChild);
		}
		this.rowCount = 0;
		this.attrCMBs = [];
		this.operCMBs = [];
		this.valueWidgets = [];
		this.addRow();
	},

	/**
	 * @private
	 */
	_criterionFromRow : function (index) {
		if (index < 0) {
			return null;
		}
		var attrCMB = this.attrCMBs[index];
		var operCMB = this.operCMBs[index];
		var valueWidget = this.valueWidgets[index];
		if (attrCMB == null || operCMB == null || valueWidget == null) {
			return null;
		}

		var atDef = this.layer.getFeatureType().getAttributeByName(attrCMB.getValue());
		if (atDef == null) {
			// just in case....a combobox would return the label, while a filteringselect would return the value behind the label.
			atDef = this.layer.getFeatureType().getAttributeByLabel(attrCMB.getValue());
		}
		var oper = operCMB.getValue();
		var value = valueWidget.getValue();
		var valueCheck = "" + value; // We do this because Javascript doesn't know the difference between 0 and null.
		if (atDef == null || oper == null || oper == "" || valueCheck == "null") {
			return null;
		}
		if (atDef.getType() == "string") {
			if (oper == "contains") {
				oper = "like";
				value = "*" + value + "*";
			}
			value = "'" + value + "'";
		}
		if (atDef.getType() == "date") {
			value = "'"+dojo.date.locale.format(value, {datePattern:"dd/MM/yyyy", selector:"date"})+"'";
		}

		return new SearchCriterion(atDef, oper, value);
	},

	/**
	 * @private
	 */
	_attributeComboBox : function (index) {
		if(this.layer) {
			var combo = new geomajas.widget.attributes.AttributeSelect({
				name:this.id+":attr:"+index,
				autoComplete:false,
				searchAttr:"name",
				store: null,
				style: "width: 150px",
				invalidMessage: null
			}, document.createElement("div"));
			combo.setLayer(this.layer);
			return combo;
		}
		return null;
	},

	/**
	 * @private
	 */
	_operatorComboBox : function (index) {
		if (this.layer) {
			var combo = new geomajas.widget.attributes.OperatorSelect({
				name:this.id+":oper:"+index,
				autoComplete:false,
				searchAttr:"name",
				store: null,
				style: "width: 90px"
			}, document.createElement("div"));
			combo.setLayer(this.layer);
			return combo;
		}
		return null;
	},

	/**
	 * @private
	 */
	_valueWidget : function (index) {
		if (this.layer) {
			var valueWidget = new geomajas.widget.attributes.AttributeValueWidget({ name:this.id+":value:"+index }, null);
			valueWidget.setLayer(this.layer);
			return valueWidget;
		}
		return null;
	},

	// Getters and setters:

	setLayer : function (layer) {
		this.layer = layer;
		this.reset();
	},

	setFeatureTable : function (featureTable) {
		this.featureTable = featureTable;
	},

	setMaxRows : function (maxRows) {
		this.maxRows = maxRows;
	},

	setMaxResultSize : function (maxResultSize) {
		this.maxResultSize = maxResultSize;
	},

	setShowTableFunction: function (showTableFunction) {
		this.showTableFunction = showTableFunction;
	}
});