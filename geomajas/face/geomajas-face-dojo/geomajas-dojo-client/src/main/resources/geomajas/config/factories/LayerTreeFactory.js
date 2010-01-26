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

dojo.provide("geomajas.config.factories.LayerTreeFactory");
dojo.declare("LayerTreeFactory", null, {

	constructor : function (mapWidget, dispatcher) {
		this.mapWidget = mapWidget;
		this.dispatcher = dispatcher;
		this.layerTreeWidget = null;
	},
	
	createLayerTree : function (layerTreeInfo) {
		// create the tools and the layertree
		if (layerTreeInfo != null && this.mapWidget != null) {
			this.layerTreeWidget = dijit.byId(layerTreeInfo.id);
			if (this.layerTreeWidget != null) {
				this.layerTreeWidget.init(this.mapWidget.getMapModel());
				var tools = layerTreeInfo.tools.list;
				if (tools != null && tools instanceof Array) {
					for (var i=0; i<tools.length; i++) {
						var config = tools[i];
						var tool = this._createAction(config.id, config.parameters.list);
						if (tool != null) {
							if (!this.layerTreeWidget.addAction (tool)) {
								return;
							}
						} else {
							tool = this._createTool(config.id, config.parameters.list);
							if (tool != null) {
								tool.init(this.mapWidget);
								if (!this.layerTreeWidget.addTool(tool)) {
									return;
								}
							}
						}
					}
				}
			} else {
                log.error("Cannot find layertree with id " + layerTreeInfo.id + "in html.");
            }
		}
		return null;
	},

	/**
	 * @private
	 */
	_createAction : function (name, params) {
		if (!name) {
			return null;
		}
		if (name == "ShowTableAction") {
			return new ShowTableAction(this.layerTreeWidget, this.mapWidget, null, this._getParamValue(params,"tableID"));
		} else if (name == "RefreshLayerAction") {
			return new RefreshLayerAction(this.layerTreeWidget);
		}
		return null;
	},
	
	/**
	 * @private
	 */
	_createTool : function (name, params) {
		if (!name) {
			return null;
		}
		if (name == "LayerVisibleTool"){
			return new LayerVisibleTool (this.layerTreeWidget, this.mapWidget.getRenderTopic());
		} else if (name == "LayerLabeledTool") {
			return new LayerLabeledTool (this.layerTreeWidget);
		} else if (name == "LayerSnappingTool") {
			return new LayerSnappingTool (this.layerTreeWidget);
		}
		return null;
	},

	/**
	 * @private
	 */
	_getParamValue : function (params, name) {
		for (var i=0; i<params.length; i++) {
			if (params[i].name == name) {
				return params[i].value;
			}
		}
		return null;
	}
});