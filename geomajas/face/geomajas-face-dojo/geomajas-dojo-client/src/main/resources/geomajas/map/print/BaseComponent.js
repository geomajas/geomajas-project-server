dojo.provide("geomajas.map.print.BaseComponent");
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
dojo.declare("BaseComponent", PainterVisitable, {

	statics : {
		STATE_IDLE : 1,
		STATE_HOVERED : 2,
		STATE_EDITING : 3
	},

	id : null,
	editId : null,

/**
	 * @class 
	 * A basic print component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/* json object */ json, /* unique string */ id) {
		if(json){
			dojo.mixin(this,json);
		}
		// needed for cloning
		this.id = id;
		if(!this.children){
			this.children = {list: [], javaClass : "java.util.ArrayList"};
		}
		this.state = this.statics.STATE_IDLE;
		this.action = null;
	},

	setLayoutConstraint : function ( layoutConstraint) {
		this.layoutConstraint = layoutConstraint;
	},

	getId : function () {
		return this.id;
	},
	
	getLabel : function () {
		return this.tag;
	},
	
	setTag : function (tag) {
		this.tag = tag;
	},
	
	getTag : function () {
		return this.tag;
	},
	
	getState : function () {
		return this.state;
	},
	
	setHovered : function () {
		this.state = this.statics.STATE_HOVERED;
	},
	
	setIdle : function () {
		this.state = this.statics.STATE_IDLE;
	},
	
	setEditing : function (id) {
		this.state = this.statics.STATE_EDITING;
		this.editId = id;
	},

	isHovered : function () {
		return this.state == this.statics.STATE_HOVERED;
	},
	
	isIdle : function () {
		return this.state == this.statics.STATE_IDLE;
	},
	
	isEditing : function () {
		return this.state == this.statics.STATE_EDITING;
	},
	
	/**
	 * recursively visit this component and its children
	 */
	accept : function (/*Visitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit (this);
		for(var i = 0; i < this.children.list.length; i++){
			this.children.list[i].accept(visitor,bbox,recursive);
		}
	},
	
	/**
	 * recursively execute a function on this component and its children (parent first !)
	 */
	executeTopDown : function (/*function*/callback) {
		if(callback(this)){
			return true;
		}
		for(var i = 0; i < this.children.list.length; i++){
			if(this.children.list[i].executeTopDown(callback)){
				return true;
			}
		}
		return false;
	},
	
	/**
	 * recursively execute a function on this component and its children (child first !)
	 */
	executeBottomUp : function (/*function*/callback) {
		for(var i = 0; i < this.children.list.length; i++){
			if(this.children.list[i].executeBottomUp(callback)){
				return true;
			}
		}
		if(callback(this)){
			return true;
		}
	},

	/**
	 * returns the bounds as bbox
	 */
	getBounds : function () {
		if(!this.bounds){
			log.error("no bounds for "+this.id);
			for(var name in this){
				log.error(name+"="+this[name]);
			}
		}
		return new Bbox(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
	},

	/**
	 * returns the view bounds
	 */
	getViewBounds : function () {
		return geomajasConfig.printManager.getTransform().transformBoundsPrintToView(this.getBounds());
	},
	
	/**
	 * returns the edit bounds
	 */
	getEditBounds : function () {
		return geomajasConfig.printManager.getTransform().transformBoundsPrintToView(this.action.bounds);
	},
	
	/**
	 * set this bbox as bounds
	 */
	setBounds : function (bbox) {
		var dx = bbox.getOrigin().getX() - this.bounds.x;
		var dy = bbox.getOrigin().getY() - this.bounds.y;
		this.bounds.x = bbox.getOrigin().getX();
		this.bounds.y = bbox.getOrigin().getY();
		this.bounds.width = bbox.getWidth();
		this.bounds.height = bbox.getHeight();
		// autowidth if component was not resized !!!
		if(this.action){
			if(!this.action.componentOnly){
			// save the new bounds as constraints (absolute positioning now !)
			this.constraint.alignmentX = 6;
			this.constraint.alignmentY = 6;
			this.constraint.marginX = this.bounds.x;
			this.constraint.marginY = this.bounds.y;
			this.constraint.width = this.bounds.width;
			this.constraint.height = this.bounds.height;
			} else {	
				switch(this.constraint.alignmentX){
					case 0: // left
						this.constraint.marginX += dx;
						break;
					case 3: // right
						this.constraint.marginX -= dx;
						break;
					case 5: // justified
						this.constraint.alignmentX = 0;
						this.constraint.marginX = this.bounds.x;
						this.constraint.width = this.bounds.width;
						break;
					default:
						this.constraint.alignmentX = 0;
						this.constraint.marginX = this.bounds.x;
				}
				
				switch(this.constraint.alignmentY){
					case 1: // bottom
						this.constraint.marginY += dy;
						break;
					case 4: // top
						this.constraint.marginY -= dy;
						break;
					case 5: // justified
						this.constraint.alignmentY = 1;
						this.constraint.marginY = this.bounds.y;
						this.constraint.height = this.bounds.height;
						break;
					default:
						this.constraint.alignmentY = 1;
						this.constraint.marginY = this.bounds.y;
				}
				
			}
		}
	},
	
	addComponent : function (child) {
		child.id = this.getId()+"."+this.children.list.length;
		this.children.list.push(child);
	},
	
	removeChildByTag : function (tag) {
		for(var i = this.children.list.length-1; i >= 0; i--){
			if(this.children.list[i].getTag() == tag){
				this.children.list.splice(i,1);
			}
		}
	},
	
	startDragging : function (type, startPosition) {
		this.action = {type: type, start: startPosition.clone(), bounds: this.getBounds(), componentOnly: false};
	},	
	
	isDragging : function (){
		return this.action != null;
	},
	
	continueDragging : function (nextPosition) {
		var dx = Math.round(nextPosition.getX()-this.action.start.getX());
		var dy = Math.round(nextPosition.getY()-this.action.start.getY());
		var type = this.action.type;
		this.action.bounds = this._getDraggedBounds(type, dx, dy, this.bounds);
	},
	
	stopDragging : function (lastPosition) {
		this.continueDragging(lastPosition);
		this.setBounds(this.action.bounds);
		log.info("stop bounds "+this.action.bounds);
		log.info("stop bounds x="+this.bounds.x+",y="+this.bounds.y+
				",width="+this.bounds.width+",height="+this.bounds.height);
		this.action = null;
	}, 

	_getDraggedBounds : function (type, dx, dy, bounds) {
		var x = bounds.x;
		var y = bounds.y;
		var right = bounds.x + bounds.width;
		var top = bounds.y + bounds.height;
		var diagonal = new LineString();
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
		} else if(type == "bm") {
			diagonal.appendCoordinate(new Coordinate(right, y+dy));
			diagonal.appendCoordinate(new Coordinate(x, top));			
		} else if(type == "tm") {
			diagonal.appendCoordinate(new Coordinate(right, top+dy));
			diagonal.appendCoordinate(new Coordinate(x, y));			
		} else if(type == "lm") {
			diagonal.appendCoordinate(new Coordinate(x+dx, top));
			diagonal.appendCoordinate(new Coordinate(right, y));			
		} else if(type == "rm") {
			diagonal.appendCoordinate(new Coordinate(right+dx, top));
			diagonal.appendCoordinate(new Coordinate(x, y));			
		} else if(type == "component") {
			diagonal.appendCoordinate(new Coordinate(x+dx, y+dy));
			diagonal.appendCoordinate(new Coordinate(right+dx, top+dy));			
			if(this.action){
				this.action.componentOnly = true;
		}
		}
		return diagonal.getBounds();
	}
	
	
});
