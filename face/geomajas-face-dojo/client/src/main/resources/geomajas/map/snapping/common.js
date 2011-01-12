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

dojo.provide("geomajas.map.snapping.common");
dojo.platformRequire({
    common: ["geomajas.map.snapping.SnappingRule",
             "geomajas.map.snapping.SnappingAlgorithm",
             "geomajas.map.snapping.ClosestPoint",
             "geomajas.map.snapping.NearestSnap",
             "geomajas.map.snapping.Snapper"
            ],
    browser: []
});