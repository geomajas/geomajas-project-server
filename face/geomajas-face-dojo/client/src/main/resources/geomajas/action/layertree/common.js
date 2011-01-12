dojo.provide("geomajas.action.layertree.common");
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
    common: ["geomajas.action.layertree.LayerVisibleTool",
             "geomajas.action.layertree.LayerLabeledTool",
             "geomajas.action.layertree.LayerSnappingTool",
             "geomajas.action.layertree.ShowTableAction",
             "geomajas.action.layertree.RefreshLayerAction"
            ],
    browser: []
});
