dojo.provide("geomajas.widget.ExtendedFeatureListTable");
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
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.requireLocalization("geomajas.widget", "featureTableTooltip");

dojo.declare("geomajas.widget.ExtendedFeatureListTable", 
		[dijit._Widget, dijit._Templated], {

	widgetsInTemplate : true,
	templatePath : dojo.moduleUrl("geomajas.widget", "html/ExtendedFeatureListTable.html"),

	mapWidget : null,
	selected : null,

	// Options:
	idInTable : false,
	singleSelection : false,
	supportEditing : true,
	showHelp : false,
	
	titleString : "",
	item1String : "",
	item2String : "",
	
	postCreate : function () {
		var cmb = dijit.byId(this.id + ":cmb");
		if (cmb != null) {
			cmb.onChange = dojo.hitch (this, "_onLayerSelect");
		}

		var refreshButton = dijit.byId(this.id + ":refresh");
		if (refreshButton != null) {
			refreshButton.onClick = dojo.hitch (this, "_onRefreshTable");
			refreshButton.setDisabled(true);
		}

		var deselectButton = dijit.byId(this.id + ":deselect");
		if (deselectButton != null) {
			deselectButton.onClick = dojo.hitch (this, "_onDeselectAll");
			deselectButton.setDisabled(true);
		}
		
		var table = dijit.byId(this.id + ":flt");
		if (table != null) {
			table.setIdInTable(this.idInTable);
			table.setSingleSelection(this.singleSelection);
			table.setSupportEditing(this.supportEditing);
		}
		
		if (this.showHelp) {
			var element = dojo.byId(this.id+":info");
			element.style.display = "inline";
		}
	},
	
	postMixInProperties : function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
		var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "featureTableTooltip");
		this.titleString = widgetLocale.title;
		this.item1String = widgetLocale.item1;
		this.item2String = widgetLocale.item2;
	},

	setMapWidget : function (mapWidget) {
		this.mapWidget = mapWidget;
		if (mapWidget != null) {
			// Set mapWidget on the FeatureListTable:
			var table = dijit.byId(this.id + ":flt");
			table.setMapWidget(mapWidget);
			table.enableSelection (mapWidget.getMapModel().getSelectionTopic());

			// Fill the layer combobox (skip raster layers):
			var cmb = dijit.byId(this.id + ":cmb");
			if (cmb != null) {
				var store = {
					data : {
						identifier :"name",
						items : []
					}
				};
				var layers = mapWidget.getMapModel().getLayerList();
				var count = 1;
				for (var i=0; i<layers.count; i++) {
					var layer = layers.item(i);
					if (layer.getLayerType() != geomajas.LayerTypes.RASTER) {
						var item = {
							id : count,
							label : layer.getLabel(),
							name : layer.getLabel()
						};
						store.data.items.push(item);
						count++;
					}
				}
				cmb.store = new dojo.data.ItemFileReadStore(store);
			}
		}
	},

	resize : function (size) {
		var cs = dojo.getComputedStyle(this.domNode);
		var toolbar = dijit.byId(this.id + ":toolbar");
		var table = dijit.byId(this.id + ":flt");
		
		if (dojo.isIE && (cs.height == "auto" || cs.height == "100%")) {
			var height = this.domNode.offsetHeight;
			if (size != null) {
				height = size.h;
			}
			var tbHeight = getCompleteNodeHeight(toolbar.domNode);
			table.grid.domNode.style.height = (height-tbHeight) + "px";
			table.domNode.style.height = (height-tbHeight) + "px";
		} else {
			var height = parseInt(cs.height);
			var tbHeight = getCompleteNodeHeight(toolbar.domNode);
			table.grid.domNode.style.height = (height-tbHeight) + "px";
			table.domNode.style.height = (height-tbHeight) + "px";
		}
		table.grid.render();
	},

	addAction : function (action) {
		var toolbar = dijit.byId(this.id + ":toolbar");
		var button = new geomajas.widget.TBButton({name:action.getId(), id:action.getId(), iconClass:action.getImage(), label:action.getTooltip(), showLabel:false}, document.createElement('div'));
		button.init(action);
		toolbar.addChild(button);
	},
	
	reset : function () {
		var table = dijit.byId(this.id + ":flt");
		if (table != null) {
			table.reset();
		}
	},

	// Getters and setters:

	isIdInTable : function () {
		return this.idInTable;
	},

	setIdInTable : function (idInTable) {
		this.idInTable = idInTable;
		var table = dijit.byId(this.id + ":flt");
		if (table != null) {
			table.setIdInTable(this.idInTable);
		}
	},

	getSingleSelection : function () {
		return this.singleSelection;
	},

	setSingleSelection : function (singleSelection) {
		this.singleSelection = singleSelection;
		var table = dijit.byId(this.id + ":flt");
		if (table != null) {
			table.setSingleSelection(this.singleSelection);
		}
	},

	getSupportEditing : function () {
		return this.supportEditing;
	},

	setSupportEditing : function (supportEditing) {
		this.supportEditing = supportEditing;
		var table = dijit.byId(this.id + ":flt");
		if (table != null) {
			table.setSupportEditing(this.supportEditing);
		}
	},



	//-------------------------------------------------------------------------
	// Private functions.
	//-------------------------------------------------------------------------

	/**
	 * @private
	 */
	_onLayerSelect : function (label) {
		this.selected = this.mapWidget.getMapModel().getLayerByLabel(label);
		this._onRefreshTable();
		this.resize();
	},

	/**
	 * @private
	 */
	_onRefreshTable : function (e) {
		var refreshButton = dijit.byId(this.id + ":refresh");
		var deselectButton = dijit.byId(this.id + ":deselect");

		if (this.selected != null) {
			var table = dijit.byId(this.id + ":flt");
			if (table) {
				table.setLayer (this.selected);

				var fs = this.selected.getFeatureStore();
				var el = fs.getElements(); // All elements
				var features = el.getValueList();
				for (var i=0; i<features.length; i++) {
					var feature = features[i];
					table.addFeature (feature); // Add them to the table one by one.
				}
				table.render();
			}
			
			refreshButton.setDisabled(false);
			deselectButton.setDisabled(false);
		} else {
			var table = dijit.byId(this.id + ":flt");
			if (table) {
				table.reset();
			}
			refreshButton.setDisabled(true);
			deselectButton.setDisabled(true);
		}
	},

	/**
	 * @private
	 */
	_onDeselectAll : function () {
		if (this.mapWidget != null) {
			dojo.publish(this.mapWidget.getMapModel().getSelectionTopic(), [ "deselectAll", null ]);
		}
	}
});
