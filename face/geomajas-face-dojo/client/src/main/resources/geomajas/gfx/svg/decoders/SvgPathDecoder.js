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

dojo.provide("geomajas.gfx.svg.decoders.SvgPathDecoder");
dojo.require("geomajas._base");

dojo.declare("SvgPathDecoder", null, {

	/**
	 * @class Decoder for geometry object to SVG path d-attributes.
	 * This is not applicable on Point geometries, only lines and polygons.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * This function decodes a Geometry (line or polygon) to a path's
	 * d-attribute.
	 * @param geometry The Line of Polygon object to be decoded.
	 * @return Returns the d-attribute as a string.
	 */
	decode : function (geometry) {
		if (geometry == null) {
			return "";
		}
		if (geometry.declaredClass == geomajas.GeometryTypes.LINESTRING) {
			return this._decodeLine (geometry);
		} else if (geometry.declaredClass == geomajas.GeometryTypes.LINEARRING) {
			return this._decodeLinearRing (geometry);
		} else if (geometry.declaredClass == geomajas.GeometryTypes.POLYGON) {
			return this._decodePolygon (geometry);
		} else if (geometry.declaredClass == geomajas.GeometryTypes.MULTIPOLYGON) {
			return this._decodeMultiPolygon (geometry);
		} else if (geometry.declaredClass == geomajas.GeometryTypes.MULTILINESTRING) {
			return this._decodeMultiLine(geometry);
		}
		return "";
	},

	/**
	 * Private function for decoding line-geometries.
	 * @private
	 */
	_decodeLine : function (line) {
		if (line == null || line.isEmpty()) {
			return "";
		}
		var d = "M"+line.getCoordinates()[0].getX() + " " + line.getCoordinates()[0].getY();
		var pstr = [];
		for (var i=1; i<line.getCoordinates().length; i++) {
			var x = line.getCoordinates()[i].getX() - line.getCoordinates()[i-1].getX();
			var y = line.getCoordinates()[i].getY() - line.getCoordinates()[i-1].getY();
			pstr.push(" l "+x+" "+y);
		}
		return d + pstr.join("");
	},

	/**
	 * Private function for decoding multiline string-geometries.
	 * @private
	 */
	_decodeMultiLine : function (multiline) {
		var n = multiline.getNumGeometries();
		var pstr = [];
		for(var i = 0; i <= n; i++){
			pstr.push(this._decodeLine(multiline.getGeometryN(i)));
		}
		return pstr.join("");
	},

	/**
	 * Private function for decoding polygon-geometries.
	 * @private
	 */
	_decodePolygon : function (polygon) {
		if (polygon == null || polygon.isEmpty()) {
			return "";
		}
		var pstr = [];
		pstr.push(this._decodeLinearRing(polygon.getExteriorRing()));
		for (var i=0; i<polygon.getNumInteriorRing(); i++) {
			pstr.push(this._decodeLinearRing(polygon.getInteriorRingN(i)));
		}
		return pstr.join("");
	},

	/**
	 * Private function for decoding multipolygon-geometries.
	 * @private
	 */
	_decodeMultiPolygon : function (multipoly) {
		var n = multipoly.getNumGeometries();
		var pstr = [];
		for(var i = 0; i < n; i++){
			pstr.push(this._decodePolygon(multipoly.getGeometryN(i)));
		}
		return pstr.join("");
	},

	/**
	 * @private
	 */
	_decodeLinearRing : function (linearRing) {
		if (linearRing == null || linearRing.isEmpty()) {
			return "";
		}
		var d = "M";
		var pstr = [];
		for (var i=0; i<linearRing.getCoordinates().length-1; i++) {
			var pt = linearRing.getCoordinates()[i].getX() + " " + linearRing.getCoordinates()[i].getY();
			if (i<(linearRing.getCoordinates().length - 2)) {
				pt += ", ";
			}
			pstr.push(pt);
		}
		return d + pstr.join("")+ " Z";
	}
});