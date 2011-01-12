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

dojo.provide("geomajas.spatial.geometry.util.GeometryOperation");
dojo.declare("GeometryOperation", null, {

	/**
	 * @fileoverview Interface for a geometric operation.
	 * @class General interface for geometric opertations.
	 * @author Pieter De Graef
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * The main edit function. Is passed a geometry object. If other values are
	 * needed, pass them through the constructor, or via setters. Does not
	 * return anything, so it adjusts the given geometry!
	 * @param geometry The {@link Geometry} object to be adjusted.
	 */
	edit : function (geometry) {
		alert("GeometryOperartion.edit : implement me!");
	}
});