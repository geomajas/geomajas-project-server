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