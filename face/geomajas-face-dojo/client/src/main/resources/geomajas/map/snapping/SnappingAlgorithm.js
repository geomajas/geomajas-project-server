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

dojo.provide("geomajas.map.snapping.SnappingAlgorithm");
dojo.declare("SnappingAlgorithm", null, {

	/**
	 * @fileoverview General abstraction for snapping algorithms.
	 * @class This abstract class needs to be extened by actual implementations
	 * of a snapping algorithm.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @param geometries Array of geometries on which to apply snapping.
	 */
	constructor : function (geometries) {
		this.geometries = geometries;
		this.minDistance = Number.MAX_VALUE;
	},

	/**
	 * The simplest of all snapping algorithms: Don't snap at all
 	 * @ret				  Returns closest snappoint if snapping applies, else 
	 *					  returns null.
	 */
	calcSnapPoint : function (coordinate, minDistance) {
		return null;
	},

	/**
	 * Getter for the minimum distance.
	 * @return Returns the current minimum distance.
	 */
	getMinDistance : function () {
		return this.minDistance;
	},

	/**
	 * @private
	 * This function retrieves the coordinates of a given geometry as a 2D
	 * array.
	 */
	_getAllCoordinates : function (geometry) {
		if (geometry instanceof MultiPolygon) {
			var coords = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				var polygon = geometry.getGeometryN(i);
				for (var j=0; j<polygon.getNumGeometries(); j++) { 
					/* j: 0 .. number of rings - 1 (interior + exterior) */
					coords.push (polygon.getGeometryN(j).getCoordinates());
				}
			}
			return coords;
		} else if (geometry instanceof Polygon) {
			var coords = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				coords[i] = geometry.getGeometryN(i).getCoordinates();
			}
			return coords;
		} else if (geometry instanceof MultiLineString) {
			var coords = [];
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				coords[i] = geometry.getGeometryN(i).getCoordinates();
			}
			return coords;
		} else if (geometry instanceof LineString) {
			return [geometry.getCoordinates()];
		} else if (geometry instanceof Point) {
			return [geometry.getCoordinates()];
		}
	}
});