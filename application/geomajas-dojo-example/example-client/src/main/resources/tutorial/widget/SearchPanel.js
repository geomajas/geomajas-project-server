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
dojo.provide("tutorial.widget.SearchPanel");

dojo.declare("SearchPanel", geomajas.widget.FloatingPane, {

	mapWidget : null,
	selected : null,

	postMixInProperties : function (){
		this.dockable= true;
		this.maxable= false;
		this.closable= true;
		this.resizable= true;
		this.title = "Search features";
		this.inherited (arguments);
	},

	postCreate : function() {
		var content =
			"<div dojoType=\"dijit.layout.BorderContainer\" style=\"width:100%;height:100%; background-color:#D0D0D0;\">" +
			"	<div dojoType=\"dijit.layout.ContentPane\" region=\"top\" style=\"height:30px; overflow:hidden;\">" +
			"		<div dojoType=\"dijit.Toolbar\" id=\""+this.id+":toolbar\">" +
			"			<button dojoType=\"dijit.form.Button\" id=\""+this.id+":add\" iconClass=\"newIcon\">Add Criterium</button>" +
			"			<button dojoType=\"dijit.form.Button\" id=\""+this.id+":search\" iconClass=\"searchIcon\">Search!</button>" +
			"			<button dojoType=\"dijit.form.Button\" id=\""+this.id+":reset\" iconClass=\"resetIcon\">Reset</button>" +
			"		</div>" +
			"	</div>" +
			"	<div dojoType=\"dijit.layout.ContentPane\" region=\"center\" style=\"margin: 5px; vertical-align:top; padding:10px;\" class=\"contentInner\">" +
			"		<center>" +
			"			<div id=\""+this.id+":text\" style=\"font-weight: bold;\">There is no layer selected!</div>"+
			"			<div style=\"width: 550px; text-align:left; margin-bottom:10px; margin-top: 10px;\"><span style=\"padding-bottom:3px;\">Select a layer: </span>" +
			"			<select id=\""+this.id+":layerCMB\" name=\""+this.id+":layerCMB\" dojoType=\"dijit.form.ComboBox\" class=\"medium\" autoComplete=\"false\" pageSize=\"30\" searchStr=\"label\">"+
			"			</select></div>" +
			"			<div dojoType=\"geomajas.widget.SearchTable\" id=\""+this.id+":searchTable\" style=\"width: 550px; height:150px; border: 1px solid #888; background:#FFFFFF;\"></div>" +
			"		</center>"+ 
			"	</div>" +
			"</div>";
		this.setContent (content);
		this.inherited (arguments);

		// Now set functionality: 
		var layerCMB = dijit.byId(this.id + ":layerCMB");
		if (layerCMB != null) {
			layerCMB.onChange = dojo.hitch(this, "_onLayerSelect");
		}

		var add = dijit.byId(this.id + ":add");
		if (add != null) {
			add.onClick = dojo.hitch (this, "add");
			add.setDisabled (true);
		}

		var search = dijit.byId(this.id + ":search");
		if (search != null) {
			search.onClick = dojo.hitch (this, "search");
			search.setDisabled (true);
		}

		var reset = dijit.byId(this.id + ":reset");
		if (reset != null) {
			reset.onClick = dojo.hitch (this, "reset");
			reset.setDisabled (true);
		}

		dojo.connect(this, "show", dojo.hitch(this, "_selectLayer"));
	},

	setMapWidget : function (mapWidget) {
		this.mapWidget = mapWidget;
		if (mapWidget != null) {
			// Fill the layer combobox (skip raster layers):
			var cmb = dijit.byId(this.id + ":layerCMB");
			if (cmb != null) {
				var store = {
					data : {
						identifier :"name",
						items : []
					}
				};
				var layers = mapWidget.getMapModel().getLayerList();
				var count = 1;
//				var layerToSelect = null;
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
//						layerToSelect = layer.getLabel();
					}
				}
				cmb.store = new dojo.data.ItemFileReadStore(store);
//				if (layerToSelect != null) {
//					cmb.setValue(layerToSelect);
//				}
			}
		}
	},
	
	initTable : function (showTableFunction, tableId) {
		var searchTable = dijit.byId(this.id + ":searchTable");
		if (searchTable != null) {
			searchTable.setFeatureTable(tableId);
			searchTable.setShowTableFunction(showTableFunction);
			searchTable.setMaxResultSize(500);
		}
	},

	add : function () {
		var searchTable = dijit.byId(this.id + ":searchTable");
		if (searchTable != null) {
			searchTable.addRow();
		}
	},

	search : function () {
		var searchTable = dijit.byId(this.id + ":searchTable");
		if (searchTable != null) {
			searchTable.search();
		}
	},

	reset : function () {
		var searchTable = dijit.byId(this.id + ":searchTable");
		if (searchTable != null) {
			searchTable.reset();
		}
	},



	//-------------------------------------------------------------------------
	// Private functions.
	//-------------------------------------------------------------------------

	/**
	 * @private
	 */
	_onLayerSelect : function (label) {
		var map = dijit.byId("sampleFutureMap");
		this.selected = this.mapWidget.getMapModel().getLayerByLabel(label);
		
		var searchTable = dijit.byId(this.id + ":searchTable");
		if (searchTable != null){
			searchTable.setLayer(this.selected);
			this._setInfo("Now searching in layer: "+this.selected.getLabel());
		} else {
			this._setInfo("There is no layer selected!");
		}

		var add = dijit.byId(this.id + ":add");
		var search = dijit.byId(this.id + ":search");
		var reset = dijit.byId(this.id + ":reset");
		if (this.selected != null) {
			if (add != null) {
				add.setDisabled (false);
			}
			if (search != null) {
				search.setDisabled (false);
			}
			if (reset != null) {
				reset.setDisabled (false);
			}
		} else {
			if (add != null) {
				add.setDisabled (true);
			}
			if (search != null) {
				search.setDisabled (true);
			}
			if (reset != null) {
				reset.setDisabled (true);
			}
		}
	},
	
	/**
	 * @private
	 */
	_setInfo : function (text) {
		var div = document.getElementById(this.id+":text");
		while (div.firstChild) {
			div.removeChild(div.firstChild);
		}
		var node = document.createTextNode(text);
		div.appendChild(node);
	},

	_selectLayer : function () {
		var cmb = dijit.byId(this.id + ":layerCMB");
		cmb.setValue(cmb.store._jsonData.items[0].name);
	}
});