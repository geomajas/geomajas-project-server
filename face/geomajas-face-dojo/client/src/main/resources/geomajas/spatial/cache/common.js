dojo.provide("geomajas.spatial.cache.common");
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
    common: ["geomajas.spatial.cache.SpatialCode",
             "geomajas.spatial.cache.SpatialNode",
    		 "geomajas.spatial.cache.SpatialCache",
             "geomajas.spatial.cache.TileCode",
             "geomajas.spatial.cache.Tile",
             "geomajas.spatial.cache.TileCache",
             "geomajas.spatial.cache.VisibleOnlyTileCache",
             "geomajas.spatial.cache.experimental.RenderedTile",
             "geomajas.spatial.cache.experimental.LabeledTile",
             "geomajas.spatial.cache.experimental.RenderingIndependentTileCache"
            ],
    browser: []
});
