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