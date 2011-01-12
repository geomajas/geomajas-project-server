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

dojo.provide("geomajas.spatial.common");
dojo.platformRequire({
    common: ["geomajas.spatial.Coordinate",
             "geomajas.spatial.Vector2D",
             "geomajas.spatial.LineSegment",
             "geomajas.spatial.Bbox",
             "geomajas.spatial.Matrix2D",
             "geomajas.spatial.Matrix3D",
             "geomajas.spatial.geometry.common",
             "geomajas.spatial.transform.common",
             "geomajas.spatial.MathLib",
             "geomajas.spatial.cache.common"
            ],
    browser: []
});