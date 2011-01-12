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

dojo.provide("geomajas.gfx.vml.decoders.VmlStyleDecoder");
dojo.require("geomajas.gfx.ShapeStyle");
dojo.require("geomajas.gfx.FontStyle");
dojo.require("geomajas.gfx.PictureStyle");

dojo.declare("VmlStyleDecoder", null, {

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
		if (style.symbol != null && style.symbol != "") {
			return style.symbol; // exception...
		}
		var css = "";
		if (style.fillColor != null && style.fillColor != "") {
			css += "fill:"+style.fillColor+";";
		}
		if (style.fillOpacity != null && style.fillOpacity != "") {
			var value = parseFloat(style.fillOpacity);
			if (value >= 0 && value <= 1) {
				css += "fill-opacity:"+value+";";
			}
		}
		if (style.strokeColor != null && style.strokeColor != "") {
			css += "stroke:"+style.strokeColor+";";
		}
		if (style.strokeOpacity != null && style.strokeOpacity != "") {
			var value = parseFloat(style.strokeOpacity);
			if (value >= 0 && value <= 1) {
				css += "stroke-opacity:"+value+";";
			}
		}
		if (style.strokeWidth != null && style.strokeWidth != "") {
			var value = parseFloat(style.strokeWidth);
			if (value >= 0) {
				css += "stroke-width:"+value+";";
			}
		}
		if (style.dashArray != null && style.dashArray != "") {
			css += "stroke-dasharray:"+style.dashArray+";";
		}
		return css;
	},

	_decodeFontStyle : function (style) {
		var css = "";
/*		if (style.fillColor != null && style.fillColor != "") {
			css += "fill:"+style.fillColor+";";
		}*/
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
		return css;
	},
	
	_decodePictureStyle : function (style) {
		var value = parseFloat(style.getOpacity());
		if (value >= 0 && value <= 1) {
			css += "opacity:"+value+";";
		}
		return "opacity:1;";
	}
});