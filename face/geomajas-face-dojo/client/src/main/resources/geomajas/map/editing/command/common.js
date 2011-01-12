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

dojo.provide("geomajas.map.editing.command.common");
dojo.platformRequire({
    common: ["geomajas.map.editing.command.EditCommand",
             "geomajas.map.editing.command.AddCoordinateCommand",
             "geomajas.map.editing.command.InsertCoordinateCommand",
             "geomajas.map.editing.command.RemoveCoordinateCommand",
             "geomajas.map.editing.command.RemoveRingCommand",
             "geomajas.map.editing.command.MoveCoordinateCommand"
            ],
    browser: []
});