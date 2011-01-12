dojo.provide("geomajas.config.common");
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
    common: ["geomajas.config.FeatureStyleInfo",
             "geomajas.config.LabelStyleInfo",
             "geomajas.config.NamedStyleInfo",
             "geomajas.config.factories.LayerTreeFactory",
             "geomajas.config.factories.ToolbarFactory",
             "geomajas.config.ConfigManager"
             ],
    browser: []
});
