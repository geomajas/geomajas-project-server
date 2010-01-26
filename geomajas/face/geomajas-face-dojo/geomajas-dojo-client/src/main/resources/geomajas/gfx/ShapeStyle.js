dojo.provide("geomajas.gfx.ShapeStyle");
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
