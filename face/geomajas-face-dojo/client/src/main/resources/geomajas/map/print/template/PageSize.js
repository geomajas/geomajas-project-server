dojo.provide("geomajas.map.print.template.PageSize");
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
dojo.declare("PageSize", null, {
	
	METERS_PER_INCH : 0.0254,
	
	/**
	 * @class 
	 * Page size for printing, including name and metric values.
	 * 
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (w,h,name) {
		this.width = w;
		this.height = h;
		this.name = name;
		this.metricWidth = w / 72.0 * this.METERS_PER_INCH;
		this.metricHeight = h / 72.0 * this.METERS_PER_INCH;
	},
	
	getWidth : function () {
		return this.width;
	},
	
	getHeight : function () {
		return this.height;
	},
	
	getName : function () {
		return this.name;
	},
	
	getMetricWidth : function () {
		return this.metricWidth;
	},
	
	getMetricHeight : function () {
		return this.metricHeight;
	}

});

geomajas.PageSizes = {
	"A0" : new PageSize(2384, 3370, "A0"), 	/* ISO A0 format. */
	"A1" : new PageSize(1684, 2384, "A1"), /* ISO A1 format. */
	"A2" : new PageSize(1191, 1684, "A2"), /* ISO A2 format. */
	"A3" : new PageSize(842, 1191, "A3"), /* ISO A3 format. */
	"A4" : new PageSize(595, 842, "A4") /* ISO A4 format. */
};
