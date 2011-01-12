dojo.provide("geomajas.gfx.painter.common");
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
log.info("geomajas.gfx.painter.common");

dojo.platformRequire({
    common: ["geomajas.gfx.painter.DefaultPaintablePainter",
             "geomajas.gfx.painter.RectanglePainter",
             "geomajas.gfx.painter.CirclePainter",
             "geomajas.gfx.painter.ImagePainter",
             "geomajas.gfx.painter.LabeledTilePainter",
             "geomajas.gfx.painter.RasterImagePainter",
             "geomajas.gfx.painter.RasterLayerPainter",
             "geomajas.gfx.painter.RasterNodePainter",
             "geomajas.gfx.painter.TextPainter",
             "geomajas.gfx.painter.MapModelPainter",
             "geomajas.gfx.painter.VectorLayerPainter",
             "geomajas.gfx.painter.FeaturePainter",
             "geomajas.gfx.painter.TilePainter",
             "geomajas.gfx.painter.RenderedTilePainter",
             "geomajas.gfx.painter.editing.FeatureTransactionPainter",
             "geomajas.gfx.painter.BaseComponentPainter",
             "geomajas.gfx.painter.PageComponentPainter",
             "geomajas.gfx.painter.ViewPortComponentPainter"
            ],
    browser: []
});

log.info("geomajas.gfx.painter.common done");
