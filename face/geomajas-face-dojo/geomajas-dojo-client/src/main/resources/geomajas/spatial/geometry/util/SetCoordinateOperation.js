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