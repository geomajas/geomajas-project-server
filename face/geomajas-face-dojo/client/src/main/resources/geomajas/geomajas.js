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

dojo.provide("geomajas.geomajas");
dojo.require("dojox.collections");
dojo.require("dojo.parser");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.layout.LayoutContainer");
dojo.require("dijit.layout.SplitContainer");
dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.AccordionContainer");
dojo.require("dojox.layout.FloatingPane");
dojo.require("dijit.TitlePane");
dojo.require("dojox.layout.ExpandoPane");

dojo.require("geomajas.log.log4javascript");
dojo.require("geomajas._base");
dojo.require("geomajas.config.common");
dojo.require("geomajas.event.common");
dojo.require("geomajas.spatial.common");
dojo.require("geomajas.util.common");
dojo.require("geomajas.map.common");
dojo.require("geomajas.gfx.common");
dojo.require("geomajas.action.common");
dojo.require("geomajas.controller.common");
dojo.require("geomajas.io.common");
dojo.require("geomajas.widget.attributes.common");

dojo.require("geomajas.widget.ActivityDiv");
dojo.require("geomajas.widget.DynamicToolbar");
dojo.require("geomajas.widget.ExtendedFeatureListTable");
dojo.require("geomajas.widget.FeatureDetailEditor");
dojo.require("geomajas.widget.FeatureDetailTable");
dojo.require("geomajas.widget.FeatureEditDialog");
dojo.require("geomajas.widget.FeatureListTable");
dojo.require("geomajas.widget.FloatingPane");
dojo.require("geomajas.widget.FontStyleWidget");
dojo.require("geomajas.widget.LayerTree");
dojo.require("geomajas.widget.LegendWidget");
dojo.require("geomajas.widget.LineStringStyleWidget");
dojo.require("geomajas.widget.MapWidget");
dojo.require("geomajas.widget.PolygonStyleWidget");
dojo.require("geomajas.widget.ProgressDialog");
dojo.require("geomajas.widget.ScaleSelect");
dojo.require("geomajas.widget.SearchTable");
dojo.require("geomajas.widget.TemplatePrintWidget");
dojo.require("geomajas.widget.DefaultPrintWidget");
dojo.require("geomajas.widget.TextBalloon");
dojo.require("geomajas.widget.ZoomSlider");

dojo.require("geomajas.widget.experimental.LoadingScreen");
dojo.require("geomajas.widget.experimental.Profiler");
dojo.require("geomajas.widget.experimental.DownloadDialog");
