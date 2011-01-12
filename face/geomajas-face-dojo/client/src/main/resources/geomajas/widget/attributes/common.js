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

dojo.provide("geomajas.widget.attributes.common");
dojo.platformRequire({
    common: ["geomajas.widget.attributes.CompositionFloater",
             "geomajas.widget.attributes.CompositionEditor",
             "geomajas.widget.attributes.AssociationAttributeStore",
             "geomajas.widget.attributes.AttributeEditorFactory",
             "geomajas.widget.attributes.AttributeSelect",
             "geomajas.widget.attributes.AttributeValueWidget",
             "geomajas.widget.attributes.OperatorSelect",
             "geomajas.widget.attributes.NullableFilteringSelect"
            ],
    browser: []
});