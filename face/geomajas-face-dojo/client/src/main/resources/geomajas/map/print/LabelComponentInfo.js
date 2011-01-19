dojo.provide("geomajas.map.print.LabelComponentInfo");
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
dojo.declare("LabelComponentInfo", PrintComponentInfo, {

	/**
	 * @class 
	 * A map component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
//		private FontStyleInfo font;
//		private String text;
//		private String fontColor;
//		private String backgroundColor;
//		private String borderColor;
//		private boolean textOnly;
//		private float lineWidth;
//		private float margin;

		this.javaClass = "org.geomajas.plugin.printing.component.dto.LabelComponentInfo";
	},
	
	setText : function (text) {
		this.text = text;
	},

	setFont : function (font) {
		this.font = font;
	},

	setFontColor : function (fontColor) {
		this.fontColor = fontColor;
	},

	setBackgroundColor : function (backgroundColor) {
		this.backgroundColor = backgroundColor;
	},

	setBorderColor : function (borderColor) {
		this.borderColor = borderColor;
	},

	setTextOnly : function (textOnly) {
		this.textOnly = textOnly;
	},

	setLineWidth : function (lineWidth) {
		this.lineWidth = lineWidth;
	},

	setMargin : function (margin) {
		this.margin = margin;
	}

});
