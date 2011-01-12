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