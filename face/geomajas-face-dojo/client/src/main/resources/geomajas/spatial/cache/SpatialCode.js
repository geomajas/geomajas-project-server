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

dojo.provide("geomajas.spatial.cache.SpatialCode");
dojo.declare("SpatialCode", null, {

	/**
	 * @fileoverview Definition of a code that can be assigned to a spatial node.
	 * @class Definition of a code that can be assigned to a spatial node.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * String representation of a SpatialCode. Often used as key in dictionaries.
	 */
	toString : function () {
		return "";
	},

	/**
	 * Standard equals function. No surprises here.
	 */
	equals : function (spatialCode) {
		return false
	},

	/**
	 * Parses a JSON object.
	 */
	fromJSON : function (json) {
	}
}); 