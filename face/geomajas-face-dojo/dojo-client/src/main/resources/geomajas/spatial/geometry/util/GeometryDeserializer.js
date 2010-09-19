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

dojo.provide("geomajas.spatial.geometry.util.GeometryDeserializer");
dojo.require("geomajas._base");
dojo.declare("GeometryDeserializer", null, {

	/**
	 * @fileoverview Serializer for geometries over the json rpc.
	 * @class Serializer for geometries over the json rpc. Well actualy it only
	 * deserializes incoming geometries, but i've kept the name of the class
	 * analogous to it's java counterpart for convenience.
	 * @author Pieter De Graef
	 * @constructor
	 */
	constructor : function () {
		//log.info("createGeometryFromJSON() , creating default");
		this.factory = new GeometryFactory(4326, 5);
	},

	/**
	 * The only public function. It gets a json geometry string as input, and
	 * returns a geometry object.
	 * @param jsonGeometry The JSON geometry string.
	 * @return A geometry object. Can be of any type.
	 */
	createGeometryFromJSON : function (jsonGeometry) {
		//log.info("createGeometryFromJSON() , srid="+jsonGeometry.srid);
		this.factory.setSRID(jsonGeometry.srid);
		if (jsonGeometry.precision > 0) {
			this.factory.setPrecision(jsonGeometry.precision);
		}

		var geometry = null;
		if(jsonGeometry.type == geomajas.GeometryTypes.POINT){
			geometry = this._createPointFromJSON(jsonGeometry);
		}
		else if(jsonGeometry.type == geomajas.GeometryTypes.POLYGON) {
			geometry = this._createPolygonFromJSON(jsonGeometry);
		} 
		else if(jsonGeometry.type == geomajas.GeometryTypes.MULTIPOLYGON) {
			geometry = this._createMultiPolygonFromJSON(jsonGeometry);
		} 
		else if(jsonGeometry.type == geomajas.GeometryTypes.LINESTRING) {
			geometry = this._createLineStringFromJSON(jsonGeometry);
		}
		else if (jsonGeometry.type == geomajas.GeometryTypes.MULTILINESTRING) {
			geometry = this._createMultiLineStringFromJSON(jsonGeometry);
		}
		return geometry;
	},

	/**
	 * @private
	 */
	_createPointFromJSON : function (jsonGeometry) {
		var parsed = this._parseCoordinates(jsonGeometry.coordinates);
		return this.factory.createPoint(parsed[0]);
	},

	/**
	 * @private
	 */
	_createLineStringFromJSON : function (jsonGeometry) {
		var parsed = this._parseCoordinates(jsonGeometry.coordinates);
		return this.factory.createLineString(parsed);
	},

	/**
	 * @private
	 */
	_createLinearRingFromJSON : function (jsonGeometry) {
		var parsed = this._parseCoordinates(jsonGeometry.coordinates);
		return this.factory.createLinearRing(parsed);
	},

	/**
	 * @private
	 */
	_createMultiLineStringFromJSON : function (jsonGeometry) {
		var lines = [];
		for (var i=0; i<jsonGeometry.lineStrings.length; i++) {
			lines[i] = this._createLineStringFromJSON(jsonGeometry.lineStrings[i]);
		}
		return this.factory.createMultiLineString(lines);
	},

	/**
	 * @private
	 */
	_createPolygonFromJSON : function (jsonGeometry) {
		var shell = this._createLinearRingFromJSON(jsonGeometry.shell);
		var holes = [];
		for (var i=0; i<jsonGeometry.holes.length; i++) {
			holes[i] = this._createLinearRingFromJSON(jsonGeometry.holes[i]);
		}
		return this.factory.createPolygon(shell, holes);
	},

	/**
	 * @private
	 */
	_createMultiPolygonFromJSON : function (jsonGeometry) {
		var polys = [];
		for (var i=0; i<jsonGeometry.polygons.length; i++) {
			polys[i] = this._createPolygonFromJSON(jsonGeometry.polygons[i]);
		}
		return this.factory.createMultiPolygon(polys);
	},

	/**
	 * @private
	 */
	_parseCoordinates : function (coordinates) {
		if(coordinates instanceof Array) {
			var result = [];
			for(var i = 0; i < coordinates.length; i+=2){
				var c = new Coordinate(coordinates[i], coordinates[i+1]);
				result.push(c);
			}
			return result;
		} else {
			return [];
		}
	}
});