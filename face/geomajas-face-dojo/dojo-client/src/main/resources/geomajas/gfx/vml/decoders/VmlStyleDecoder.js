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