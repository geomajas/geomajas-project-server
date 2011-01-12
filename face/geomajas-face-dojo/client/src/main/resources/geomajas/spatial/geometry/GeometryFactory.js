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

dojo.provide("geomajas.spatial.geometry.GeometryFactory");
dojo.require("geomajas._base");
dojo.require("geomajas.spatial.Bbox");

dojo.declare("GeometryFactory", null, {

	/**
	 * @fileoverview Factory object for geometries.
	 * @class Factory object for geometries.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param srid Spatial Reference id (passive, because a layer already
	 *             defines this).
	 * @param precision The floating point precision to be used (TODO !).
	 */
	constructor : function (srid, precision) {
		if (!srid) {
			//log.info("GeometryFactory : default srid = 4326");
			this.srid = 4326;
		} else {
			this.srid = srid;
		}

		if (!precision || precision < 0) {
			this.precision = 5;
		} else {
			this.precision = precision;
		}
		//log.info("GeometryFactory() : "+this.srid+","+this.precision);
	},

	/**
	 * Set a new Spatial Reference id for this factory.
	 */
	setSRID : function (srid) {
		this.srid = srid;
	},

	/**
	 * Set a new precision for this factory.
	 */
	setPrecision : function (precision) {
		this.precision = precision;
	},

	/**
	 * Create a new {@link Point}, given a coordinate.
	 * @param coordinate The {@link Coordinate} object that positions the
	 *                   point.
	 * @return Returns a {@link Point} object.
	 */
	createPoint : function (coordinate) {
		var point = new Point(geomajas.GeometryTypes.POINT, this.srid, this.precision);
		point.appendCoordinate(coordinate);
		return point;
	},

	/**
	 * Create a new {@link LineString}, given an array of coordinates.
	 * @param coordinates An array of {@link Coordinate} objects.
	 * @return Returns a {@link LineString} object.
	 */
	createLineString : function (coordinates) {
		var ls = new LineString (geomajas.GeometryTypes.LINESTRING, this.srid, this.precision);
		if (coordinates instanceof Array) {
			ls.coordinates = coordinates; // No friend functions, or package scope in javascript, so this is the result...
		}
		return ls;
	},

	/**
	 * Create a new {@link MultiLineString}, given an array of linestrings.
	 * @param lineStrings An array of {@link LineString} objects.
	 * @return Returns a {@link MultiLineString} object.
	 */
	createMultiLineString : function (lineStrings) {
		var mls = new MultiLineString(geomajas.GeometryTypes.MULTILINESTRING, this.srid, this.precision);
		if (lineStrings instanceof Array) {
			mls.lineStrings = lineStrings; // No friend functions, or package scope in javascript, so this is the result...
		}
		return mls;
	},

	/**
	 * Create a new {@link LinearRing}, given an array of coordinates.
	 * @param coordinates An array of {@link Coordinate} objects. This function
	 *                    checks if the array is closed, and does so itself if
	 *                    needed.
	 * @return Returns a {@link LinearRing} object.
	 */
	createLinearRing : function (coordinates) {
		var lr = new LinearRing(geomajas.GeometryTypes.LINEARRING, this.srid, this.precision);
		if (coordinates instanceof Array && coordinates.length > 0) {
			lr.coordinates = coordinates; // No friend functions, or package scope in javascript, so this is the result...
			var last = coordinates[coordinates.length-1];
			if (!coordinates[0].equals(last)) {
				lr.coordinates.push(coordinates[0].clone());
			}
		}
		return lr;
	},

	/**
	 * Create a new {@link Polygon}, given a shell and and array of holes.
	 * @param shell A {@link LinearRing} object that represents the outer ring.
	 * @param holes An array of {@link LinearRing} objects representing the
	 *              holes.
	 * @return Returns a {@link Polygon} object.
	 */
	createPolygon : function (shell, holes) {
		var pol = new Polygon(geomajas.GeometryTypes.POLYGON, this.srid, this.precision);
		if (shell instanceof LinearRing) {
			pol.shell = shell; // No friend functions, or package scope in javascript, so this is the result...
		}
		if (holes instanceof Array) {
			pol.holes = holes; // No friend functions, or package scope in javascript, so this is the result...
		} else {
			pol.holes = [];
		}
		return pol;
	},

	/**
	 * Create a new {@link MultiPolygon}, given an array of polygons.
	 * @param polygons An array of {@link Polygon} objects .
	 * @return Returns a {@link MultiPolygon} object.
	 */
	createMultiPolygon : function (polygons) {
		var mp = new MultiPolygon(geomajas.GeometryTypes.MULTIPOLYGON, this.srid, this.precision);
		if (polygons instanceof Array) {
			mp.polygons = polygons; // No friend functions, or package scope in javascript, so this is the result...
		}
		return mp;
	}
});