dojo.provide("geomajas.gfx.FontStyle");
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
dojo.declare("FontStyle", null, {

	/**
	 * @fileoverview Style object for graphics fonts.
	 * @class Style object for graphics fonts.
	 * of ShapeStyle, because it needs a fill, fill-opacity, stroke,
	 * stroke-opacity,....
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param fillColor HTML color code determining font color.
	 * @param fontSize Integer value determining font size.
	 * @param fontFamily String determining the font.
	 * @param fontWeight Weight: normal, bold.
	 * @param fontStyle The font style: normal, italic, underline.
	 */
	constructor : function (fillColor, fontSize, fontFamily, fontWeight, fontStyle) {
		this.fillColor = fillColor || "#000000";
		this.fontSize = fontSize || 12;
		this.fontFamily = fontFamily || "Arial,Verdana";
		this.fontWeight = fontWeight || "normal";
		this.fontStyle = fontStyle || "normal";
	},

	/**
	 * Set all new values for this object.
	 * @param fillColor HTML color code determining font color.
	 * @param fontSize Integer value determining font size.
	 * @param fontFamily String determining the font.
	 * @param fontWeight Weight: normal, bold.
	 * @param fontStyle The font style: normal, italic, underline.
	 */
	set : function (fillColor, fontSize, fontFamily, fontWeight, fontStyle) {
		this.fillColor = fillColor;
		this.fontSize = fontSize;
		this.fontFamily = fontFamily;
		this.fontWeight = fontWeight;
		this.fontStyle = fontStyle;
	},

	clone : function () {
		return new FontStyle(this.fillColor, this.fontSize, this.fontFamily, this.fontWeight, this.fontStyle);
	},

	// Getters and setters:

	getFillColor : function () {
		return this.fillColor;
	},

	setFillColor : function (fillColor) {
		this.fillColor = fillColor;
	},

	getFontSize : function () {
		return this.fontSize;
	},

	setFontSize : function (fontSize) {
		this.fontSize = fontSize;
	},

	getFontFamily : function () {
		return this.fontFamily;
	},

	setFontFamily : function (fontFamily) {
		this.fontFamily = fontFamily;
	},

	getFontWeight : function () {
		return this.fontWeight;
	},

	setFontWeight : function (fontWeight) {
		this.fontWeight = fontWeight;
	},

	getFontStyle : function () {
		return this.fontStyle;
	},

	setFontStyle : function (fontStyle) {
		this.fontStyle = fontStyle;
	}
});
