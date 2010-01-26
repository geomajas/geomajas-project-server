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
             "geomajas.action.toolbar.FeatureInfoTool"
            ],
    browser: []
});