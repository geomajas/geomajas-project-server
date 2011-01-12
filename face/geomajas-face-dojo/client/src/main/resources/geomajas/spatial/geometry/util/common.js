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

dojo.provide("geomajas.spatial.geometry.util.common");
dojo.platformRequire({
    common: ["geomajas.spatial.geometry.util.GeometryDeserializer",
             "geomajas.spatial.geometry.util.GeometryEditor",
             "geomajas.spatial.geometry.util.GeometryOperation",
             "geomajas.spatial.geometry.util.MoveCoordinateOperation",
             "geomajas.spatial.geometry.util.SetCoordinateOperation",
             "geomajas.spatial.geometry.util.InsertCoordinateOperation",
             "geomajas.spatial.geometry.util.RemoveCoordinateOperation",
             "geomajas.spatial.geometry.util.AddRingOperation",
             "geomajas.spatial.geometry.util.RemoveRingOperation",
             "geomajas.spatial.geometry.util.TranslationOperation"
            ],
    browser: []
});