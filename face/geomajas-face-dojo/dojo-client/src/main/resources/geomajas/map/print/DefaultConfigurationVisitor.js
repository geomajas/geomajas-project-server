dojo.provide("geomajas.map.print.DefaultConfigurationVisitor");
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
dojo.declare("DefaultConfigurationVisitor", BaseComponentVisitor, {

	/**
	 * @class 
	 * Visits the print page and applies default properties (title, date, arrow,...). 
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (params) {
		/**
		 * The DPI of the raster images 
		 */
		this.rasterDPI = params.rasterDPI || 500;
				
		/**
		 * Title (null = no title)
		 */
		this.title = params.title ? params.title : "";
		
		/**
		 * show date ?
		 */
		this.withDate = params.withDate ? true : false;
		
		/**
		 * show north arrow ?
		 */
		this.withArrow = params.withArrow ? true : false;
		
		/**
		 * extract
		 */
		this.extract = params.extract ? true : false;
	},
	
	visitPage : function (page) {
		if(this.extract){
			// nothing
		} else if (this.title == "" && this.withDate == false) {
			page.removeChildByTag("title");
		}
	},
	
	visitMap : function (map) {
		if(this.extract){
		if(map.getTag() == "map") {
				this.rasterDPI = map.rasterResolution;
			}
		} else if(map.getTag() == "map") {
			if (!this.withArrow) {
				map.removeChildByTag("arrow");
			}
			map.rasterResolution = this.rasterDPI;
		}
	},

	visitLabel : function (label) {
		if(this.extract) {
		if(label.getTag() == "title"){
				this.title = label.text;
				if(this.title.indexOf(" (") >= 0 && this.title.charAt(this.title.length-1) == ')'){
					this.title = this.title.substring(0,this.title.indexOf(" ("));
					this.withDate = true;
				}
			}
		}
		else if(label.getTag() == "title"){
			var text = this.title;
			if (this.withDate) {
				if (text == "") {
					text =  dojo.date.locale.format(new Date(), {selector:'date'});
				} else {
					text += " (" + dojo.date.locale.format(new Date(), {selector:'date'}) + ")";
				}
			}
			label.setText(text);
		}
	},
	
	visitBase : function (base) {
		if(this.extract) {
			if(base.getTag() == "arrow"){
				this.withArrow = true;
			}
		}
	}

});
