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

dojo.provide("geomajas.gfx.GraphicsContext");
dojo.declare("GraphicsContext", null, {

	/**
	 * @class Interface for a Graphics context. Contains several basic mathods
	 * for drawing primitives onto the screen. Also understands the difference
	 * between drawing in worldspace or viewspace.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function (id, width, height, precision) {
		log.info("GraphicsContext: constructor");
		/** id of container node */
		this.id = id;
		/** width of container node */
		this.width = width;
		/** height of container node */
		this.height = height;
		/** screen bbox */
		this.bbox = new Bbox(0,0,width,height);
		/** the coordinate precision */
		this.precision = precision;
		/** The DOM node for this context. */
		this.node = dojo.byId(id,document);
		/** init for this browser */
		log.info("GraphicsContext: init ");
		this._afterInit();
		log.info("GraphicsContext: init done");
	},

	getElementById : function (id) {
		return document.getElementById(id);
	},

	/**
	 * sets the view size
	 */
	setScreenBox : function (bbox){
		this.width = bbox.width;
	 	this.height = bbox.height;
	 	this.bbox = bbox;
	 	this._afterResize();
	},
	
	setPrecision : function (precision) {
		this.precision = precision;
	},
	 
	getWidth : function () {
		return this.width;
	},

	getHeight : function () {
		return this.height;
	},

	/**
	 * Draw directly (implementation-specific shortcut)
	 */
	drawData : function (/*render data*/data, /* hashtable {id, style, transform,...} */ options) {
	},

	/**
	 * Draw a certain group of objects.
	 */
	drawGroup : function (/* hashtable {id, style, transform,...} */ options) {
	},
	
	/**
	 * Draw a line.
	 */
	drawLine : function (/*Paintable*/line, /* hashtable {id, style, transform,...}*/ options) {
	},
	
	/**
	 * Draw a polygon.
	 */
	drawPolygon : function (/*Paintable*/polygon, /* hashtable {id, style, transform,...}*/ options) {
	},
	
	/**
	 * Draw a rectangle.
	 */
	drawRectangle : function (/*Paintable*/rectangle, /* hashtable {id, style, transform,...}*/ options) {
	},
	
	/**
	 * Draw a circle.
	 */
	drawCircle : function (/*Paintable*/circle, /* hashtable {id, style, transform,...}*/ options) {
	},
	
	/**
	 * Draw a symbol/point object.
	 */
	drawSymbol : function (/*Paintable*/symbol, /* hashtable {id, style, transform,...}*/ options) {
	},
	
	/**
	 * Draw an image (from gif/jpg/png/...).
	 */
	drawImage : function (/*Paintable*/image, /* hashtable {id, style, transform,...}*/ options) {
	},
	
	/**
	 * Draw some text on the screen.
	 */
	drawText : function (/*Paintable*/text, /* hashtable {id, style, transform,...}*/ options) {
	},
	
	/**
	 * Draw a type (def/symbol for svg, shapetype for vml) 
	 */
	drawShapeType : function (/* hashtable {id}*/ options) {
	},
	
	/**
	 * Delete the shape with the specified id.
	 */
	deleteShape : function (/*String*/id, /*boolean*/childrenOnly) {
	},
	
	/**
	 * Set a specific cursor type.
	 * @param elementID optional. If not used, the cursor will be applied on the entire map.
	 */
	setCursor : function (/*String*/cursor, elementID) {
	},
	
	/**
	 * Set the background color.
	 */
	setBackgroundColor : function (/*String*/color) {
	}, 

	/**
	 * Hide something with the given id
	 */
	hide : function (/*String*/id) {
	},
	
	/**
	 * Unhide something with the given id
	 */
	unhide : function (/*String*/id) {
	},
	
	/**
	 * returns the root DOM node of this context
	 */
	getNode : function () {
		return this.node;
	},
	
	/**
	 * returns the xml representation of the root node as a string
	 */
	getXml : function () {		
	},
	
	computeTextLength : function (/*Paintable*/text) {
	},
	
	/**
	 * Cleanup resources to avoid for memory leaks
	 */
	destroy : function () {
		dojo._destroyElement(this.node);
		delete this.node;
	},
	
	_afterInit : function () {
	},
	
	_afterResize : function () {
	}

});