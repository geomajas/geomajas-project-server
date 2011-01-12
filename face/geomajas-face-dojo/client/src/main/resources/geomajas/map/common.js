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

dojo.provide("geomajas.map.common");
dojo.platformRequire({
    common: ["geomajas.map.RenderTopicDistributor",
             "geomajas.map.ScaleUtil",
             "geomajas.map.MapView",
             "geomajas.map.Feature",
             "geomajas.map.FeatureReference",
             "geomajas.map.RasterNode",
             "geomajas.map.RasterImage",
             "geomajas.map.store.common",
             "geomajas.map.layertree.common",
             "geomajas.map.editing.common",
             "geomajas.map.MapModel",
             "geomajas.map.Camera",
             "geomajas.map.snapping.common",
             "geomajas.map.workflow.common",
             "geomajas.map.attributes.common",
             "geomajas.map.print.common",
             "geomajas.map.search.common"
            ],
    browser: []
});