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

dojo.provide("geomajas.map.RasterNode");
dojo.require("dojox.collections.Dictionary");
dojo.require("geomajas.gfx.PainterVisitable");
dojo.require("geomajas.gfx.PainterVisitor");

dojo.declare("RasterNode", PainterVisitable, {

	/**
	 * @fileoverview A RasterNode represents a subset of raster images in a layer.
	 * @class A RasterNode represents a subset of raster images in a layer.
	 *
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends PainterVisitable
	 */
	constructor : function () {		
		/** Map of the features in this node */
		this.images = new dojox.collections.Dictionary();
		
		/** unique id of this node (tiling level) */
		this.id = null;
		
		/** Is this level visible */
		this.visible = false;
	},

	/**
	 * Accept visitor visiting.
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, recursive) {
		visitor.visit(this);
		if(recursive){
			var img = this.images.getValueList();
			for(var i = 0; i < img.length; i++){
				img[i].accept(visitor, bbox, true);
			}			
		}
	},	

	// Getters and setters:

	getImages : function () {
		return this.images;
	},

	addImage : function (image) {
		this.images.add (image.getId(), image);
	},

	removeImage: function (image) {
		if (this.images.contains(image.getId())) {
			this.images.remove (image.getId());
		}
	},

	getId : function () {
		return this.id;
	},

	setId : function (id) {
		this.id = id;
	},

	isVisible : function () {
		return this.visible;
	},

	setVisible : function (visible) {
		this.visible = visible;
	}
});
