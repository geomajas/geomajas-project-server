dojo.provide("geomajas.config.FeatureStyleInfo");
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
dojo.declare("FeatureStyleInfo", null, {

	/**
	 * @class Configuration object that defines styles for a layer object.
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @param id Unique identifier.
	 * @param label The style's label.
	 * @param style A Style object.
	 */
	constructor : function (index, name, formula, style) {
		/** @private */
		this.javaClass = "org.geomajas.configuration.FeatureStyleInfo";
		
		/** Index in the list. */
		this.index = index;
		
		/** The style's label. */
		this.name = name;
		
		/** The style's label. */
		this.formula = formula;

		/** A Style object. */
		this.style = style;
		
		this.styleId = null;
	},
	
	// Serialization methods:
	
	toJSON : function () {
		var json = {
			javaClass : this.javaClass,
			index : this.index,
			name : this.name,
			formula : this.formula,
			fillColor : this.style.fillColor,
			fillOpacity : this.style.fillOpacity,
			strokeColor : this.style.strokeColor,
			strokeOpacity : this.style.strokeOpacity,
			strokeWidth : this.style.strokeWidth,
			dashArray : this.style.dashArray,
			symbol : this.style.symbol,
			styleId : this.styleId
		};
		return json;
	},
	
	fromJSON : function (json) {
		this.index = json.index;
		this.name = json.name;
		this.formula = json.formula;
		this.styleId = json.styleId;
		this.style = new ShapeStyle();
		// only non-null elements !
		this.style.copyFrom(json);
	},
	
	// Getters and setters:
	
	getIndex : function () {
		return this.index;
	},
	
	setIndex : function (index) {
		this.index = index;
	},

	getName : function () {
		return this.name;
	},
	
	setName : function (name) {
		this.name = name;
	},

	getFormula : function () {
		return this.formula;
	},
	
	setFormula : function (formula) {
		this.formula = formula;
	},

	getStyleId : function () {
		return this.styleId;
	},
	
	setStyleId : function (styleId) {
		this.styleId = styleId;
	},

	getStyle : function () {
		return this.style;
	},
	
	setStyle : function (style) {
		this.style = style;
	}
});
