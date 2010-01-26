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

dojo.provide("geomajas.map.RasterImage");
dojo.require("geomajas.gfx.PainterVisitable");
dojo.require("geomajas.gfx.PainterVisitor");

dojo.declare("RasterImage", PainterVisitable, {

	/**
	 * @fileoverview A RasterImage is a image/tile of a raster layer.
	 * @class A RasterImage is a image/tile of a raster layer. Careful, these babies 
	 * are supposed to be drawn in world space !
	 *
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends PainterVisitable
	 */
	constructor : function () {		
	    /** Unique id of the image (normally, id = <layer id>+"."+<tile level>+"."+<x-index>,<y-index> ) */
		this.id = null;

		/** URL of the image */
		this.url = null;

		/** bounds of the image */
		this.bounds = null;

		/** Tile level (0 is 1 image for the maximum extent, 1 is (2x2) images for the maximum extent, etc...) */
		this.level = null;

		/** x-index in tile coordinates (rightward, first tile is 0) */
		this.xIndex = null;

		/** y-index in tile coordinates (upward, first tile is 0) */
		this.yIndex = null;
		
		/** factory to create geometry from bounds*/
		this.factory = new GeometryFactory(4326, 2);
	},
	
	/**
	 * Accept visitor visiting.
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit(this);
	},	
	
	clone : function () {
		var image = new RasterImage();
		image.id = this.id;
		image.url = this.url;
		image.bounds = this.bounds;
		image.level = this.level;
		image.xIndex = this.xIndex;
		image.yIndex = this.yIndex;
		return image;
	},

	// Getters and setters:

	getId : function () {
		return this.id;
	},

	setId : function (id) {
		this.id = id;
	},

	getUrl : function () {
		return this.url;
	},

	setUrl : function (url) {
		this.url = url;
	},

	
	getBounds : function () {
		return this.bounds;
	},

	setBounds : function (bounds) {
		this.bounds = bounds;
	},
	
	getLevel : function () {
		return this.level;
	},

	setLevel : function (level) {
		this.level = level;
	},
	
	getXIndex : function () {
		return this.xIndex;
	},

	setXIndex : function (xIndex) {
		this.xIndex = xIndex;
	},
	
	getYIndex : function () {
		return this.yIndex;
	},

	setYIndex : function (yIndex) {
		this.yIndex = yIndex;
	},
	
	getPosition : function () {
		return this.bounds.getOrigin();
	},


	getWidth : function () {
		return this.bounds.getWidth();
	},


	getHeight : function () {
		return this.bounds.getHeight();
	},

	getStyle : function () {
		return this.style;
	},
	
	setStyle : function (style) {
		this.style = style;
	},

	getHref : function () {
		return this.url;
	},
	
	getBoundsAsGeometry : function () {
		var coordinates = this.bounds.getCoordinates();
		var shell = this.factory.createLinearRing(coordinates);
		var polygon = this.factory.createPolygon(shell,[]);
		return polygon; 
	}
	
});