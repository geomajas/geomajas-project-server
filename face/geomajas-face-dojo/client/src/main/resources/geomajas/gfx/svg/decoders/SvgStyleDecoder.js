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

dojo.provide("geomajas.gfx.svg.decoders.SvgStyleDecoder");
dojo.require("geomajas.gfx.ShapeStyle");
dojo.require("geomajas.gfx.FontStyle");
dojo.require("geomajas.gfx.PictureStyle");

dojo.declare("SvgStyleDecoder", null, {

	constructor : function () {
	},

	decode : function (style) {
		if (style instanceof ShapeStyle) {
			return this._decodeShapeStyle(style);
		} else if (style instanceof FontStyle) {
			return this._decodeFontStyle(style);
		} else if (style instanceof PictureStyle) {
			return this._decodePictureStyle(style);
		}
		return "";
	},

	_decodeShapeStyle : function (style) {
		var css = "";
		if (style.fillColor != null && style.fillColor != "") {
			css += "fill:"+style.fillColor+";";
		}
		if (style.fillOpacity != null && style.fillOpacity != "") {
			css += "fill-opacity:"+style.fillOpacity+";";
		}
		if (style.strokeColor != null && style.strokeColor != "") {
			css += "stroke:"+style.strokeColor+";";
		}
		if (style.strokeOpacity != null && style.strokeOpacity != "") {
			css += "stroke-opacity:"+style.strokeOpacity+";";
		}
		if (style.strokeWidth != null && style.strokeWidth != "") {
			css += "stroke-width:"+style.strokeWidth+";";
		}
		if (style.dashArray != null && style.dashArray != "") {
			css += "stroke-dasharray:"+style.dashArray+";";
		}
		return css;
	},

	_decodeFontStyle : function (style) {
		var css = "";
		if (style.fillColor != null && style.fillColor != "") {
			css += "fill:"+style.fillColor+";";
		}
		if (style.fontSize != null && style.fontSize != "") {
			css += "font-size:"+style.fontSize+";";
		}
		if (style.fontFamily != null && style.fontFamily != "") {
			css += "font-family:"+style.fontFamily+";";
		}
		if (style.fontWeight != null && style.fontWeight != "") {
			css += "font-weight:"+style.fontWeight+";";
		}
		if (style.fontStyle != null && style.fontStyle != "") {
			css += "font-style:"+style.fontStyle+";";
		}
		css += "fill-opacity: 1; stroke-opacity:0;";
		return css;
	},
	
	_decodePictureStyle : function (style) {
		return "opacity:" + style.getOpacity() + ";";
	}
});