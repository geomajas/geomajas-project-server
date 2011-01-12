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

dojo.provide("geomajas.spatial.geometry.util.SetCoordinateOperation");
dojo.declare("SetCoordinateOperation", GeometryOperation, {

	/**
	 * @fileoverview Operation for replacing a coordinate of a geometry.
	 * @class Extension of the {@link GeometryOperation}, for replacing a
	 * coordinate of a geometry.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends GeometryOperation
	 * @param index Index to uniquely identify the original coordinate. Used in
	 *              the same way as {@link Geometry#getCoordinateN}.
	 */
	constructor : function (index, coordinate) {
		this.index = index;

		/** The new coordinate object. */
		this.coordinate = coordinate;
	},

	/**
	 * The actual replacing of the coordinate.
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
			coords[pointIndex] = this.coordinate.clone();
			if (pointIndex == 0 && lineString instanceof LinearRing) {
				coords[coords.length-1] = this.coordinate.clone();
			}
			this.index.push(pointIndex); // index is a reference, and may not change ....
		} else {
			var old = geometry.getCoordinateN(this.index);
			if (old != null) {
				old.setX(this.coordinate.getX());
				old.setY(this.coordinate.getY());
			}
		}
	}
});