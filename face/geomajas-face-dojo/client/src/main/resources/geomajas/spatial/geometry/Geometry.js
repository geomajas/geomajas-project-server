dojo.provide("geomajas.spatial.geometry.Geometry");
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
dojo.require("geomajas.spatial.Bbox");
dojo.require("geomajas.spatial.MathLib");

dojo.declare("Geometry", null, {

	/**
	 * @fileoverview General definition of a geometry.
	 * @class General definition of a geometry. Should always be initialized
	 * through a {@link GeometryFactory} object. This base class for all 
	 * geometries is based on the JTS implementation.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param type The geometry type.
	 * @param srid Spatial Reference id.
	 * @param precision The desired precision.
	 */
	constructor : function (type, srid, precision) {
		this.type = type;
		this.srid = srid;
		this.precision = precision;
	},

	/**
	 * Return the {@link GeometryFactory} object that corresponds to this
	 * geometry.
	 */
	getGeometryFactory : function (){
		return new GeometryFactory(this.srid, this.precision);
	},

	/**
	 * Create a copy of this geometry and return it.
	 */
	clone : function () {
		alert("Geometry.clone : override me!");
	},

	/**
	 * Return a coordinate, or null. Each implementation of this class
	 * should override it.
	 * @param n Index in the geometry. This can be an integer value or an array
	 *          of values.
	 * @return A coordinate or null.
	 */
	getCoordinateN : function (n) { // sometimes n is an array....
		alert("Geometry.getCoordinateN : override me!");
	},

	/**
	 * Get an array of coordinates.
	 */
	getCoordinates : function () {
		return this._getPrimaryCoordinates();
	},

	/**
	 * Returns the closest Bbox around the geometry.
	 */
	getBounds : function () {
		return this._computeBounds(this._getPrimaryCoordinates());
	},

	/**
	 * Return the number of coordinates.
	 */
	getNumPoints : function () {
		return this._getPrimaryCoordinates().length;
	},

	/**
	 * Returns a sub-geometry or null, or the geometry itself.
	 * Each implementation of this class should override it.
	 * @param n Index in the geometry. This can be an integer value or an array
	 *          of values. 
	 * @return A geometry object.
	 */
	getGeometryN : function (n) {
		alert("Geometry.getGeometryN : override me!");
	},

	/**
	 * Return the number of direct sub-geometries.
	 */
	getNumGeometries : function () {
		alert("Geometry.getNumGeometries : override me!");
	},

	/**
	 * Return the geometry type. Can be one of the following:
	 * <ul>
	 * 	<li>POINT</li>
	 * 	<li>LINESTRING</li>
	 * 	<li>LINEARRING</li>
	 * 	<li>POLYGON</li>
	 * 	<li>MULTILINESTRING</li>
	 * 	<li>MULTIPOLYGON</li>
	 * </ul>
	 */
	getGeometryType : function () {
		return this.type;
	},

	/**
	 * Return the spatial reference id.
	 */
	getSRID : function () {
		return this.srid;
	},

	/**
	 * This geometry is empty if there are no geometries/coordinates.
	 * @return true or false.
	 */
	isEmpty : function () {
		return (this.getNumPoints() == 0);
	},

	/**
	 * Basically this function checks if the geometrie is self-intersecting or
	 * not.
	 * @return True or false. True if there are no self-intersections in the
	 *         geometry.
	 */
	isSimple : function () {
		return this.intersects(this);
	},
	
	/**
	 * Is the geometry a valid one? Different rules apply to different geometry
	 * types. Each geometry class should override this!
	 */
	isValid : function () {
		alert("Geometry.isValid : override me!");
	},

	// Getters and setters:

	/**
	 * Append a point to this geometry
	 * @param coordinate of point
	*/
	appendCoordinate : function (coordinate) {
		alert('Geometry.appendCoordinate : override me !!!!');
	},

	/**
	 * Get area of the intersection of the polygon with this linear ring as its shell
	 * and a given rectangle.
	 *
	 * @param  rectangleClockWise		 	Clockwise directed rectangle (LinearRing)
	 * @param  rectangleCounterClockWise 	Same rectangle, but directed counter-clockwise (LinearRing)
	 *
	 * @return 0 if no intersection, else the area
	 *
	 *  @note Rectangles are provided in both directions for optimization reasons
	 */
	getAreaIntersectionWithRectangle : function (rectangleClockWise, rectangleCounterClockWise) {
		alert('Geometry.getAreaIntersectionWithRectangle : override me !!!!');
	},

	/**
	 * Calculate whether or not this geometry intersects with another.
	 * @param geometry The other geometry to check for intersection.
	 * @return Returns true or false.
	 */
	intersects : function (geometry) {
		if (geometry == null) {
			return false;
		}
		var math = new MathLib();
		var arr1 = this.getCoordinates();
		var arr2 = geometry.getCoordinates();
		for (var i=0; i < arr1.length -1; i++) {
			for (var j=0; j < arr2.length - 1; j++) {
				if (math.lineIntersects(arr1[i], arr1[i+1], arr2[j], arr2[j+1])) {
					return true;
				}
			}
		}
		return false;
	},

	/**
	 * Return the area of the geometry.
	 */
	getArea : function () {
		alert("Geometry.getArea : override me");
	},

	/**
	 * Return the length of the geometry.
	 */
	getLength : function () {
		alert("Geometry.getLength : override me");
	},

	/**
	 * The centroid is also known as the "centre of gravity" or the
	 * "center of mass".
	 * @return Return the centre point.
	 */
	getCentroid : function () {
		alert("Geometry.getCentroid : override me");
	},
	
	/**
	 * Returns the minimal distance between this coordinate and any vertex of the geometry
	 * @return Return the minimal distance
	 */
	getDistance : function (coordinate) {
		alert("Geometry.getDistance : override me");
	},
		
	getDistanceApprox: function (coordinate) {
		alert("Geometry.getDistanceApprox : override me");
	},

	/**
	 * @private
	 * Helper function that returns the first list of coordinates
	 * - for points this is a singleton array with the coordinates of the point
	 * - for linestrings this is the  array of points of the linestring
	 * - for polygons this is the array of points of the outer linear ring
	 * - for multipolygons/multilinestrings the above is applied to the first geometry
	 */
	_getPrimaryCoordinates : function (){
		return null; // override!!!
	},

	/**
	* Helper functions that calculates the bounds of an array of coordinates
	*/
	_computeBounds : function (coords) {
		if (coords.length == 0) {
			return new Bbox();
		} else {
			var xMin = coords[0].getX();
			var yMin = coords[0].getY();
			var xMax = coords[0].getX();
			var yMax = coords[0].getY();
			for (var i=1; i<coords.length; i++) {
				if (coords[i].getX() < xMin) { xMin = coords[i].getX(); }
				if (coords[i].getX() > xMax) { xMax = coords[i].getX(); }
				if (coords[i].getY() < yMin) { yMin = coords[i].getY(); }
				if (coords[i].getY() > yMax) { yMax = coords[i].getY(); }
			}
			return new Bbox (xMin, yMin, xMax-xMin, yMax-yMin);
		}
	}
});