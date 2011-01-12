dojo.provide("geomajas.gfx.paintables.Rectangle");
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
dojo.require("geomajas.gfx.PainterVisitable");

dojo.declare("Rectangle", PainterVisitable, {

	/**
	 * @class An object that is supposed to represent a Rectangle.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @param id A unique identifier.
	 */
	constructor : function (id, cornerOnly) {
		/** A unique identifier. */
		this.id = "Rectangle";
		if (id) {
			this.id = id;
		} 

		/** The rectangle's begin position. */
		this.position = new Coordinate(0,0);
		
		/** The rectangle's width. */
		this.width = 0;

		/** The rectangle's height. */
		this.height = 0;

		/** A style object that determines the look. */
		this.style = new ShapeStyle("#FFFFFF", "1","#000000","1","1",null,null);
		
		/** Corner handles only */
		if(cornerOnly) {
			this.cornerOnly = true;
		} else {
			this.cornerOnly = false;
		}
	},
	
	/**
	 * Everything that can be drawn on the map, must be accessible by a
	 * PainterVisitor!
	 * @param visitor A PainterVisitor object. Comes from a MapWidget.
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit(this);
	},

	// Getters and setters:
	
	setBounds : function (bounds) {
		this.position = bounds.getOrigin();
		this.width = bounds.getWidth();
		this.height = bounds.getHeight();
	},

	getId : function () {
		return this.id;
	},

	setId : function (id) {
		this.id = id;
	},

	getPosition : function () {
		return this.position;
	},

	setPosition : function (position) {
		this.position = position;
	},
	
	/**
	 * returns an array of 8 rectangles, one for each editing handle (4 corners + 4 middles)
	 */ 
	getEditHandles : function () {
		var handles = [];
		var pos = this.getPosition();
		handles.push(this._getCenteredRectangle(this.id+".bl_hdl",new Coordinate(pos.getX(),pos.getY()+this.getHeight()),8));
		handles.push(this._getCenteredRectangle(this.id+".br_hdl",new Coordinate(pos.getX()+this.getWidth(),pos.getY()+this.getHeight()),8));
		handles.push(this._getCenteredRectangle(this.id+".tl_hdl",new Coordinate(pos.getX(),pos.getY()),8));
		handles.push(this._getCenteredRectangle(this.id+".tr_hdl",new Coordinate(pos.getX()+this.getWidth(),pos.getY()),8));
		if(!(this.cornerOnly)) {
			handles.push(this._getCenteredRectangle(this.id+".bm_hdl",new Coordinate(pos.getX()+0.5*this.getWidth(),pos.getY()+this.getHeight()),8));
			handles.push(this._getCenteredRectangle(this.id+".tm_hdl",new Coordinate(pos.getX()+0.5*this.getWidth(),pos.getY()),8));
			handles.push(this._getCenteredRectangle(this.id+".lm_hdl",new Coordinate(pos.getX(),pos.getY()+0.5*this.getHeight()),8));
			handles.push(this._getCenteredRectangle(this.id+".rm_hdl",new Coordinate(pos.getX()+this.getWidth(),pos.getY()+0.5*this.getHeight()),8));
		}
		return handles;
	},
	
	_getCenteredRectangle : function (id, coord, width) {
		var rect = new Rectangle(id);
		rect.setPosition(new Coordinate(coord.getX()-0.5*width,coord.getY()-0.5*width));
		rect.setHeight(width);
		rect.setWidth(width);
		return rect;
	},

	getWidth : function () {
		return this.width;
	},

	setWidth : function (width) {
		this.width = width;
	},

	getHeight : function () {
		return this.height;
	},

	setHeight : function (height) {
		this.height = height;
	},

	getStyle : function () {
		return this.style;
	},

	setStyle : function (style) {
		this.style = style;
	},
	
	toString : function () {
		return "[x="+parseInt(this.getPosition().getX())+", y="+parseInt(this.getPosition().getY())+
			", w="+parseInt(this.width)+", h="+parseInt(this.height)+"]";
	},
	
	clone : function () {
		var clone = new Rectangle(this.id, this.cornerOnly);
		clone.setPosition(this.getPosition().clone());
		clone.setWidth(this.getWidth());
		clone.setHeight(this.getHeight());
		clone.setStyle(this.style.clone);
		return clone;
	}
});
