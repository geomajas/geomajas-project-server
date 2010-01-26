dojo.provide("geomajas.map.print.PrintTransformation");
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
dojo.declare("PrintTransformation", null, {

	/**
	 * @class 
	 * Transforms between print (origin = lower-left of page) and view coordinates
	 * (origin = upper-left of page, negative y-axis).
	 * Can also apply the current bounds to a print. In this case the print is scaled to fit within
	 * the current bounds. A margin of 10 pixels is applied to make the print bounds more visible.
	 * 
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/* map view */ mapView, /* map id */ mapId, /* template */ template, /* boolean */ locationIsCurrent) {
		this.mapView = mapView;
		this.mapId = mapId;
		this.template = template;
		this.map = this.template.getMapComponent(this.mapId);
		this.zoomScale = 1.0;  /* ratio of print to screen units */
		this.printToWorldTransform = null;
		this.printToViewTransform = null;
		this.fixPrintToViewHandle = null;
		this.fixPrintToWorldHandle = null;
		this.factory = new GeometryFactory(mapView.getSRID(), mapView.getPrecision() + 4);
		this._calcTransform(locationIsCurrent);
	},
	
	navigateToPage : function () {
		// find the page bounds in print coordinates (10 points buffer)
		var pagePrint = this.template.getPage().getBounds().buffer(10);
		// transform to world coordinates
		var pageWorld = this._transformBounds(pagePrint, this.printToWorldTransform);
		// set print component location, only needed by view ports !
		this.template.getPage().executeTopDown(dojo.hitch(this,"_setPrintLocation"));
		// apply bounds
		this.mapView.applyBbox(pageWorld, true);
	},
	
	applyToPage : function () {
		this.template.getPage().executeTopDown(dojo.hitch(this,"_setMapLocation"));
	},
	
	_setMapLocation : function (map) {
		// calculate scale
		scale = 1.0/this.printToWorldTransform.xx;
		if(map.declaredClass == "MapComponent") {
			map.setMapId(this.mapId);
			map.setPpUnit(scale);
			var mapLocationPrint = map.getBounds().getOrigin();
		var mapLocation = this._transformPoint(mapLocationPrint,this.printToWorldTransform);
			map.setLocation(mapLocation);
		} else if(map.declaredClass == "ViewPortComponent"){
			map.setPpUnit(map.zoomScale * scale);
			var mapLocationPrint = map.getPortBounds().getOrigin();
			var mapLocation = this._transformPoint(mapLocationPrint,this.printToWorldTransform);
			map.setLocation(mapLocation);
		}
	},
	
	_setPrintLocation : function (comp) {
		if(comp.declaredClass == "ViewPortComponent"){
			comp.resetPortBounds();
		}
	},

	transformBoundsPrintToView : function (bounds) {
		var printToViewTransform = null;
		if(this.printToViewTransform != null){
			printToViewTransform = this.printToViewTransform;
		} else {
		var worldToViewTransform = this.mapView.getWorldToViewTransformation();
			printToViewTransform = this._concatenate(this.printToWorldTransform, worldToViewTransform);
		}
		return this._transformBounds(bounds, printToViewTransform);
	},
	
	transformCoordinatePrintToWorld : function (coordinate) {
		return this._transformPoint(coordinate, this.printToWorldTransform);
	},

	transformCoordinateWorldToPrint : function (coordinate) {
		return this._transformPoint(coordinate, this._invert(this.printToWorldTransform));
	},

	transformCoordinateViewToPrint : function (coordinate) {
		var viewToPrintTransform = null;
		if(this.printToViewTransform != null){
			viewToPrintTransform = this._invert(this.printToViewTransform);
		} else {
			var worldToViewTransform = this.mapView.getWorldToViewTransformation();
			viewToPrintTransform = this._invert(this._concatenate(this.printToWorldTransform, worldToViewTransform));
		}
		return this._transformPoint(coordinate, viewToPrintTransform);
	},
	
	fixPrintToView : function () {
		if(this.fixPrintToWorldHandle != null){
			dojo.disconnect(this.fixPrintToWorldHandle);
			this.fixPrintToWorldHandle = null;
		}
		if(this.fixPrintToViewHandle == null){
			this.fixPrintToViewHandle = dojo.connect(this.mapView,"onChange", this, "_fixPrintToView");
		}
	},
	
	fixPrintToWorld : function () {
		if(this.fixPrintToViewHandle != null){
			dojo.disconnect(this.fixPrintToViewHandle);
			this.fixPrintToViewHandle = null;
		}
		if(this.fixPrintToWorldHandle == null){
			this.fixPrintToWorldHandle = dojo.connect(this.mapView,"onChange", this, "_fixPrintToWorld");
		}
	},
	
	destroy : function () {
		if(this.fixPrintToViewHandle != null){
			dojo.disconnect(this.fixPrintToViewHandle);
		}
		if(this.fixPrintToWorldHandle != null){
			dojo.disconnect(this.fixPrintToWorldHandle);
		}
	},
		
	_fixPrintToView : function () {
		log.info("_fixPrintToView");
		// print to view transformation is kept fixed whenever world-to-view changes !
		var worldToViewTransform = this.mapView.getWorldToViewTransformation();
		this.printToWorldTransform = this._concatenate(this.printToViewTransform, this._invert(worldToViewTransform));
		// may need to update loaction of map !
		this.applyToPage();
	},
		
	_fixPrintToWorld : function () {
		log.info("_fixPrintToWorld");
		// print to world transformation is kept fixed whenever world-to-view changes !
		var worldToViewTransform = this.mapView.getWorldToViewTransformation();
		this.printToViewTransform = this._concatenate(this.printToWorldTransform, worldToViewTransform);
	},
		
	_calcTransform : function (/* boolean */ locationIsCurrent) {
		if(locationIsCurrent){
			// find the page bounds in print coordinates (10 points buffer)
			var pagePrint = this.template.getPage().getBounds().buffer(10);
			// this should transform to current bbox, check for the scale
			var width = this.mapView.getMapWidth();
			var height = this.mapView.getMapHeight();
			// check if width or height should map ?
			var wRatio = width/pagePrint.getWidth();
			var hRatio = height/pagePrint.getHeight();
			var scale = wRatio > hRatio ? hRatio : wRatio;
			if(scale == wRatio){
				log.info("print fits width");
			} else {
				log.info("print fits height");
			}
			// transform should map print center to view center !
			var dx = 0.5 * width - scale * pagePrint.getCenterPoint().getX();
			var dy = 0.5 * height + scale * pagePrint.getCenterPoint().getY();
			this.printToViewTransform = {xx: scale, xy: 0, yx: 0, yy: -scale, dx: dx, dy: dy};
			var worldToViewTransform = this.mapView.getWorldToViewTransformation();
			this.printToWorldTransform = this._concatenate(this.printToViewTransform, this._invert(worldToViewTransform));
		} else {
			// world to print transformation
			var scale = this.map.getPpUnit();
			var location = this.map.getLocation();
			var printBounds = this.map.getBounds();
			var dX = -(location.getX() * scale) + printBounds.getOrigin().getX();
			var dY = -(location.getY() *scale) + printBounds.getOrigin().getY();
			var worldToPrintTransform = {xx: scale, xy: 0, yx: 0, yy: scale, dx: dX, dy: dY};
			this.printToWorldTransform = this._invert(worldToPrintTransform); 
		}
	},
	
	
	/**
	 * transforms a bbox with a matrix
	 */
	_transformBounds : function (bbox, matrix) {
		var origin = this._transformPoint(bbox.getOrigin(),matrix);
		var end = this._transformPoint(bbox.getEndPoint(),matrix);
		var lineString = this.factory.createLineString([origin, end]);
		return lineString.getBounds();
	},
	
	/**
	 * transforms a point with a matrix
	 */
	_transformPoint: function(point, matrix){
		return new Coordinate(
		    matrix.xx * point.getX() + matrix.xy * point.getY() + matrix.dx, 
		    matrix.yx * point.getX() + matrix.yy * point.getY() + matrix.dy
		); 
	},

	/**
	 * concatenates 2 transformation matrices 
	 */
	_concatenate : function (m1, m2) {
		return {
			xx: m2.xx * m1.xx, 
			xy: 0,
			yx: 0,
			yy: m2.yy * m1.yy,
			dx: m2.xx * m1.dx + m2.dx,
			dy: m2.yy * m1.dy + m2.dy
		};
	},
	
	/**
	 * Inverts a transformation matrix (scale + transform only !!!!)
	 */
	_invert : function (m) {
		return {
			xx: 1/m.xx, 
			xy: 0, 
			yx: 0, 
			yy: 1/m.yy, 
			dx: -m.dx/m.xx, 
			dy: -m.dy/m.yy
		};
	}



	
});
