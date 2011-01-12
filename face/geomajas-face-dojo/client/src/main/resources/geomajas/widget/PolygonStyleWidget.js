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

dojo.provide("geomajas.widget.PolygonStyleWidget");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit._Container");
dojo.require("dijit.ColorPalette");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.layout.ContentPane");
dojo.require("dijit.form.Slider");
dojo.require("dijit.form.NumberTextBox");

dojo.require("geomajas.gfx.ShapeStyle");
dojo.requireLocalization("geomajas.widget", "styleWidgets");

/**
 * @author Pieter De Graef
 */
dojo.declare(
	"geomajas.widget.PolygonStyleWidget",
	[dijit._Widget, dijit._Templated, dijit._Contained, dijit._Container],
	{
		widgetsInTemplate : true,
		templatePath : dojo.moduleUrl("geomajas.widget", "html/PolygonStyleWidget.html"),

		fillColorString : "Fill Color",
		fillOpacityString : "Fill Opacity",
		strokeColorString : "Stroke Color",
		strokeOpacityString : "Stroke Opacity",
		strokeWidthString : "Stroke Width",
		strokeDashArrayString : "Dash Array",

		alignment : "vertical",

		postMixInProperties : function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
			var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "styleWidgets");
			this.fillColorString = widgetLocale.fillColor;
			this.fillOpacityString = widgetLocale.fillOpacity;
			this.strokeColorString = widgetLocale.strokeColor;
			this.strokeOpacityString = widgetLocale.strokeOpacity;
			this.strokeWidthString = widgetLocale.strokeWidth;
			this.strokeDashArrayString = widgetLocale.strokeDashArray;

			if (this.alignment == "horizontal") {
				this.templatePath = dojo.moduleUrl("geomajas.widget", "html/PolygonStyleWidgetH.html");
			}
		},

		fillInTemplate : function () {
			this.inherited ("fillInTemplate", arguments);
			dojo.html.setClass(this.domNode, "styleMain");
		},

		setStyle : function (style) {
			var fcw = this._getFillColorWidget();
			if (fcw != null) {
				fcw.setValue (style.getFillColor());
			}
			var fow = this._getFillOpacityWidget();
			if (fow != null) {
				fow.setValue (style.getFillOpacity ());
			}
			var scw = this._getStrokeColorWidget();
			if (scw != null) {
				scw.setValue (style.getStrokeColor());
			}
			var sow = this._getStrokeOpacityWidget();
			if (sow != null) {
				sow.setValue (style.getStrokeOpacity());
			}
			var sww = this._getStrokeWidthWidget();
			if (sww != null) {
				sww.setValue (style.getStrokeWidth());
			}
			var daw = this._getDashArrayWidget();
			if (daw != null) {
				daw.setValue (style.getDashArray());
			}
		},

		getStyle : function () {
			var style = new ShapeStyle (null,null,null,null,null,null,null);
			var fcw = this._getFillColorWidget();
			if (fcw != null && fcw.getValue() != "" && fcw.getValue() != null) {
				style.setFillColor (fcw.getValue());
			}
			var fow = this._getFillOpacityWidget();
			if (fow != null && fow.getValue() != "" && fow.getValue() != null) {
				style.setFillOpacity ("" + fow.getValue());
			}
			var scw = this._getStrokeColorWidget();
			if (scw != null && scw.getValue() != "" && scw.getValue() != null) {
				style.setStrokeColor (scw.getValue());
			}
			var sow = this._getStrokeOpacityWidget();
			if (sow != null && sow.getValue() != "" && sow.getValue() != null) {
				style.setStrokeOpacity ("" + sow.getValue());
			}
			var sww = this._getStrokeWidthWidget();
			if (sww != null && sww.getValue() != "" && sww.getValue() != null) {
				style.setStrokeWidth ("" + sww.getValue());
			}
			var daw = this._getDashArrayWidget();
			if (daw != null && daw.getValue() != "" && daw.getValue() != null) {
				style.setDashArray ("" + daw.getValue());
			}
			return style;
		},

		getValue : function () {
			return this.getStyle();
		},

		setValue : function (value) {
			this.setStyle (value);
		},

		setDisabled : function (disabled) {
			var fcw = this._getFillColorWidget();
			if (fcw != null) {
				fcw.setDisabled(disabled);
			}
			var fow = this._getFillOpacityWidget();
			if (fow != null) {
				fow.setDisabled(disabled);
			}
			var scw = this._getStrokeColorWidget();
			if (scw != null) {
				scw.setDisabled(disabled);
			}
			var sow = this._getStrokeOpacityWidget();
			if (sow != null) {
				sow.setDisabled(disabled);
			}
			var sww = this._getStrokeWidthWidget();
			if (sww != null) {
				sww.setDisabled(disabled);
			}
			var daw = this._getDashArrayWidget();
			if (daw != null) {
				daw.setDisabled(disabled);
			}
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
		_getFillOpacityWidget : function () {
			return dijit.byId (this.id+":fillOpacity");
		},

		/**
		 * @private
		 */
		_getStrokeColorWidget : function () {
			return dijit.byId (this.id+":strokeColor");
		},

		/**
		 * @private
		 */
		_getStrokeOpacityWidget : function () {
			return dijit.byId (this.id+":strokeOpacity");
		},

		/**
		 * @private
		 */
		_getStrokeWidthWidget : function () {
			return dijit.byId (this.id+":strokeWidth");
		},

		/**
		 * @private
		 */
		_getDashArrayWidget : function () {
			return dijit.byId (this.id+":dashArray");
		}
	}
);