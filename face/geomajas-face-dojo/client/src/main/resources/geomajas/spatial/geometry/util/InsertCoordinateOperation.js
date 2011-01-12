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

dojo.provide("geomajas.spatial.geometry.util.InsertCoordinateOperation");
dojo.declare("InsertCoordinateOperation", GeometryOperation, {

	/**
	 * @fileoverview Operation for inserting a coordinate in a geometry.
	 * @class Extension of the {@link GeometryOperation}, for inserting a
	 * coordinate in a geometry.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends GeometryOperation
	 * @param index Index to uniquely identify the coordinate location. Used in
	 *              the same way as {@link Geometry#getCoordinateN}.
	 * @param coordinate The {@link Coordinate} object to insert.
	 */
	constructor : function (index, coordinate) {
		this.index = index;
		this.coordinate = coordinate;
	},

	/**
	 * The actual inserting of the coordinate.
	 * @param geometry Geometry object.
	 */
	edit : function (geometry) {
		var pointIndex = this.index.pop();
		var lineString = geometry.getGeometryN(this.index);				
		var coords = lineString.getCoordinates();
		
		if (pointIndex == 0) {
			coords.unshift(this.coordinate);
			if (lineString instanceof LinearRing) {
				coords[coords.length-1] = this.coordinate;
			}
		} else {
			lineString.coordinates.splice(pointIndex,0,this.coordinate);
		}
		this.index.push(pointIndex); // the array is a reference, and should remain the same after this function...
	}
});