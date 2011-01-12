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

dojo.provide("geomajas.map.layertree.common");
dojo.platformRequire({
    common: ["geomajas.map.layertree.LayerTreeNode",
             "geomajas.map.layertree.VectorLayer",
             "geomajas.map.layertree.RasterImageFactory",
             "geomajas.map.layertree.GoogleImageFactory",
             "geomajas.map.layertree.RasterLayer"
            ],
    browser: []
});