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