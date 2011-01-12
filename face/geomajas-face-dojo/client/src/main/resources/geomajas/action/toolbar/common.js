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

dojo.provide("geomajas.action.toolbar.common");
dojo.platformRequire({
    common: ["geomajas.action.toolbar.ZoomInAction",
             "geomajas.action.toolbar.ZoomOutAction",
             "geomajas.action.toolbar.ZoomPreviousAction",
             "geomajas.action.toolbar.ZoomNextAction",
             "geomajas.action.toolbar.RotateLeftAction",
             "geomajas.action.toolbar.RotateRightAction",
             "geomajas.action.toolbar.DeselectAllAction",
             "geomajas.action.toolbar.EditSelectedAction",
             "geomajas.action.toolbar.ShowDefaultPrintAction",
             "geomajas.action.toolbar.NavigateTool",
             "geomajas.action.toolbar.ZoomInTool",
             "geomajas.action.toolbar.ZoomOutTool",
             "geomajas.action.toolbar.PanTool",
             "geomajas.action.toolbar.ZoomToRectangleTool",
             "geomajas.action.toolbar.ZoomToSelectionAction",
             "geomajas.action.toolbar.ZoomToMaximumExtentAction",
             "geomajas.action.toolbar.ZoomToFeatureAction",
			 "geomajas.action.toolbar.PanToSelectionAction",
			 "geomajas.action.toolbar.PrintTool",
             "geomajas.action.toolbar.SelectionTool",
             "geomajas.action.toolbar.MeasureDistanceTool",
             "geomajas.action.toolbar.EditTool",
             "geomajas.action.toolbar.SplitPolygonTool",
             "geomajas.action.toolbar.MergePolygonTool",
             "geomajas.action.toolbar.LocationInfoTool",
             "geomajas.action.toolbar.FeatureInfoTool",
             "geomajas.action.toolbar.MouseInfoTool"
            ],
    browser: []
});