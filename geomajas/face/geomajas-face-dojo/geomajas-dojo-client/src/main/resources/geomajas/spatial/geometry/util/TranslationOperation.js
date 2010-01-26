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

dojo.provide("geomajas.spatial.geometry.util.TranslationOperation");
dojo.declare("TranslationOperation", GeometryOperation, {

	/**
	 * @fileoverview Operation for translating a geometry.
	 * @class Extension of the {@link GeometryOperation}, for translating a 
	 * geometry.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends GeometryOperation
	 * @param translateX The x-factor to translate over.
	 * @param translateY The y-factor to translate over.
	 */
	constructor : function (translateX, translateY) {
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
		if (geometry instanceof Point) {
			var coordinate = geometry.getCoordinateN(0);
			var x = coordinate.getX() + this.translateX;
			var y = coordinate.getY() + this.translateY;
			coordinate.setX(x);
			coordinate.setY(y);
		} else if (geometry instanceof LineString) {
			this._translateLineString(geometry);
		} else if (geometry instanceof LinearRing) {
			this._translateLinearRing(geometry);
		} else if (geometry instanceof Polygon) {
			this._translatePolygon(geometry);
		} else if (geometry instanceof MultiLineString) {
			this._translateMultiLineString(geometry);
		} else if (geometry instanceof MultiPolygon) {
			this._translateMultiPolygon(geometry);
		}
	},

	_translateLineString : function (lineString) {
		var coords = lineString.getCoordinates();
		for (var i=0; i<coords.length; i++) {
			var x = coords[i].getX() + this.translateX;
			var y = coords[i].getY() + this.translateY;
			coords[i].setX(x);
			coords[i].setY(y);
		}		
	},

	_translateLinearRing : function (linearRing) {
		var coords = linearRing.getCoordinates();
		for (var i=0; i<coords.length; i++) {
			var x = coords[i].getX() + this.translateX;
			var y = coords[i].getY() + this.translateY;
			coords[i].setX(x);
			coords[i].setY(y);
		}		
	},

	_translatePolygon : function (polygon) {
		var shell = polygon.getExteriorRing();
		if (shell != null) {
			this._translateLinearRing(shell);
		}
		for (var i=0; i<polygon.getNumInteriorRing(); i++) {
			this._translateLinearRing(polygon.getInteriorRingN(i));
		}
	},

	_translateMultiLineString : function (multiLineString) {
		for (var i=0; i<multiLineString.getNumGeometries(); i++) {
			this._translateLineString(multiLineString.getGeometryN(i));
		}
	},

	_translateMultiPolygon : function (multiPolygon) {
		for (var i=0; i<multiPolygon.getNumGeometries(); i++) {
			this._translatePolygon(multiPolygon.getGeometryN(i));
		}
	}
});