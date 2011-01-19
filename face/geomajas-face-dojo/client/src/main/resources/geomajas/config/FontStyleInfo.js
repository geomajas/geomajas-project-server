dojo.provide("geomajas.config.FontStyleInfo");
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
dojo.declare("FontStyleInfo", null, {

	/**
	 * @class Configuration object that defines styles for a layer object.
	 * @author Kristof Heirwegh
	 *
	 * @constructor
	 * @param id Unique identifier.
	 */
	constructor : function (size, family, weight, style, color, opacity) {
		/** @private */
		this.javaClass = "org.geomajas.configuration.FontStyleInfo";
		
		this.size = size;
		this.family = family;
		this.weight = weight;
		this.style = style;
		this.color = color;
		this.opacity = opacity;
		if (!this.size) this.size = -1;
		if (!this.opacity) this.opacity = -1; 
	},
	
	// Serialization methods:
	
	toJSON : function () {
		return this;
	},
	
	fromJSON : function (json) {
		this.size = json.size;
		this.family = json.family;
		this.weight = json.weight;
		this.style = json.style;
		this.color = json.color;
		this.opacity = json.opacity;
	},
	
	getSize : function () {
		return this.size;
	},
	
	setSize : function (size) {
		this.size = size;
	},
	
	getFamily : function () {
		return this.family;
	},
	
	setFamily : function (family) {
		this.family = family;
	},
	
	getWeight : function () {
		return this.weight;
	},
	
	setWeight: function (weight) {
		this.weight = weight;
	},
	
	getColor : function () {
		return this.color;
	},
	
	setColor : function (color) {
		this.color = color;
	},
	
	getOpacity : function () {
		return this.opacity;
	},
	
	setOpacity : function (opacity) {
		this.opacity = opacity;
	},
	
	getStyle : function () {
		return this.style;
	},
	
	setStyle : function (style) {
		this.style = style;
	}
});
