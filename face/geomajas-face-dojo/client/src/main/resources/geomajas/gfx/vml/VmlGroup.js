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

dojo.provide("geomajas.gfx.vml.VmlGroup");
dojo.declare("VmlGroup", null, {

	/**
	 * @class Represents a VML group tag, together with style (to be converted to shapetype) and transformation properties
	 * 
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/* VML node */ rawNode) {
		this.rawNode = rawNode;
		this.children = new dojox.collections.ArrayList();
		this.width = 0;
		this.height = 0;
		this.coordWidth = 0;
		this.coordHeight = 0;
		this.coordX = 0;
		this.coordY = 0;
		this.flipX = false;
		this.flipY = false;
		this.id = null;
		this.style = null;
	},
	
	setParent : function (parent){
		this.parent = parent;
	},

	setWidth : function (width) {
		this.width = width;
	},

	setHeight : function (height) {
		this.height = height;
	},
	
	setId : function (id){
		this.id = id;
	},
	
	setStyle : function (/* style object */ style) {
		this.style = style;
	},

	/**
	 * limited to diagonal matrices
	 */
	setTransform : function(matrix){
        log.debug("VmlGroup.setTransform");
		this.scale(matrix.xx,matrix.yy);
		this.translate(matrix.dx,matrix.dy);
	},
	
	/**
	 * propagates the correct width, height and local coordinate space to the children
	 */
	
	/**
	 * Scales by using the local coordinate system. Transformations are right-chained.
	 * Transformations are limited to 1 group level !!!
	 * @param scaleY factor by which local y-coordinates must be multiplied to obtain view coordinates
	 */
	scale : function(scaleX, scaleY){
			if(scaleX < 0){
				if(this.flipX){
					this.flipX = false;
					this.coordX = this.coordX + 0.5*this.coordWidth;
				} else {
					this.flipX = true;
					this.coordX = this.coordX - 0.5*this.coordWidth/Math.abs(scaleX);
				}
			} else {
				if(this.flipX){
					this.coordX = this.coordX+0.5*(scaleX-1.0)/scaleX*this.coordWidth;
				} else {
					// nothing to do
				}
			}
			if(scaleY < 0){
				if(this.flipY){
					this.flipY = false;
					this.coordY = this.coordY + 0.5*this.coordHeight;
				} else {
					this.flipY = true;
					this.coordY = this.coordY - 0.5*this.coordHeight/Math.abs(scaleY);
				}
			} else {
				if(this.flipY){
					this.coordY = this.coordY+0.5*(scaleY-1.0)/scaleY*this.coordHeight;
				} else {
					// nothing to do
				}
			}
			this.coordWidth = this.coordWidth/Math.abs(scaleX);
			this.coordHeight = this.coordHeight/Math.abs(scaleY);
	},
	
	/**
	 * Translates by using the local coordinate system. Transformations are right-chained.
	 * Transformations are limited to 1 group level !!!
	 * @param dx distance to be added to local x-coordinates to obtain view coordinates
	 * @param dy distance to be added to local y-coordinates to obtain view coordinates
	 */
	translate : function(dx, dy){		
			// this stuff can be derived from the general transform formulae !!!		
			if(this.flipX){
				this.coordX = this.coordX + 0.5 * this.coordWidth/this.width * dx;
			} else {
				this.coordX = this.coordX - 0.5 * this.coordWidth/this.width * dx;
			}
			if(this.flipY){
				this.coordY = this.coordY + 0.5 * this.coordHeight/this.height * dy;
			} else {
				this.coordY = this.coordY - 0.5 * this.coordHeight/this.height * dy;
			}
	},

	addChild : function (/* VmlGroup */child){
	 	this.children.add(child);
	 	child.setParent(this);
	}
});
