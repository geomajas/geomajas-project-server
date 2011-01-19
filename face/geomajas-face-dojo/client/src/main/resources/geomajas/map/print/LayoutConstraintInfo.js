dojo.provide("geomajas.map.print.LayoutConstraintInfo");
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
geomajas.LayoutConstraints = { 
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
};

dojo.declare("LayoutConstraintInfo", null , {

	/**
	 * @class 
	 * A layout constraint info (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (alignmentX, alignmentY, flowDirection, width, height, marginX, marginY) {
		this.javaClass = "org.geomajas.plugin.printing.component.dto.LayoutConstraintInfo";
		
		this.alignmentX = (alignmentX ? alignmentX : geomajas.LayoutConstraints.LEFT);
		this.alignmentY = (alignmentY ? alignmentY : geomajas.LayoutConstraints.BOTTOM);
		this.flowDirection = (flowDirection ? flowDirection : geomajas.LayoutConstraints.FLOW_NONE);
		this.width = (width ? width : 0); // auto
		this.height = (height ? height : 0); // auto
		this.marginX = (marginX ? marginX : 10);
		this.marginY = (marginY ? marginY : 10);
	},
	
	setAlignmentX : function (alignmentX) {
		this.alignmentX = alignmentX;
	},
	
	setAlignmentY : function (alignmentY) {
		this.alignmentY = alignmentY;
	},
	
	setFlowDirection : function (flowDirection) {
		this.flowDirection = flowDirection;
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

	setWidth : function (width) {
		this.width = width;
	}

});
