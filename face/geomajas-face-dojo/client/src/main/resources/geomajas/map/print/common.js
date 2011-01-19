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
             "geomajas.map.print.PrintTemplateInfo",
             "geomajas.map.print.LayoutConstraintInfo",

             "geomajas.map.print.PrintComponentInfo",
             "geomajas.map.print.BaseLayerComponentInfo",
             "geomajas.map.print.PageComponentInfo",
             "geomajas.map.print.MapComponentInfo",
             "geomajas.map.print.ImageComponentInfo",
             "geomajas.map.print.LegendComponentInfo",
             "geomajas.map.print.LegendItemComponentInfo",
             "geomajas.map.print.LegendIconComponentInfo",
             "geomajas.map.print.ScaleBarComponentInfo",
             "geomajas.map.print.LabelComponentInfo",
             "geomajas.map.print.RasterLayerComponentInfo",
             "geomajas.map.print.VectorLayerComponentInfo",

             "geomajas.map.print.template.common"
            ],
    browser: []
});
