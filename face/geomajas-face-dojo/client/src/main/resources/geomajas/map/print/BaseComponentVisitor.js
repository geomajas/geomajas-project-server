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