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

dojo.provide("geomajas.gfx.common");
dojo.platformRequire({
    common: ["geomajas.gfx.ShapeStyle",
             "geomajas.gfx.FontStyle",
             "geomajas.gfx.PictureStyle",
             "geomajas.gfx.Painter",
             "geomajas.gfx.PainterVisitable",
             "geomajas.gfx.PainterVisitor",
             "geomajas.gfx.GraphicsContext",
             "geomajas.gfx.painter.common",
             "geomajas.gfx.paintables.common"
            ],
    browser: []
});

// include a context conditionally
dojo.requireIf(dojo.isIE == 0, "geomajas.gfx.svg.SvgGraphicsContext");
dojo.requireIf(dojo.isIE > 0, "geomajas.gfx.vml.VmlGraphicsContext");

//dojo.requireIf(hasSVGSupport() == true, "geomajas.gfx.svg.SvgGraphicsContext");
//dojo.requireIf(hasSVGSupport() == false, "geomajas.gfx.vml.VmlGraphicsContext");