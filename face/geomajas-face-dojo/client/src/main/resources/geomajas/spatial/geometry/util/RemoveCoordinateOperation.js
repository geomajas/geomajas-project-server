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

dojo.provide("geomajas.spatial.geometry.util.RemoveCoordinateOperation");
dojo.declare("RemoveCoordinateOperation", GeometryOperation, {

	/**
	 * @fileoverview Operation for deleting a coordinate of a geometry.
	 * @class Extension of the {@link GeometryOperation}, for deleting a
	 * coordinate of a geometry.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends GeometryOperation
	 * @param index Index to uniquely identify the original coordinate. Used in
	 *              the same way as {@link Geometry#getCoordinateN}.
	 */
	constructor : function (index) {
		this.index = index;
	},

	/**
	 * The actual deletion of the coordinate.
	 * @param geometry A {@link Geometry} object.
	 */
	edit : function (geometry) {
		if (this.index instanceof Array) {
			var pointIndex = this.index.pop();
			var lineString = geometry;
			if (this.index.length > 0) {
				lineString = geometry.getGeometryN(this.index);
			}
			var coords = lineString.getCoordinates();
			coords.splice(pointIndex, 1);
			if (pointIndex == 0 && lineString instanceof LinearRing) {
				coords[coords.length-1] = coords[0].clone();
			}
			this.index.push(pointIndex); // the array is a reference, and should remain the same after this function...
		} else {
			var coords = geometry.getCoordinates();
			coords.splice(this.index, 1);
			if (this.index == 0 && lineString instanceof LinearRing) {
				coords[coords.length-1] = coords[0].clone();
			}
		}
	}
});