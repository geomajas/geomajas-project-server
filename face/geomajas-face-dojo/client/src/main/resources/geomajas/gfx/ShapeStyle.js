dojo.provide("geomajas.gfx.ShapeStyle");
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
dojo.declare("ShapeStyle", null, {
	
	// defaults
	fillColor : "#FFFFFF",
	fillOpacity : "1",
	strokeColor : "#000000",
	strokeOpacity : "1",
	strokeWidth : "1",
	dashArray : null,
	symbol : null,	

	/**
	 * @fileoverview Simple style object for CSS.
	 * @class Simple style object for CSS.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function (fillColor, fillOpacity, strokeColor, strokeOpacity, strokeWidth, dashArray, symbol) {
		if(fillColor != null){
			this.fillColor = fillColor;
		}
		if(fillOpacity != null){
			this.fillOpacity = fillOpacity;
		}
		if(strokeColor != null){
			this.strokeColor = strokeColor;
		}
		if(strokeOpacity != null){
			this.strokeOpacity = strokeOpacity;
		}
		if(strokeWidth != null){
			this.strokeWidth = strokeWidth;
		}
		if(dashArray != null){
			this.dashArray = dashArray;
		}
		if(symbol != null){
			this.symbol = symbol;
		}
	},

	set : function (fillColor, fillOpacity, strokeColor, strokeOpacity, strokeWidth, dashArray, symbol) {
		this.fillColor = fillColor;
		this.fillOpacity = fillOpacity;
		this.strokeColor = strokeColor;
		this.strokeOpacity = strokeOpacity;
		this.strokeWidth = strokeWidth;
		this.dashArray = dashArray;
		this.symbol = symbol;
	},

	clone : function () {
		return new ShapeStyle(this.fillColor, this.fillOpacity, this.strokeColor, this.strokeOpacity, this.strokeWidth, this.dashArray, this.symbol);
	},
	
	// copy non-null elements only !
	copyFrom : function (other) {
		if(other.fillColor != null){
			this.fillColor = other.fillColor;
		}
		if(other.fillOpacity != null){
			this.fillOpacity = other.fillOpacity;
		}
		if(other.strokeColor != null){
			this.strokeColor = other.strokeColor;
		}
		if(other.strokeOpacity != null){
			this.strokeOpacity = other.strokeOpacity;
		}
		if(other.strokeWidth != null){
			this.strokeWidth = other.strokeWidth;
		}
		if(other.dashArray != null){
			this.dashArray = other.dashArray;
		}
		if(other.symbol != null){
			this.symbol = other.symbol;
		}
	},
	
	getStrokeWidthAsFloat : function () {
		return parseFloat(this.strokeWidth);
	},

	// Getters and setters:

	getFillColor : function () {
		return this.fillColor;
	},

	setFillColor : function (fillColor) {
		this.fillColor = fillColor;
	},

	getFillOpacity : function () {
		return this.fillOpacity;
	},

	setFillOpacity : function (fillOpacity) {
		this.fillOpacity = fillOpacity;
	},

	getStrokeColor : function () {
		return this.strokeColor;
	},

	setStrokeColor : function (strokeColor) {
		this.strokeColor = strokeColor;
	},

	getStrokeOpacity : function () {
		return this.strokeOpacity;
	},

	setStrokeOpacity : function (strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	},

	getStrokeWidth : function () {
		return this.strokeWidth;
	},

	setStrokeWidth : function (strokeWidth) {
		this.strokeWidth = strokeWidth;
	},

	getDashArray : function () {
		return this.dashArray;
	},

	setDashArray : function (dashArray) {
		this.dashArray = dashArray;
	},

	getSymbol : function () {
		return this.symbol;
	},

	setSymbol : function (symbol) {
		this.symbol = symbol;
	}
});
