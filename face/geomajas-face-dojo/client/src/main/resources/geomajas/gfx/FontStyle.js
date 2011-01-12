dojo.provide("geomajas.gfx.FontStyle");
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
