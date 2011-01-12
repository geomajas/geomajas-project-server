dojo.provide("geomajas.config.factories.LayerTreeFactory");
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