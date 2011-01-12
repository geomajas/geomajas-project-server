dojo.provide("geomajas.map.print.DefaultConfigurationVisitor");
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
