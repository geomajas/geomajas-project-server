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

dojo.provide("geomajas.map.snapping.Snapper");
dojo.require("geomajas.spatial.MathLib");

dojo.declare("Snapper", null, {

	/**
	 * @fileoverview Class that is able to snap points, using the snap function.
	 * @class Class that is able to snap points, using the snap function.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @param layer The layer object for which to make use of snapping.
	 * @param mapModel The mapModel from which to get the snapping layers.
	 */
	constructor : function (layer, mapModel) {
		/** The layer object for which to make use of snapping. */
		this.layer = layer;

		/** The mapModel from which to get the snapping layers. */
		this.mapModel = mapModel;

		/** Is this snapper active or not. */
		this.active = false;
		this.mode = 1; /* default = snap preferably to enclosing geometry */
	},

	/**
	 * Function that does the actual snapping. Takes in a coordinate and
	 * returns the snapped coordinate. Of course, when snapping is not active,
	 * the coordinate is returned immediatly. But when snapping is active,
	 * this function will search for the closest snapping point that fits one
	 * of the snapping rules defined for the referenced layer.
	 * @param coordinate The Coordinate to search a snapping point for.
	 * @return Returns the snapped point, or the same point.
	 */
	snap : function (coordinate) {
		if (!this.active) {
			return coordinate;
		}
		var minDistance = Number.MAX_VALUE;
		var minDistanceEnclosingGeometry = Number.MAX_VALUE;
		var snapPoint   = coordinate; 
		var snapPointEnclosingGeometry  = null;
		
		for (var i=0; i<this.layer.getSnappingRules().count; i++) {
			var currentRule = this.layer.getSnappingRules().item(i);
			if (currentRule.getType() != "CLOSEST_ENDPOINT" && currentRule.getType() != "NEAREST_POINT") {
				return; /* Usage error */
			} 
			
			var bounds = new Bbox(coordinate.getX()-currentRule.getDistance(),
					coordinate.getY()-currentRule.getDistance(), currentRule.getDistance()*2, 
					currentRule.getDistance()*2);
			var mode = this.mode; /* if 1 give preference to geometries in which the point lies */

			var snapLayer = this.mapModel.getLayerById (this.mapModel.getId()+"."+currentRule.getLayer());
			if (snapLayer.layerType != geomajas.LayerTypes.POLYGON && snapLayer.layerType != geomajas.LayerTypes.MULTIPOLYGON) {
				mode = 0; /* Do NOT give preference to geometries in which the point lies for linestrings etc. (area=0)  */
			}		
			function _callBack (/*FeatureNode*/data) {
				var features = data.getFeatures(); /* bef 1.3.x merge: getFeatures().getValueList();*/
				var geometries = [];
				var mathLib = new MathLib();
				
				for (var j=0; j<features.length; j++) {
					var geometry = features[j].getGeometry()
					// For multipolygons and multilinestrings, we calculate bounds intersection
					// for each partial geometry. This way we can send parts of the complex
					// geometries to the snapping list, and not always the entire geometry.(=faster)
					var currentGeometries = [];
					if (geometry instanceof MultiLineString || geometry instanceof MultiPolygon) {
						for (var i=0; i<geometry.getNumGeometries(); i++) {
							var geomN = geometry.getGeometryN(i);
							if (geomN.getBounds().intersects(bounds)) {
								currentGeometries.push(geomN);
							}
						}
					} else {
						if (geometry.getBounds().intersects(bounds)) {
							currentGeometries.push(geometry);
						}
					}
					if (currentGeometries.length != 0) {
						if (mode == 1 && mathLib.isWithin(geometry, coordinate) ) {
							var algorithm;
						
							if (currentRule.getType() == "CLOSEST_ENDPOINT") {
								algorithm = new ClosestPoint(currentGeometries, currentRule.getDistance());
							} else {
								algorithm = new NearestSnap (currentGeometries, currentRule.getDistance());
							}
							var snapPointIfFound = algorithm.calcSnapPoint(coordinate, minDistanceEnclosingGeometry);
							if (snapPointIfFound != null) {
								snapPoint = snapPointEnclosingGeometry = snapPointIfFound;
								minDistanceEnclosingGeometry = algorithm.getMinDistance();
							}
						}  /* mode == 1 && geometry.liesInside(coordinate) */	
						else {			
							geometries = geometries.concat(currentGeometries);
						}
					} /* if (geometry.getBounds().intersects(bounds)) */
				} /* for */
				if (snapPointEnclosingGeometry == null && geometries.length != 0) { /* No snappoint on an enclosing geom found yet */
					var algorithm; 
					if (currentRule.getType() == "CLOSEST_ENDPOINT") {
						algorithm = new ClosestPoint(geometries, currentRule.getDistance());
					} else if (currentRule.getType() == "NEAREST_POINT") {
						algorithm = new NearestSnap (geometries, currentRule.getDistance());
					}
					else {
						return; /* Usage error */
					}
					var snapPointIfFound = algorithm.calcSnapPoint(coordinate, minDistance);
					if (snapPointIfFound != null) {
						snapPoint = snapPointIfFound;
						minDistance = algorithm.getMinDistance();
					}
				} /* if (snapPointEnclosingGeometry == null) */
			} /* call-back */

			
			snapLayer.getFeatureStore().applyOnBounds (bounds,/* 1.1.x: null,*/ _callBack);
		}
		return snapPoint;
	},

	/**
	 * Is snapping on or off?
	 * @return true or false.
	 */
	isActive : function () {
		return this.active;
	},

	/**
	 * Activate or deactivate this snapper.
	 * @param active true or false.
	 */
	setActive : function (active) {
		this.active = active;
	},
	
	setMode : function (mode) {
		this.mode = mode;
	},
	
	getMode : function () {
		return this.mode;
	}
});