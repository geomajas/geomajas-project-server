dojo.provide("geomajas.spatial.geometry.LinearRing");
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
dojo.require("geomajas.spatial.MathLib");

dojo.declare("LinearRing", LineString, {

	/**
	 * @fileoverview Geometry implementation for LinearRings.
	 * @class Geometry implementation for LinearRings. The LinearRing is an 
	 * extension of a {@link LineString} with the added requirement that the
	 * array of coordinates is always closed (the first coordinate must equal
	 * the last). Also the subsequent linesegments may not intersect any other
	 * linesegment of the LinearRing.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends LineString
	 */
	constructor : function () {
	},

	/**
	 * Create a copy of this geometry and return it.
	 */
	clone : function () {
		var fac = this.getGeometryFactory();
		var coords = [];
		for(var i=0; i<this.coordinates.length; i++) {
			coords[i] = this.coordinates[i].clone();
		}
		return fac.createLinearRing(coords);
	},

	/**
	 * Self-intersection is not allowed, so this function must always return
	 * true.
	 */
	isSimple : function () {
		return true;
	},

	/**
	 * Checks to see if the last coordinate equals the first.
	 * Should always be the case!
	 */
	isClosed : function () {
		if (this.isEmpty()) {
			return false;
		}
		if (this.getNumPoints() == 1) {
			return false;
		}
		return this.coordinates[0].equals(this.coordinates[this.coordinates.length-1]);
	},

	/**
	 * An empty LinearRing is valid. Furthermore this object must be closed
	 * and must not self-intersect.
	 */
	isValid : function () {
		if (this.isEmpty()) {
			return true;
		}
		if (!this.isClosed()) {
			return false;
		}
		return (!this.intersects(this));
	},
	
	/**
	 * check if not self-intersecting. For a closed linear ring, the last point is not counted !!!!
	 */
	isNotIntersecting : function () {
		if (this.isEmpty()) {
			return true;
		} else if(!this.isClosed()){
			return (!this.intersects(this));
		} else if(this.coordinates.length >= 5) { // at least 4 different points required !
			var math = new MathLib();
			for (var i=0; i < this.coordinates.length -2; i++) {
				for (var j=0; j < this.coordinates.length - 2; j++) {
					if (math.lineIntersects(
						this.coordinates[i], this.coordinates[i+1],
					    this.coordinates[j], this.coordinates[j+1]
					    )) {
						return false;
					}
				}
			}			
		}
		return true;
	},
	

	/**
	 * Since a LinearRing is closed, it has an area.
	 */
	getArea : function () {
		var area = 0;
		for (var i=1; i<this.coordinates.length; i++) {
			var x1 = this.coordinates[i-1].getX();
			var y1 = this.coordinates[i-1].getY();
			var x2 = this.coordinates[i].getX();
			var y2 = this.coordinates[i].getY();
			area += x1*y2 - x2*y1;
		}
		return Math.abs(parseFloat(area/2));
	},

	/**
	 * The centroid is also known as the "centre of gravity" or the
	 * "center of mass".
	 * @return Return the centre point.
	 */
	getCentroid : function () {
		var area = 0; // We need a signed area!! We can improve this function a lot !!
		for (var i=1; i<this.coordinates.length; i++) {
			var x1 = this.coordinates[i-1].getX();
			var y1 = this.coordinates[i-1].getY();
			var x2 = this.coordinates[i].getX();
			var y2 = this.coordinates[i].getY();
			area += x1*y2 - x2*y1;
		}
		area = parseFloat(area/2);

		var x = 0;
		var y = 0;
		for (var i=1; i<this.coordinates.length; i++) {
			var x1 = this.coordinates[i-1].getX();
			var y1 = this.coordinates[i-1].getY();
			var x2 = this.coordinates[i].getX();
			var y2 = this.coordinates[i].getY();
			x += (x1+x2)*(x1*y2-x2*y1);
			y += (y1+y2)*(x1*y2-x2*y1);
		}
		x = parseFloat(x / (6*area));
		y = parseFloat(y / (6*area));
		return new Coordinate (x, y);
	},

	/**
	 * Add a coordinate to the list. Shouldn't be here.
	 */
	appendCoordinate : function (coordinate) {
		var linearring = this._getPrimaryCoordinates();
		var length = linearring.length;
		if (length == 0) {
			linearring[0] = coordinate;
			linearring[1] = coordinate;
		} else {
			linearring[length-1] = coordinate;
			linearring[length] = linearring[0];
		}		
	},
	
	revert: function () {
		for(var i=1, j=this.coordinates.length-2 ; i<j; i++,j--) {
			tmp = this.coordinates[j];
			this.coordinates[j] = this.coordinates[i];
			this.coordinates[i] = tmp;
		}
		return this;
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
		var area = 0;
		var intersect = this._getIntersectionWithRectangle(rectangleClockWise, rectangleCounterClockWise);

		if (intersect) {
			area = intersect.getArea();
		}
		return area;

	},

	/*
	 *  return:
	 *  	< 0 : this linear ring has a clockwise direction
	 *  	> 0 : this linear ring has a counter-clockwise direction
	 *      0   : the direction can not be determined (e.g. geometry is empty, all points are colinear)
	 */
	getDirection : function () {
		if (this.coordinates.length <= 3) {
			return 0;
		}
		var area = 0;
		for (var i=1; i<this.coordinates.length; i++) {
			var x1 = this.coordinates[i-1].getX();
			var y1 = this.coordinates[i-1].getY();
			var x2 = this.coordinates[i].getX();
			var y2 = this.coordinates[i].getY();
			area += x1*y2 - x2*y1;
		}
		/*  Area will be positive if counter-clockwise direction.*/
		return area;
	},

	log : function () {
		dojo.debug("LinearRing: ");
		var coords = this.getCoordinates();
		for (var i=0; i<this.getNumPoints(); i++) {
			dojo.debug("\t"+coords[i].toString());
		}
	},

	/**
	 * Get the geometry obtained by intersecting the polygon (no holes) with "this" linear ring as its shell
	 * and a given rectangle.
	 * @param  rectangleClockWise		 	Clockwise directed rectangle (LinearRing)
	 * @param  rectangleCounterClockWise 	Same rectangle, but directed counter-clockwise (LinearRing)
	 *
	 * @return NULL if no intersection, else the intersection lineString
	 */
	_getIntersectionWithRectangle : function (rectangleClockWise, rectangleCounterClockWise) {
		var intersectionRing = null;
		var rectangle = null;

		/*  Create a clockwise linear ring with the same extend as the Bbox
		 *  and with the same direction (clockwise/counter-clockwise) as the geometry
		 */

		var dir = this.getDirection();

		if (dir > 0) {	 /* only if this.geometry has a counter-clockwise direction */
			rectangle = rectangleCounterClockWise;
		}
		else if (dir < 0) {
			rectangle = rectangleCounterClockWise;
		}
		else if (dir = 0) {
			return  new LinearRing(); /* empty intersection */
		}

		/* collect intersection points */
		var crossingStore = new Array(this.coordinates.length);

		var intersectPointsRing = this._getIntersectPoints(rectangle, crossingStore, true);

		var intersectPointsRectangle = rectangle._getIntersectPoints(this, crossingStore, false);

		intersectionRing = this._combineIntersectPoints(intersectPointsRing, intersectPointsRectangle, crossingStore);
						/* either of the 2 intersectPoints arrays can be empty */

		return intersectionRing;

	},

	/**
	 * @param  poly2		 			Shell of a polygon (lineString, must have the same direction as "this" poly)
	 * @param  crossingStore		 	Store for crossing points (intersection
	 * 											points  of edges of this poly and poly2)
	 * @param  addToStore				If true, add crossing points to the crossingStore
	 *
	 * @return 	array of intersection points (corners of this poly lying inside poly2 + intersection
	 * 											points  of edges of this poly and poly2)
	 */
	_getIntersectPoints : function (poly2, crossingStore, addToStore) {
		var intersectPoints =  new Array();
		var mathLib = new MathLib();

		for (var i=1; i<this.coordinates.length; i++) {
			if (mathLib.isWithin(poly2, this.coordinates[i-1])) {
				intersectPoints.push([this.coordinates[i-1], true]);
			}
			if (addToStore) {
				crossingStore[i-1] = new Array(poly2.coordinates.length-1);
			}
			/* Determine intersection points between current edge and all edges of poly2 */
			var crossingPoints =  new Array();

			for (var iPoly2=1; iPoly2< poly2.coordinates.length; iPoly2++) {

				/* TODO: store the intersection points of the edges only once in a matrix[][],
				 * with matrix[i][j] = intersection point of edge i of poly1 and edhe j of poly 2
				 * */

				var coord = mathLib.lineSegmentIntersection(poly2.coordinates[iPoly2-1], poly2.coordinates[iPoly2],
						this.coordinates[i-1], this.coordinates[i]);
				if (coord != null) {
					if (addToStore) {
						/* Store the intersection points of the edges only once in a matrix[][],
						 * with matrix[i][j] = intersection point of edge i of poly1 and edhe j of poly 2
						 */
						crossingStore[i-1][iPoly2-1] = coord;
						crossingPoints.push([i-1,iPoly2-1]);
					}
					else {
						crossingPoints.push([iPoly2-1, i-1]);
					}
				}

			} /* for */
			/* order intersection points according to increasing distance from this.coordinates[i-1]  */
			ref = this.coordinates[i-1];
			crossingPoints.sort(function(tuple1,tuple2) {
				var deltaX;
				var deltaY;

				var p1 = crossingStore[tuple1[0]][tuple1[1]];
				var p2 = crossingStore[tuple2[0]][tuple2[1]];
				deltaX = ref.getX() - p1.getX();
				deltaY = ref.getY() - p1.getY();
				var d1 = deltaX*deltaX + deltaY*deltaY;
				deltaX = ref.getX() - p2.getX();
				deltaY = ref.getY() - p2.getY();
				var d2 = deltaX*deltaX + deltaY*deltaY;
				return (d1-d2);	}
			);
			for (var j = 0;  j < crossingPoints.length; j++) {
				intersectPoints.push([crossingPoints[j], false]);
			}
		}
		return intersectPoints;
	},


	/**
	 * @param  intersect1		 		Array of intersection points of poly1 with poly2, including the corners of poly1 lying inside poly2
	 * 									(poly1 and poly2 must have the same direction)
	 * @param  intersect2		 		Array of intersection points of poly2 with poly1
	 * @param  crossingStore		 	Store for crossing points (intersection
	 * 											points  of edges of this poly and poly2)
	 *
	 * @return 	LinearRing of intersection area of poly1 and poly2
	 */

	_combineIntersectPoints : function (intersect1, intersect2, crossingStore) {
		var intersectionRing = new LinearRing();

		var followPoly1  = false;
		var doneFromPoly2 = 0;
		var j;

		if (intersect1.length == 0) {
			/* if intersect2.length != 0 , poly2 lies completely within poly1
			 * swap intersect1 and intersect2 */
			intersect1 = intersect2;
			intersect2 = new Array(); /* empty */
		}

		for (var i=0 ;  i < intersect1.length ; i++) {
			if (intersect1[i][1]) { /* corner of poly1 */
				followPoly1 = true;
				intersectionRing.appendCoordinate(intersect1[i][0]);
			}
			else {
				var tuple = intersect1[i][0];
				var p = crossingStore[tuple[0]][tuple[1]];
				intersectionRing.appendCoordinate(p);

				 /* switch from following poly1 to poly2 or vice versa */
				followPoly1 =  !followPoly1;

				if (!followPoly1) { /* now follow edge of poly2 */
					if (doneFromPoly2 == 0) {
						/* sync poly 2 with poly 1 on intersection specified by tuple  */

						for (k=0; k < intersect2.length ; k++) {
							if (!intersect2[k][1]) {
								var tuple2 = intersect2[k][0];
								if (tuple2[0] == tuple[0] && tuple2[1] == tuple[1]) {
									j = k;  /* found sync crossing point */
									break;
								}
							}
						}
					}
					if (doneFromPoly2 < intersect2.length  && !intersect2[j][1]) {
						doneFromPoly2++;   /* but do not add intersection point, since already added */
						j = (j+1) % intersect2.length;
					}
					while (doneFromPoly2 < intersect2.length  && intersect2[j][1])  {
						intersectionRing.appendCoordinate(intersect2[j][0]); /* add corner of poly2 */
						doneFromPoly2++;
						j = (j+1) % intersect2.length;
					}
				} /* if (followPoly1 ==  false) */
			}
		} /* for all points in intersect1 */
		return intersectionRing;
	}
});