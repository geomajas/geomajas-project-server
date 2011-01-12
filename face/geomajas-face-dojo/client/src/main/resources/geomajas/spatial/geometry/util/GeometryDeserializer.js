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