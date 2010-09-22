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

dojo.provide("geomajas.map.snapping.NearestSnap");
dojo.require("geomajas.util.common");

dojo.declare("NearestSnap", SnappingAlgorithm, {

	/**
	 * @fileoverview Snapping algorithm that snaps to any point on a
	 * geometry's edge.
	 * @class Snapping algorithm that snaps to any point on a
	 * geometry's edge.
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
	},


	/**
	 * Actual snapping algorithm. Snaps to the nearest point on any of the
	 * edges of the geometries. This includes endpoints.
	 * @param coordinate The mouse cursor position. This coordinate is to be
	 *                   snapped to the list of geometries.
	 * @param minDistance Current minimum distance. This might differ from the
	 *                    snapping rule's distance because more then one
	 *                    snapping rules can be defined for a layer.
	 * Actual snapping algorithm. 
	 * 
	 * @ret				  Returns closest snappoint if snapping applies, else 
	 *					  returns null.
	 */
	calcSnapPoint : function (coordinate, minDistance) {
	
		var snapPoint = null;

 		if (this.geometries != null) {
			for (var i=0; i<this.geometries.length; i++) {
				var coords = this._getAllCoordinates(this.geometries[i]); // array of arrays.
				for (var j=0; j<coords.length; j++) {
					var ring = coords[j];
					for (var k=1; k<ring.length; k++) {
						var ls = new LineSegment (ring[k-1], ring[k]);
						var distance = ls.distance(coordinate);
						if (distance < minDistance && distance < this.ruleDistance) {
							this.minDistance = minDistance = distance;
							snapPoint = ls.nearest(coordinate);
						}
					}
				}
			}
		}
		return snapPoint;
 	},
	getMinDistance : function() {
		return this.minDistance;
	} 	
});