dojo.provide("geomajas.map.print.LayoutConstraint");
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
