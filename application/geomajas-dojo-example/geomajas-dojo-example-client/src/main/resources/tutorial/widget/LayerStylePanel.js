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
dojo.provide("tutorial.widget.LayerStylePanel");

dojo.declare("LayerStylePanel", geomajas.widget.FloatingPane, {

	mapWidget : null,
	legend : null,
	selected : null,
	style : null,

	postMixInProperties : function (){
		this.dockable= true;
		this.maxable= false;
		this.closable= false;
		this.resizable= true;
		this.title = "Vector layer style manager";
		this.inherited (arguments);
	},

	postCreate : function() {
		var content =
			"<div dojoType=\"dijit.layout.LayoutContainer\" style=\"width:100%;height:100%; background-color:#D0D0D0;\">" +
			"	<div dojoType=\"dijit.layout.ContentPane\" layoutAlign=\"top\" style=\"height:30px;\">" +
			"		<div dojoType=\"dijit.Toolbar\" id=\""+this.id+":toolbar\" style=\"height:30px;\">" +
			"			<span style=\"padding-bottom:3px;\">Select a layer: </span>" +
			"			<select id=\""+this.id+":layerCMB\" name=\""+this.id+":layerCMB\" dojoType=\"dijit.form.ComboBox\" class=\"medium\" autoComplete=\"false\" pageSize=\"30\" searchStr=\"label\">"+
			"			</select>" +
			"		</div>" +
			"	</div>" +
			"	<div dojoType=\"dijit.layout.LayoutContainer\" layoutAlign=\"client\" style=\"padding: 5px; vertical-align:top;\">" +
			"		<div style=\"height:25px; padding:5px;\" dojoType=\"dijit.layout.ContentPane\" layoutAlign=\"top\" class=\"contentInner\">" +
			"			<span>Select a style : </span>" +
			"           <select id=\""+this.id+":styleCMB\" name=\""+this.id+":layerCMB\" dojoType=\"dijit.form.ComboBox\" class=\"medium\" autoComplete=\"false\" pageSize=\"30\" searchStr=\"label\">" +
			"			</select>" +
			"		</div>" +
			"		<div style=\"margin-top:5px; padding:5px;\" dojoType=\"dijit.layout.ContentPane\" layoutAlign=\"client\" class=\"contentInner\">"+
			"			<center><div id=\""+this.id+":connection\" style=\"width:100%;height:100%;\"></div></center>" +
			"		</div>" +
			"	</div>" +
			"	<div dojoType=\"dijit.layout.ContentPane\" layoutAlign=\"bottom\" style=\"margin:0 5px 5px 5px; height:35px;\" class=\"contentInner\">" +
			"		<center><button dojoType=\"dijit.form.Button\" id=\""+this.id+":apply\">Apply</button><button dojoType=\"dijit.form.Button\" id=\""+this.id+":reset\">Reset</button></center>"+
			"	</div>" +
			"</div>";
		this.setContent (content);
		this.inherited (arguments);
		
		// Now set functionality: 
		var layerCMB = dijit.byId(this.id + ":layerCMB");
		if (layerCMB != null) {
			layerCMB.onChange = dojo.hitch(this, "_onLayerSelect");
		}

		var styleCMB = dijit.byId(this.id + ":styleCMB");
		if (styleCMB != null) {
			styleCMB.onChange = dojo.hitch (this, "_onStyleChange");
			styleCMB.setDisabled (true);
		}

		var apply = dijit.byId(this.id + ":apply");
		if (apply != null) {
			apply.onClick = dojo.hitch (this, "apply");
			apply.setDisabled (true);
		}

		var reset = dijit.byId(this.id + ":reset");
		if (reset != null) {
			reset.onClick = dojo.hitch (this, "reset");
			reset.setDisabled (true);
		}
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

	setLegend : function (legend){
		this.legend = legend;
	},


	apply : function () {
		var styleWidget = dijit.byId(this.id + ":styleWidget");

		if (this.selected != null && styleWidget != null && this.style != null) {
			var value = styleWidget.getValue();
			if (!this.selected.checkVisibility()) {
				alert("The selected layer ("+this.selected.getLabel()+") is not visible!");
				return;
			}

			var found = false;
			var name = dijit.byId(this.id+":styleCMB").getValue();
			for (var i=0; i<this.selected.getStyles().count; i++) {
				if (name == this.selected.getStyles().item(i).getName()) {
					this.selected.getStyles().item(i).setStyle(value);
					found = true;
				}
			}
			if (!found) {
				alert("Choose a style first!");
				return;
			}

			this.selected.getFeatureStore().clear();
			dojo.publish(this.mapWidget.getRenderTopic(), [ this.selected, "delete"]);
			dojo.publish(this.mapWidget.getRenderTopic(), [ this.selected, "all"]);

			if (this.legend) {
				this.legend.layout();
			}
		}
	},

	reset : function () {
		var styleWidget = dijit.byId(this.id + ":styleWidget");
		styleWidget.setValue(this.style.clone());
		this.apply();
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

		this._setStyleWidget();
		this._fillStyleBox();
	},

	/**
	 * @private
	 */
	_fillStyleBox : function () {
		var cmb = dijit.byId(this.id+":styleCMB");
		if(this.selected != null && this.selected instanceof VectorLayer) {
			cmb.setDisabled (false);
			var data = {identifier : "name", items : []};
			for (var i=0; i<this.selected.getStyles().count; i++) {
				var style = this.selected.getStyles().item(i);
				data.items.push({label:style.getName(), id:style.getId(), name:style.getName()});
			}
			cmb.store = new dojo.data.ItemFileReadStore({data: data});
			cmb.setDisabled(false);
			cmb.setValue(this.selected.getStyles().item(0).getName());
			this._onStyleChange(this.selected.getStyles().item(0).getName());
		}
	},

	/**
	 * @private
	 */
	_onStyleChange : function (label) {
		if (this.selected) {
			this.style = null;
			for (var i=0; i<this.selected.getStyles().count; i++) {
				if (label == this.selected.getStyles().item(i).getName()) {
					this.style = this.selected.getStyles().item(i).getStyle();
				}
			}
			var styleWidget = dijit.byId(this.id + ":styleWidget");
			if (this.style != null && styleWidget != null) {
				styleWidget.setValue(this.style);
				styleWidget.setDisabled(false);
				var apply = dijit.byId(this.id + ":apply");
				if (apply != null){
					apply.setDisabled(false);
				}
				var reset = dijit.byId(this.id + ":reset");
				if (reset != null){
					reset.setDisabled(false);
				}
			}
		}
	},

	/**
	 * @private
	 */
	_setStyleWidget : function () {
		var widgetId = this.id + ":styleWidget";
		if (dijit.byId(widgetId)) {
			dijit.byId(widgetId).destroy();
		}
		var conn = dojo.byId(this.id + ":connection");
		var div = document.createElement("div");
		div.setAttribute("style", "width:100%;height:100%;");
		conn.appendChild (div);
		if(this.selected != null && this.selected instanceof VectorLayer) {
			var widget = null;
			if (this.selected.getLayerType() == geomajas.LayerTypes.LINESTRING || this.selected.getLayerType() == geomajas.LayerTypes.MULTILINESTRING) {
				widget = new geomajas.widget.LineStringStyleWidget({
					id:widgetId,
					alignment : "horizontal"
				}, div);
			} else if (this.selected.getLayerType() == geomajas.LayerTypes.POINT || this.selected.getLayerType() == geomajas.LayerTypes.POLYGON || this.selected.getLayerType() == geomajas.LayerTypes.MULTIPOLYGON) {
				widget = new geomajas.widget.PolygonStyleWidget({
					id:widgetId,
					alignment : "horizontal"
				}, div);
			}
			widget.setDisabled (true);
		}
		this.resize(this._currentState);
	}

});