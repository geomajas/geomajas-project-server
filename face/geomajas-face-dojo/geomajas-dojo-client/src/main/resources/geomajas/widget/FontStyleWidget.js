dojo.provide("geomajas.widget.FontStyleWidget");
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
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit._Container");
dojo.require("dijit.ColorPalette");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.form.NumberTextBox");
dojo.require("dijit.form.ComboBox");

dojo.require("geomajas.gfx.FontStyle");
dojo.requireLocalization("geomajas.widget", "styleWidgets");

/**
 * @author Pieter De Graef
 */
dojo.declare(
	"geomajas.widget.FontStyleWidget",
	[dijit._Widget, dijit._Templated, dijit._Container],
	{
		widgetsInTemplate : true,
		templatePath : dojo.moduleUrl("geomajas.widget", "html/FontStyleWidget.html"),

		fillColorString : "Fill Color",
		fontSizeString : "Font Size",
		fontFamilyString : "Font Family",
		fontWeightString : "Font Weight",
		fontStyleString : "Font Style",
		normalString : "normal",
		boldString : "bold",
		italicString : "italic",
		obliqueString : "oblique",
		
		alignment : "vertical",

		postMixInProperties : function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
			var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "styleWidgets");
			this.fillColorString = widgetLocale.fillColor;
			this.fontSizeString = widgetLocale.fontSize;
			this.fontFamilyString = widgetLocale.fontFamily;
			this.fontWeightString = widgetLocale.fontWeight;
			this.fontStyleString = widgetLocale.fontStyle;
			this.normalString = widgetLocale.normal;
			this.boldString = widgetLocale.bold;
			this.italicString = widgetLocale.italic;
			this.obliqueString = widgetLocale.oblique;

			if (this.alignment == "horizontal") {
				this.templatePath = dojo.moduleUrl("geomajas.widget", "html/FontStyleWidgetH.html");
			}
		},

		setStyle : function (style) {
			var fcw = this._getFillColorWidget();
			if (fcw != null) {
				fcw.setValue (style.getFillColor());
			}
			var fsw = this._getFontSizeWidget();
			if (fsw != null) {
				fsw.setValue (style.getFontSize ());
			}
			var ffw = this._getFontFamilyWidget();
			if (ffw != null) {
				ffw.setValue (style.getFontFamily());
			}
			var fww = this._getFontWeightWidget();
			if (fww != null) {
				fww.setValue (style.getFontWeight());
			}
			var fstw = this._getFontStyleWidget();
			if (fstw != null) {
				fstw.setValue (style.getFontStyle());
			}
		},

		getStyle : function () {
			var style = new FontStyle ();
			var fcw = this._getFillColorWidget();
			if (fcw != null) {
				style.setFillColor (fcw.getValue());
			}
			var fsw = this._getFontSizeWidget();
			if (fsw != null) {
				style.setFontSize(fsw.getValue());
			}
			var ffw = this._getFontFamilyWidget();
			if (ffw != null) {
				style.setFontFamily(ffw.getValue());
			}
			var fww = this._getFontWeightWidget();
			if (fww != null) {
				style.setFontWeight(fww.getValue());
			}
			var fstw = this._getFontStyleWidget();
			if (fstw != null) {
				style.setFontStyle(fstw.getValue());
			}
			return style;
		},

		getValue : function () {
			return this.getStyle();
		},

		setValue : function (value) {
			this.setStyle (value);
		},

		/**
		 * @private
		 */
		_getFillColorWidget : function () {
			return dijit.byId (this.id+":fillColor");
		},

		/**
		 * @private
		 */
		_getFontSizeWidget : function () {
			return dijit.byId (this.id+":fontSize");
		},

		/**
		 * @private
		 */
		_getFontFamilyWidget : function () {
			return dijit.byId (this.id+":fontFamily");
		},

		/**
		 * @private
		 */
		_getFontWeightWidget : function () {
			return dijit.byId (this.id+":fontWeight");
		},

		/**
		 * @private
		 */
		_getFontStyleWidget : function () {
			return dijit.byId (this.id+":fontStyle");
		}
	}
);
