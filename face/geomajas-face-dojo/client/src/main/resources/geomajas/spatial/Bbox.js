dojo.provide("geomajas.spatial.Bbox");
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
dojo.require("geomajas.spatial.Coordinate");

dojo.declare("Bbox", null, {

	/**
	 * @fileoverview Bounding Box.
	 * @class Bounding Box.<br>
	 * Determined by a point, it's width and it's height. If the 4 params
	 * aren't correct, everything is set to 0.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param x The x-value for the origin-point.
	 * @param y The y-value for the origin-point.
	 * @param width The bounding box' width (along the X-axis).
	 * @param height The bounding box' height (along the Y-axis).
	 */
	constructor : function (x, y, width, height) {
		if (x != null && y != null && width != null && height != null) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		} else {
			this.x = 0;
			this.y = 0;
			this.width = 0;
			this.height = 0;
		}
	},

	toJSON : function () {
		return {
			javaClass : "org.geomajas.geometry.Bbox",
			x : this.x,
			y : this.y,
			width : this.width,
			height : this.height
		};
	},

	/**
	 * Set new values for this Bbox.
	 * @param x The x-value for the origin-point.
	 * @param y The y-value for the origin-point.
	 * @param width The bounding box' width (along the X-axis).
	 * @param height The bounding box' height (along the Y-axis).
	 */
	set : function (/*Double*/x, /*Double*/y, /*Double*/width, /*Double*/height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	},

	equals : function (bbox) {
		return (
			this.x == bbox.x &&
			this.y == bbox.y &&
			this.width == bbox.width &&
			this.height == bbox.height
		);
	},

	clone : function () {
		return new Bbox(this.x, this.y, this.width, this.height);
	},
	
	/**
	 * Returns the origin (x, y) as a Coordinate.
	 */
	getOrigin : function () {
		return new Coordinate (this.x, this.y);
	},
	
	/**
	 * Clones and resizes to the specified width and height, keeping the center fixed
	 */
	cloneWithSize : function (width, height) {
		var clone = this.clone();
		clone.setWidth(width);
		clone.setHeight(height);
		clone.setCenterPoint(this.getCenterPoint());
		return clone;
	},
	
	/**
	 * Moves center to the specified coordinate
	 */
	setCenterPoint : function (coordinate) {
		this.x = coordinate.getX() - 0.5 * this.width;
		this.y = coordinate.getY() - 0.5 * this.height;
	},
	
	/**
	 * Get the center of the bounding box as a Coordinate.
	 */
	getCenterPoint : function () {
		return new Coordinate (this.x + this.width/2, this.y + this.height/2);
	},

	/**
	 * Get the endpoint of the bounding box as a Coordinate.
	 */
	getEndPoint : function () {
		return new Coordinate (this.x + this.width, this.y + this.height);
	},

	/**
	 * Get the coordinates of the bounding box (5 coordinates) as an array.
	 */
	getCoordinates : function () {
		var result = [];
		result.push(new Coordinate(this.x,this.y));
		result.push(new Coordinate(this.x+ this.width,this.y));
		result.push(new Coordinate(this.x+ this.width,this.y+ this.height));
		result.push(new Coordinate(this.x,this.y+ this.height));
		result.push(new Coordinate(this.x,this.y));
		return result;
	},

	/**
	 * Does this bounding box contain the given bounding box?
	 * @param other Another Bbox.
	 * @return true is the other is completely surrounded by this one,
	 *         false otherwise.
	 */
	contains : function (/*Bbox*/other) {
		if(other.getX() < this.getX()){
			return false;
		}
		if(other.getY() < this.getY()){
			return false;
		}
		if(other.getEndPoint().getX() > this.getEndPoint().getX()){
			return false;
		}
		if(other.getEndPoint().getY() > this.getEndPoint().getY()){
			return false;
		}
		return true;		
	},
	

	/**
	 * Does this bounding box contain the given coordinate ?
	 * @param other Another Bbox.
	 * @return true is the coordinate is completely surrounded by this bbox
	 */
	containsCoordinate : function (/*Coordinate*/coord) {
		if(coord.getX() < this.getX()){
			return false;
		}
		if(coord.getY() < this.getY()){
			return false;
		}
		if(coord.getX() > this.getEndPoint().getX()){
			return false;
		}
		if(coord.getY() > this.getEndPoint().getY()){
			return false;
		}
		return true;		
	},
	
	/**
	 * Does this bounding box intersect the given bounding box?
	 * @param other Another Bbox.
	 * @return true if the other intersects this one,
	 *         false otherwise.
	 */
	intersects : function (/*Bbox*/other) {
		//dojo.debug(other);
    	if(other.getX() > this.getEndPoint().getX()){
    		return false;
    	}
    	if(other.getY() > this.getEndPoint().getY()){
    		return false;
    	}
    	if(other.getEndPoint().getX() < this.getX()){
    		return false;
    	}
    	if(other.getEndPoint().getY() < this.getY()){
    		return false;
    	}
		return true;		
	},
	
	/**
	 * Computes the intersection of this bounding box with the specified bounding box
	 * @param other Another Bbox.
	 * @return bounding box of intersection
	 */
	intersection : function(/*Bbox*/other) {
		if(!this.intersects(other)){
			return new Bbox();
		} else {
			var minx = other.getX() > this.getX() ? other.getX() : this.getX();
			var maxx = other.getEndPoint().getX() < this.getEndPoint().getX() ? other.getEndPoint().getX() : this.getEndPoint().getX();
			var miny = other.getY() > this.getY() ? other.getY() : this.getY();
			var maxy = other.getEndPoint().getY() < this.getEndPoint().getY() ? other.getEndPoint().getY() : this.getEndPoint().getY();
			return new Bbox(minx,miny,(maxx-minx),(maxy-miny));
		}
	},

	/**
	 * Calculates the union of 2 bounding boxes.
	 * @param other The other Bbox.
	 */
	union : function (/*Bbox*/other) {
		if (other.getWidth() == 0 || other.getHeight == 0) {
			return this.clone();
		}
		if (this.width == 0 || this.height == 0) {
			return other.clone();
		}
		var minx = other.getX() < this.getX() ? other.getX() : this.getX();
		var maxx = other.getEndPoint().getX() > this.getEndPoint().getX() ? other.getEndPoint().getX() : this.getEndPoint().getX();
		var miny = other.getY() < this.getY() ? other.getY() : this.getY();
		var maxy = other.getEndPoint().getY() > this.getEndPoint().getY() ? other.getEndPoint().getY() : this.getEndPoint().getY();
		return new Bbox(minx,miny,(maxx-minx),(maxy-miny));
	},
	
	buffer : function (distance) {
		if (distance > 0) {
			return new Bbox(this.x - distance,this.y - distance, this.width+(distance*2),this.height+(distance*2));
		}
		return this.clone();
	},

	toString : function () {
		return "[x="+parseInt(this.x)+", y="+parseInt(this.y)+", w="+parseInt(this.width)+", h="+parseInt(this.height)+"]";
	},

	// Getters and setters.

	getX : function () {
		return this.x;
	},

	setX : function (/*Double*/x) {
		this.x = x;
	},

	getY : function () {
		return this.y;
	},

	setY : function (/*Double*/y) {
		this.y = y;
	},

	getOrigX : function () {
		return this.x;
	},

	getOrigY : function () {
		return this.y;
	},

	getEndX : function () {
		return this.x + this.width;
	},

	getEndY : function () {
		return this.y + this.height;
	},

	getWidth : function () {
		return this.width;
	},

	setWidth : function (/*Double*/width) {
		this.width = width;
	},

	getHeight : function () {
		return this.height;
	},

	setHeight : function (/*Double*/height) {
		this.height = height;
	},
	
	translate : function (/*Double*/dx, /*Double*/dy){
		this.x = this.x + dx;
		this.y = this.y + dy;
	}
});
