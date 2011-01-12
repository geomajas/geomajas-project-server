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

dojo.provide("geomajas.spatial.geometry.util.MoveCoordinateOperation");
dojo.declare("MoveCoordinateOperation", GeometryOperation, {

	/**
	 * @fileoverview Operation for translating a coordinate of a geometry.
	 * @class Extension of the {@link GeometryOperation}, for translating a
	 * coordinate of a geometry.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends GeometryOperation
	 * @param index Index to uniquely identify the original coordinate. Used in
	 *              the same way as {@link Geometry#getCoordinateN}.
	 * @param translateX The x-factor to translate over.
	 * @param translateY The y-factor to translate over.
	 */
	constructor : function (index, translateX, translateY) {
		this.index = index;

		/** x-delta. */
		this.translateX = translateX;

		/** y-delta. */
		this.translateY = translateY;
	},

	/**
	 * The actual translation of the coordinate.
	 * @param geometry A {@link Geometry} object.
	 */
	edit : function (geometry) {
		var coordinate = geometry.getCoordinateN(index);
		if (coordinate != null) {
			var x = coordinate.getX() + this.translateX;
			var y = coordinate.getY() + this.translateY;
			coordinate.setX(x);
			coordinate.setY(y);
		}
	}
});