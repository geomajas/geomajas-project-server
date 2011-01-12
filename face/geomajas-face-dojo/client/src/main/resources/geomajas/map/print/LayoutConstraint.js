dojo.provide("geomajas.map.print.LayoutConstraint");
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
dojo.declare("LayoutConstraint", null , {

	statics : {
	 LEFT : 0,
	 BOTTOM : 1,
	 CENTER : 2,
	 RIGHT : 3,
	 TOP : 4,
	 JUSTIFIED : 5,
	 ABSOLUTE : 6,
	 FLOW_X : 0,
	 FLOW_Y : 1,
	 FLOW_NONE : 2
	},
	
	/**
	 * @class 
	 * A layout constraint (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
		this.alignmentX = this.statics.LEFT;
		this.alignmentY = this.statics.BOTTOM;
		this.flowDirection = this.statics.FLOW_NONE;
		this.width = 100;
		this.height = 100;
		this.marginX = 10;
		this.marginY = 10;
	},
	
	setAlignmentX : function (alignmentX) {
		this.alignmentX = alignmentX;
	},
	
	setAlignmentY : function (alignmentY) {
		this.alignmentY = alignmentY;
	},
	
	setWidth : function (width) {
		this.width = width;
	},
	
	setHeight : function (height) {
		this.height = height;
	},
	
	setMarginX : function (marginX) {
		this.marginX = marginX;
	},
	
	setMarginY : function (marginY) {
		this.marginY = marginY;
	},
	
	setFlowDirection : function (flowDirection) {
		this.flowDirection = flowDirection;
	}
	
});
