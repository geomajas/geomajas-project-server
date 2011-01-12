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
