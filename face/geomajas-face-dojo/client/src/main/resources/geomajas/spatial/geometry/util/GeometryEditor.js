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

dojo.provide("geomajas.spatial.geometry.util.GeometryEditor");
dojo.declare("GeometryEditor", null, {

	/**
	 * @fileoverview General editor for geometries.
	 * @class General editor for geometries. Uses geometry operations to do
	 * it's work. Don't try to adjust geometries without this class and these
	 * operations!
	 * @author Pieter De Graef
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * The actual editing of a geometry by a certain operation.
	 * @param geometry A {@link Geometry} object.
	 * @param operation A {@link GeometryOperation} object.
	 */
	edit : function (geometry, operation) {
		if (geometry == null) {
			log.warn("Geometry operation could not start: geometry was null.");
			return;
		}
		try {
			operation.edit(geometry);
		} catch (e) {
			log.error ("Error in geometry operation, "+operation.declaredClass);
		}
	}
});