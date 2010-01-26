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