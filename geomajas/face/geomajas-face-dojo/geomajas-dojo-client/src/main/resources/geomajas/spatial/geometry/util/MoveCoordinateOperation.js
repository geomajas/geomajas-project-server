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