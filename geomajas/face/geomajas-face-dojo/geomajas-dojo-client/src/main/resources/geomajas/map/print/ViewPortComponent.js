dojo.provide("geomajas.map.print.ViewPortComponent");
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
dojo.declare("ViewPortComponent", MapComponent, {

	/**
	 * @class A viewport component (mirror of server object)
	 * @author Jan De Moerloose
	 * 
	 * @constructor
	 */
	constructor : function(/* json object */json, /* unique string */id) {
		if(!this.zoomScale){
			this.zoomScale = 1;
		}
		if(!this.userX){
			this.userX = 10;
		}
		if(!this.userY){
			this.userY = 10;
		}
		if(!this.tag){
			this.tag = "viewport";
		}
		if(!this.constraint){
			this.constraint = new LayoutConstraint();
		}
		if(!this.bounds){
			this.bounds = new Bbox(0,0,100,100);
		}
		if(!this.portBounds){
			this.portBounds = new Bbox(0,0,100,100);
		}
		if(!this.location){
			this.location = new Coordinate(0, 0);
		}
		this.javaClass = "org.geomajas.extension.printing.component.ViewPortComponent";
		// avoid json serialization of port bounds !
		dojo.mixin(this.portBounds, {json : function() { return this;}});
	},
	
	/**
	 * set the view port bounds
	 */
	setBounds : function(bbox) {
		this._setViewBounds(bbox);
		// adjust the zoom scale to keep the same port bounds
		this.zoomScale = bbox.getWidth()/this.portBounds.getWidth();
		// the port should remain unchanged, reset just in case...
		this._setPortBounds(this.portBounds.cloneWithSize(bbox.getWidth()/this.zoomScale,bbox.getHeight()/this.zoomScale));
	},
		
	/**
	 * set the bounds of the port location on the map
	 */
	setPortBounds : function(bbox) {
		this._setPortBounds(bbox);
		// we keep the width or height, whichever is largest
		if(bbox.getWidth() > bbox.getHeight()) {
			this.zoomScale = this.bounds.getWidth()/this.portBounds.getWidth();
		} else {
			this.zoomScale = this.bounds.getHeight()/this.portBounds.getHeight();
		}
		this._setViewBounds(this.bounds.cloneWithSize(bbox.getWidth()*this.zoomScale,bbox.getHeight()*this.zoomScale));
	},
	
	/**
	 * set the bounds of the port location on the map based on location, zoomScale and current transform
	 */
	resetPortBounds : function() {
		var width = this.getBounds().getWidth()/this.zoomScale;
		var height = this.getBounds().getHeight()/this.zoomScale;
		var origin = geomajasConfig.printManager.getTransform().transformCoordinateWorldToPrint(this.getLocation());
		this.portBounds = new Bbox(origin.getX(), origin.getY() ,width, height);
	},
	
		
	_setViewBounds : function(bbox) {
		MapComponent.superclass.setBounds.apply(this, [ bbox ]);
		this.userX = bbox.getX();
		this.userY = bbox.getY();
	},
	
	_setPortBounds : function(bbox) {
		dojo.mixin(this.portBounds,bbox);
		this.location = geomajasConfig.printManager.getTransform().transformCoordinatePrintToWorld(this.portBounds.getOrigin());
	},

	/**
	 * returns the bounds of the port location on the map
	 */
	getPortBounds : function() {
		return this.portBounds;
	},
	
	/**
	 * returns the view bounds
	 */
	getPortViewBounds : function () {
		return geomajasConfig.printManager.getTransform().transformBoundsPrintToView(this.portBounds);
	},

	/**
	 * returns the edit bounds
	 */
	getEditingViewBounds : function () {
		if(this.isEditingPort()){
			return this.getPortViewBounds();
		} else {
			return this.getViewBounds();
		}
	},
	
	startDragging : function (type, startPosition) {
		if(this.isEditingPort()){
			var bounds = this.getPortBounds();
		} else {
			var bounds = this.getBounds();
		}
		this.action = {type: type, start: startPosition.clone(), bounds: bounds};
	},
	
	continueDragging : function (nextPosition) {
		var dx = Math.round(nextPosition.getX()-this.action.start.getX());
		var dy = Math.round(nextPosition.getY()-this.action.start.getY());
		var type = this.action.type;
		if(this.isEditingPort()){
			var bounds = this.getPortBounds();
			this.action.bounds = this._getDraggedBounds(type, dx, dy, bounds);
		} else {
			// move but keep ratio
			var bounds = this.getBounds();
			this.action.bounds = this._getProportionalDraggedBounds(type, dx, dy, bounds);
		}
	},

	stopDragging : function (lastPosition) {
		this.continueDragging(lastPosition);
		if(this.isEditingPort()){
			this.setPortBounds(this.action.bounds);
		} else {
			this.setBounds(this.action.bounds);
		}
		this.action = null;
	},
	
	_getProportionalDraggedBounds : function (type, dx, dy, bounds) {
		var x = bounds.x;
		var y = bounds.y;
		var right = bounds.x + bounds.width;
		var top = bounds.y + bounds.height;
		var diagonal = new LineString();
		if(type == "component") {
			diagonal.appendCoordinate(new Coordinate(x+dx, y+dy));
			diagonal.appendCoordinate(new Coordinate(right+dx, top+dy));			
		} else {
			// calculate the width and height
			if(type == "bl") {
				diagonal.appendCoordinate(new Coordinate(x+dx, y+dy));
				diagonal.appendCoordinate(new Coordinate(right, top));			
			} else if(type == "br") {
				diagonal.appendCoordinate(new Coordinate(right+dx, y+dy));
				diagonal.appendCoordinate(new Coordinate(x, top));			
			} else if(type == "tl") {
				diagonal.appendCoordinate(new Coordinate(x+dx, top+dy));
				diagonal.appendCoordinate(new Coordinate(right, y));			
			} else if(type == "tr") {
				diagonal.appendCoordinate(new Coordinate(right+dx, top+dy));
				diagonal.appendCoordinate(new Coordinate(x, y));			
			} 			
			var dragBounds = diagonal.getBounds();
			var ratio = dragBounds.getHeight() > 0 ? dragBounds.getWidth()/dragBounds.getHeight() : 1000;
			var origRatio = this.bounds.getWidth()/this.bounds.getHeight();
			if(ratio > origRatio){
				var height = dragBounds.getHeight();
				var width = height * origRatio;
			} else {
				var width = dragBounds.getWidth();
				var height = width / origRatio;
			}
			// calculate the diagonal with the correct width and height
			diagonal = new LineString();
			if(type == "bl") {
				diagonal.appendCoordinate(new Coordinate(right-width, top-height));
				diagonal.appendCoordinate(new Coordinate(right, top));			
			} else if(type == "br") {
				diagonal.appendCoordinate(new Coordinate(x+width, top-height));
				diagonal.appendCoordinate(new Coordinate(x, top));			
			} else if(type == "tl") {
				diagonal.appendCoordinate(new Coordinate(right-width, y+height));
				diagonal.appendCoordinate(new Coordinate(right, y));			
			} else if(type == "tr") {
				diagonal.appendCoordinate(new Coordinate(x+width, y+height));
				diagonal.appendCoordinate(new Coordinate(x, y));			
			} 			
		}
		return diagonal.getBounds();
	},

	
	isEditingPort : function () {
		return this.editId.endsWith("port.bounds");
	}

});
