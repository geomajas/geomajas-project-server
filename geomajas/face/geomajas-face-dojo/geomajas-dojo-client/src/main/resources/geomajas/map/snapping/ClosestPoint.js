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

dojo.provide("geomajas.map.snapping.ClosestPoint");
dojo.require("geomajas.util.common");
/*
sortByX = function (c1,c2) {
	return c1.getX()-c2.getX();
}

sortByY = function (c1,c2) {
	return c1.getY() -c2.getY();
}
*/
dojo.declare("ClosestPoint", SnappingAlgorithm, {

	/**
	 * @fileoverview Snapping algorithm that snaps to the closest endpoint of
	 * a geometry.
	 * @class Snapping algorithm that snaps to the closest endpoint of a
	 * geometry.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @param geometries Array of geometries on which to apply snapping.
	 * @param ruleDistance The distance specified in the config's snapping rule.
	 */
	constructor : function (geometries, ruleDistance) {
		this.minDistance = Number.MAX_VALUE;
		this.geometries = geometries;
		this.ruleDistance = parseFloat(ruleDistance);
		this.v1 = new Vector2D();
		this.v2 = new Vector2D();
		
		this.sortByX = function (c1,c2) {
			return c1.getX()-c2.getX();
		}
		this.sortByY = function (c1,c2) {
			return c1.getY() -c2.getY();
		}

		this.coordinates = this._getAllCoordinatesAsArray();
		this.coordsX = this.coordinates.slice(0).sort(this.sortByX);
		this.coordsY = this.coordinates.slice(0).sort(this.sortByY);
	},

	/**
	 * Actual snapping algorithm. Snaps to the nearest point of any of the
	 * geometries. This includes endpoints.
	 * @param coordinate The mouse cursor position. This coordinate is to be
	 *                   snapped to the list of geometries.
	 * @param minDistance Current minimum distance. This might differ from the
	 *                    snapping rule's distance because more then one
	 *                    snapping rules can be defined for a layer.
	 * 
	 * @ret				  Returns closest snappoint if snapping applies, else 
	 *					  returns null.
	 */
	calcSnapPoint : function (coordinate, minDistance) {
	
		var snapPoint = null;
		this.v1.fromCoordinate(coordinate);
		var coords = this._getPossibleCoordinates(coordinate);
		for(var i = 0; i < coords.length; i++) {
			var coord = coords[i];
			this.v2.fromCoordinate(coord);
			var distance = this.v1.distance(this.v2);
			if (distance < minDistance && distance < this.ruleDistance) {
				this.minDistance = minDistance = distance;
				snapPoint = coord;
			}
		}
 		return snapPoint;
	},
	
	getMinDistance : function() {
		return this.minDistance;
	},
	

	/**
	 * @private
	 */
	_getAllCoordinatesAsArray : function () {
		var coords = [];
		if (this.geometries != null) {
			for (var i=0; i<this.geometries.length; i++) {
				var rings = this._getAllCoordinates(this.geometries[i]);
				for (var j=0; j<rings.length; j++) {
					var ring = rings[j];
					for (var k=0; k<ring.length; k++) {
						coords.push(ring[k]);						
					}
				}
			}
		}
		return coords;
	},

	/**
	 * @private
	 */
	_getPossibleCoordinates : function (coordinate) {
		var xmin = this.coordsX.binarySearch(this.sortByX,new Coordinate(coordinate.getX()-this.ruleDistance,0));
		var xmax = this.coordsX.binarySearch(this.sortByX,new Coordinate(coordinate.getX()+ this.ruleDistance,0));
		var ymin = this.coordsY.binarySearch(this.sortByY,new Coordinate(0,coordinate.getY()-this.ruleDistance));
		var ymax = this.coordsY.binarySearch(this.sortByY,new Coordinate(0,coordinate.getY()+this.ruleDistance))		;

		var coords = [];

		for(var i = xmin; i < xmax; i++){
			coords.push(this.coordsX[i]);
		}

		for(var j = ymin; j < ymax; j++){
			coords.push(this.coordsY[j]);
		}
		return coords;	
	}
});