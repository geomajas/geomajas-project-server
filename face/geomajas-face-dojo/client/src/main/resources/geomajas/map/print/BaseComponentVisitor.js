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

dojo.provide("geomajas.map.print.BaseComponentVisitor");
dojo.declare("BaseComponentVisitor", null, {

	/**
	 * @class 
	 * Mother of all print component visitors. Forwards to component-specific methods.
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
	},
	
	visit : function (comp) {
		if(comp instanceof PageComponent){
			this.visitPage(comp);
		} else if(comp instanceof MapComponent){
			this.visitMap(comp);
		} else if(comp instanceof LegendComponent){
			this.visitLegend(comp);
		} else if(comp instanceof LabelComponent){
			this.visitLabel(comp);
		} else if(comp instanceof BaseComponent){
			this.visitBase(comp);
		}
	},
	
	visitPage : function (page) {
	},
	
	visitMap : function (map) {
	},

	visitLegend : function (legend) {
	},
	
	visitLabel : function (label) {
	},
	
	visitBase : function (base) {
	}

});