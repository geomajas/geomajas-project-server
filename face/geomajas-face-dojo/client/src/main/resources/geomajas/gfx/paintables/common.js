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

dojo.provide("geomajas.gfx.paintables.common");
log.info("geomajas.gfx.paintables.common");

dojo.platformRequire({
    common: ["geomajas.gfx.paintables.Rectangle",
             "geomajas.gfx.paintables.Picture",
             "geomajas.gfx.paintables.Line",
             "geomajas.gfx.paintables.Circle",
             "geomajas.gfx.paintables.PaintableGeometry",
             "geomajas.gfx.paintables.Text"
            ],
    browser: []
});

log.info("geomajas.gfx.paintables.common done");
