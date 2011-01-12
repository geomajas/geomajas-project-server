dojo.provide("geomajas.config.factories.ToolbarFactory");
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
dojo.require("geomajas.action.common");

dojo.declare("ToolbarFactory", null, {

	constructor : function (mapWidget, dispatcher) {
		this.mapWidget = mapWidget;
		this.dispatcher = dispatcher;

		this.mapViewQueueHandler = new MapViewQueueHandler(mapWidget, 10);
	},
	
	createToolbar : function (toolbarConfig) {
		// create the tools and the toolbar
		if (toolbarConfig != null) {
			var toolbar = dijit.byId(toolbarConfig.id);
			if (toolbar != null) {
				var tools = toolbarConfig.tools.list;
				if (tools != null && tools instanceof Array) {
					for (var i=0; i<tools.length; i++) {
						var config = tools[i];
						var tool = this._createToolbarAction(toolbarConfig.id, config.id, config.parameters.list);
						if (tool != null) {
							if (!toolbar.addAction (tool, "after")) {
								break;
							}
						} else if (config.id == "ToolbarSeparator") {
								toolbar.addSeparator();
						} else {
							if(config != null){
								tool = this._createToolbarTool(toolbarConfig.id, config.id, config.parameters.list);
								if (tool != null) {
									tool.init(this.mapWidget);
									if (!toolbar.addTool (tool, "after")) {
										break;
									}
								} else {
									tool = this._createMultiToggleTool(toolbarConfig.id, config.id, config.parameters.list);
									if (tool != null) {
										tool.init(this.mapWidget);
										if (!toolbar.addMultiToggleTool (tool, "after")) {
											break;
										}
									} 
								}
							}
						}
					}
				}
				return toolbar;
			}
		}
		return null;
	},

	/**
	 * @private this.mapViewQueueHandler
	 */
	_createToolbarAction : function (toolbarId, name, params) {
		if (!name) {
			return null;
		}
		if (name == "DeselectAll"){
			return new DeselectAllAction (toolbarId + ".DeselectAll", this.mapWidget.getMapModel().getSelectionTopic());
		} else if (name == "ZoomIn") {
			return new ZoomInAction (toolbarId + ".ZoomIn", this.mapWidget.getMapView(), this._getParamValue(params,"delta"));
		} else if (name == "ZoomOut") {
			return new ZoomOutAction (toolbarId + ".ZoomOut", this.mapWidget.getMapView(), this._getParamValue(params,"delta"));
		} else if (name == "ZoomPrevious") {
			return new ZoomPreviousAction (toolbarId + ".ZoomPrevious", this.mapViewQueueHandler);
		} else if (name == "ZoomNext") {
			return new ZoomNextAction (toolbarId + ".ZoomNext", this.mapViewQueueHandler);
		} else if (name == "ZoomToSelection") {
			return new ZoomToSelectionAction (toolbarId + ".ZoomToSelection", this.mapWidget);
		} else if (name == "ZoomToMaximumExtent") {
			return new ZoomToMaximumExtentAction (toolbarId + ".ZoomToMaximumExtent", this.mapWidget, this._getParamValue(params,"excludeRasterLayers"),  this._getParamValue(params,"includeLayers"));
		} else if (name == "PanToSelection") {
			return new PanToSelectionAction (toolbarId + ".PanToSelection", this.mapWidget);
		} else if (name == "EditSelected"){
			return new EditSelectedAction(toolbarId + ".EditSelected", this.mapWidget);
		} else if (name == "ShowDefaultPrint") {
			return new ShowDefaultPrintAction (toolbarId + ".ShowDefaultPrint", this.mapWidget, this._getParamValue(params, "downloadMethod"), this._getParamValue(params, "pageSize"));
		} else if (name == "ExportGenericXml") {
			return new ExportGenericXmlAction (toolbarId + ".ExportGenericXml", this.mapWidget, this._getParamValue(params, "exporter"));
		} else if (name == "ImportGenericXml") {
			return new ImportGenericXmlAction (toolbarId + ".ImportGenericXml", this.mapWidget, this._getParamValue(params, "importer"));
		}
		return null;
	},
	
	/**
	 * @private
	 * TODO make this generic !!!!!
	 */
	_createToolbarTool : function (toolbarId, name, params) {
		if (!name) {
			return null;
		}
		if (name == "EditMode" || name == "MarkedEditMode"){
			return new EditTool (toolbarId + ".EditMode", this.mapWidget, this._getParamValue(params,"useMarkings"), this._getParamValue(params,"useSnapHelp"), this._getParamValue(params,"allowHoles"));
		} else if (name == "SplitPolygonMode") {
			return new SplitPolygonTool (toolbarId + ".SplitPolygonMode", this.mapWidget);
		} else if (name == "MergePolygonMode") {
			return new MergePolygonTool (toolbarId + ".MergePolygonMode", this.mapWidget);
		} else if (name == "MeasureDistanceMode") {
			return new MeasureDistanceTool (toolbarId + ".MeasureDistanceMode", this.mapWidget, this._getParamValue(params,"simple"), this._getParamValue(params,"useSnapHelp"));
		} else if (name == "PanMode") {
			return new PanTool (toolbarId + ".PanMode", this.mapWidget, this._getParamValue(params,"hideLabelsOnDrag"));
		} else if (name == "SelectionMode") {
			return new SelectionTool (toolbarId + ".SelectionMode", this.mapWidget,
					this._getParamValue(params, "supportEditing"),
					this._getParamValue(params, "priorityToSelectedLayer"),
					this._getParamValue(params, "coverageRatio"),
					this._getParamValue(params, "pixelTolerance") );
		} else if (name == "ZoomInMode") {
			return new ZoomInTool (toolbarId + ".ZoomInMode", this.mapWidget, this._getParamValue(params,"delta"));
		} else if (name == "ZoomOutMode") {
			return new ZoomOutTool (toolbarId + ".ZoomOutMode", this.mapWidget, this._getParamValue(params,"delta"));
		} else if (name == "ZoomToRectangleMode") {
			return new ZoomToRectangleTool (toolbarId + ".ZoomToRectangleMode", this.mapWidget);
		} else if (name == "NavigateMode") {
			return new NavigateTool (toolbarId + ".NavigateMode", this.mapWidget);
		} else if (name == "FeatureInfoMode") {
			return new FeatureInfoTool (toolbarId + ".FeatureInfoMode", this.mapWidget);
		} else if (name == "LocationInfoMode") {
			return new LocationInfoTool (toolbarId + ".LocationInfoMode", this.mapWidget);
		} else if (name == "PrintMode") {
			return new PrintTool (toolbarId + ".PrintMode", this.mapWidget);
		}
		return null;
	},

	/**
	 * For creating tools that can be toggled at the same time
	 */
	_createMultiToggleTool : function(toolbarId, name, params) {
		log.debug("trying to create multitoggle tool");
		if (!name) {
			return null;
		}
		if  (name == "MouseInfoMode") {
			return new MouseInfoTool (toolbarId + ".MouseInfoMode", this.mapWidget, 
					this._getParamValue(params,"showViewCoords"), 
					this._getParamValue(params,"showWorldCoords"), 
					this._getParamValue(params,"left"), 
					this._getParamValue(params,"right"),
					this._getParamValue(params,"top"), 
					this._getParamValue(params,"bottom"),
					this._getParamValue(params,"opacity"));
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
