dojo.provide("geomajas.widget.FontStyleWidget");
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
