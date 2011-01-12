dojo.provide("geomajas.map.print.common");
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
dojo.platformRequire({
    common: [
             "geomajas.map.print.BaseComponent",
             "geomajas.map.print.BaseComponentVisitor",
             "geomajas.map.print.LayoutConstraint",
             "geomajas.map.print.DefaultConfigurationVisitor",
             "geomajas.map.print.LabelComponent",
             "geomajas.map.print.LegendComponent",
             "geomajas.map.print.LegendItemComponent",
             "geomajas.map.print.MapComponent",
             "geomajas.map.print.MapConfigurationVisitor",
             "geomajas.map.print.PageComponent",
             "geomajas.map.print.PrintTemplate",
             "geomajas.map.print.PrintTemplateManager",
             "geomajas.map.print.PrintTransformation",
             "geomajas.map.print.PrintController",
             "geomajas.map.print.RasterLayerComponent",
             "geomajas.map.print.VectorLayerComponent",
             "geomajas.map.print.ViewPortComponent"
            ],
    browser: []
});
