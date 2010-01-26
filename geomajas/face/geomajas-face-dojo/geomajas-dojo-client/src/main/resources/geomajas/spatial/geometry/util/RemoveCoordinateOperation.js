/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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