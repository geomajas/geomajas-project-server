dojo.provide("geomajas.gfx.painter.common");
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
