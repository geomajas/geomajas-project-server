dojo.provide("geomajas.gfx.svg.SvgGraphicsContext");
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
dojo.require("geomajas.gfx.GraphicsContext");
dojo.require("geomajas.gfx.svg.decoders.SvgPathDecoder");
dojo.require("geomajas.gfx.svg.decoders.SvgStyleDecoder");
dojo.require("geomajas.spatial.transform.WorldViewTransformation");

geomajas.gfx.svg.xmlns = {
	xlink: "http://www.w3.org/1999/xlink",
	svg:   "http://www.w3.org/2000/svg"
};

dojo.extend(GraphicsContext, {

	/**
	 * @class Implementation of the "GraphicsContext" interface for drawing in
	 * SVG.
	 * @author Pieter De Graef & Jan De Moerloose
	 *
	 * @constructor
	 * @extends GraphicsContext
	 * 
	 */
	svgNode : null,
	defs : null,
	backgroundGroup : null,
	mapGroup : null,
	screenGroup : null,
	styleDecoder : new SvgStyleDecoder(),
	decoder : new SvgPathDecoder(),

	/**
	 * Draw directly (implementation-specific shortcut)
	 */
	drawData : function (/*render data*/data, /* hashtable {id, style, transform,...} */ options) {
		this._findOrCreateData(data,options);
	},

	/**
	 * Draw a certain group of objects.
	 */
	drawGroup : function (/* hashtable {id, style, transform,...} */ options) {
		var g = this._findOrCreateElement("g",options);
	},

	/**
	 * Draw a line.
	 */
	drawLine : function (/*Paintable*/line, /* hashtable {id, style, transform,...}*/ options) {
		var path = this._findOrCreateElement("path", options);
		if (line) {
			this._updateAttribute(path, "d", this.decoder.decode(line));
		} else if(options.path){
			this._updateAttribute(path, "d", options.path);
		} 
	},
	
	/**
	 * Draw a polygon.
	 */
	drawPolygon : function (/*Paintable*/polygon, /* hashtable {id, style, transform,...}*/ options) {
		var path = this._findOrCreateElement("path", options);
		if (polygon) {
			this._updateAttribute(path, "d", this.decoder.decode(polygon));
		} else if(options.path){
			this._updateAttribute(path, "d", options.path);
		} 
		this._updateAttribute(path, "fill-rule", "evenodd");
	},

	/**
	 * Draw a rectangle.
	 */
	drawRectangle : function (/*Paintable*/rectangle, /* hashtable {id, style, transform,...}*/ options) {
		var rect = this._findOrCreateElement("rect", options);
		this._updateAttribute(rect, "x", rectangle.getPosition().getX());
		this._updateAttribute(rect, "y", rectangle.getPosition().getY());
		this._updateAttribute(rect, "width", rectangle.getWidth());
		this._updateAttribute(rect, "height", rectangle.getHeight());
	},

	/**
	 * Draw a circle.
	 */
	drawCircle : function (/*Paintable*/circle, /* hashtable {id, style, transform,...}*/ options) {
		var circleElement = this._findOrCreateElement("circle",options);
		this._updateAttribute(circleElement, "cx", circle.getPosition().getX());
		this._updateAttribute(circleElement, "cy", circle.getPosition().getY());
		this._updateAttribute(circleElement, "r", circle.getR());
	},

	/**
	 * Draw a symbol/point object.
	 */
	drawSymbol : function (/*Paintable*/symbol, /* hashtable {id, style, transform,...}*/ options) {
		var g = this._findOrCreateElement("g",options);
		var point = this._findOrCreateElement("use", {id : options.id+".use"});
		if (options.styleId != null) {
			this._updateAttributeNS(geomajas.gfx.svg.xmlns.xlink, point, "xlink:href", "#" + options.styleId);
		} else {
			this._updateAttributeNS(geomajas.gfx.svg.xmlns.xlink, point, "xlink:href", "#"+point.parentNode.id+".style");
		}
		if (symbol) {
			var coordinate = symbol.getCoordinates();
			this._updateAttribute(point, "x", coordinate[0].getX());
			this._updateAttribute(point, "y", coordinate[0].getY());
		}
	},

	/**
	 * Draw an image (from gif/jpg/png/...).
	 */
	drawImage : function (/*Paintable*/image, /* hashtable {id, style, transform,...}*/ options) {
		var imageElement = this._findOrCreateElement("image", options);
        log.debug("svg.drawImage "+image.id+" "+image.getPosition().getX()+","+image.getPosition().getY()+" s "+image.getWidth()+","+image.getWidth());
		this._updateAttribute(imageElement, "x", image.getPosition().getX());
		this._updateAttribute(imageElement, "y", image.getPosition().getY());
		this._updateAttribute(imageElement, "width", image.getWidth());
		this._updateAttribute(imageElement, "height", image.getHeight());
		this._updateAttribute(imageElement, "preserveAspectRatio", "none");
		this._updateAttributeNS(geomajas.gfx.svg.xmlns.xlink, imageElement, "xlink:href", image.getHref());
	},
	
	/**
	 * Draw some text on the screen.
	 */
	drawText : function (/*Paintable*/text, /* hashtable {id, style, transform,...}*/ options) {
		var textElement = this._findOrCreateElement("text",options);
		if(!textElement.firstChild){
			var textNode = document.createTextNode(text.getText());
			textElement.appendChild (textNode);
		}
		textElement.textContent = text.getText();
		if (text.getPosition() != null) {
			var fontSize = 12;
			if (text.getStyle() != null) {
				fontSize = text.getStyle().getFontSize();
			}
			this._updateAttribute(textElement, "x", text.getPosition().getX());
			this._updateAttribute(textElement, "y", text.getPosition().getY() + fontSize);
		}
	},
	
	/**
	 * Draw a type (def/symbol for svg, shapetype for vml) 
	 */
	drawShapeType : function (/* hashtable {id, style}*/ options) {
		try {
			var symbol = options.style.symbol;
			if(!symbol){
				return;
			}
			var def = document.getElementById(options.id);
			var exists = false;
			if (def) {
				exists = true;
			} else {
				def = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "symbol");
				def.setAttribute("id", options.id);
				def.setAttribute("overflow", "visible");
			}
			var node = null;
			if(symbol.rect){
				var width = symbol.rect.w;
				var height = symbol.rect.h;
				if(options.transform && options.transform.xx != 0){
					var scale = options.transform.xx;
					width = (width/scale).toFixed();
					height = (height/scale).toFixed();
				}
				node = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "rect");
				node.setAttribute("width", width);
				node.setAttribute("height", height);
				node.setAttribute("x", -width/2);
				node.setAttribute("y", -height/2);
			} else if(symbol.circle){
				var radius = symbol.circle.r;
				if(options.transform && options.transform.xx != 0){
					//rotate (scale only !!!!)
					var scale = options.transform.xx;
					radius = (radius/scale).toFixed();
				}
				node = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "circle");
				node.setAttribute("cx", 0);
				node.setAttribute("cy", 0);
				node.setAttribute("r", radius);
			}
			if (exists) {
				while (def.firstChild) {
					def.removeChild(def.firstChild);
				}
				def.appendChild(node);
			} else {
				def.appendChild(node);
				this.defs.appendChild(def);
			}
		} catch(e){
			for(word in e){
				log.error(word+":"+e[word])
			}
			log.error("drawSymbol : could not add shape type");
		}
	},

	/**
	 * Delete the shape with the specified id.
	 */
	deleteShape : function (/*String*/id, /*boolean*/childrenOnly) {
		//log.info("deleting "+id);
		var shape = document.getElementById (id);
		if(!shape){
			return;
		}
		var parent = shape.parentNode;
		if(shape && childrenOnly){
            while (shape.childNodes.length > 0) {
            	dojo._destroyElement(shape.firstChild);
             }
		}
		else if(shape && parent) {
			dojo._destroyElement(shape);
		}
	},
	
	/**
	 * Set a specific cursor type.
	 */
	setCursor : function (/*String*/cursor, elementID) {
		if (elementID == null) {
			this.svgNode.style.cursor=cursor;
		} else {
			var element = document.getElementById(elementID);
			element.style.cursor=cursor;
		}
	},
	
	/**
	 * Set the background color.
	 */
	setBackgroundColor : function (/*String*/color) {
		this.background.setAttribute("style", "fill:" + color);
	},
	

	/**
	 * Hide something with the given id
	 */
	hide : function (/*String*/id) {
		var element = document.getElementById (id);
		if (element != null) {
			this._updateAttribute(element, "display", "none");
		} 
	},
	
	/**
	 * Unhide something with the given id
	 */
	unhide : function (/*String*/id) {
		var element = document.getElementById (id);
		if (element != null) {
			this._updateAttribute(element, "display", "inline");
		}
	},
	
	/**
	 * returns the root DOM node of this context
	 */
	getNode : function () {
		return this.svgNode;
	},

	/**
	 * returns the xml representation of the root node as a string
	 */
	getXml : function () {
		return (new XMLSerializer()).serializeToString(this.svgNode);		
	},

	/**
	 * Return the length in pixels of a given {@link Text} object.
	 * @param text A paintable {@link Text} object.
	 * @return The size in pixels.
	 */
	computeTextLength : function (/*Paintable*/text) {
		var textElement = document.createElementNS (geomajas.gfx.svg.xmlns.svg, "text");
		textElement.setAttribute ("style", text.getStyle());

		var textNode = document.createTextNode(text.getText());
		textElement.appendChild (textNode);

		this.screenGroup.appendChild (textElement);
		var size = textElement.getComputedTextLength();
		this.screenGroup.removeChild (textElement);
		return size;
	},

	/**
	 * Browser-specific initialization
	 */
	_afterInit : function(){
		log.info("SvgGraphicsContext:_afterInit");
		this.decoder = new SvgPathDecoder();
		log.info("SvgGraphicsContext:_afterInit");
		this.svgNode = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "svg");
		log.info("SvgGraphicsContext:_afterInit : created svgNode "+this.svgNode);
		this.svgNode.setAttribute("width",  this.width);
		this.svgNode.setAttribute("height", this.height);
		this.svgNode.setAttribute("viewBox", "0 0 " + this.width + " " + this.height);

		// defs:
		this.defs = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "defs");
		this.svgNode.appendChild(this.defs);
				
		// Background:
		this.backgroundGroup = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "g");
		this.backgroundGroup.setAttribute("style", "fill-opacity:1;stroke:#FFFFFF;stroke-width:0;");
		this.background = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "rect");
		this.background.setAttribute("x", 0);
		this.background.setAttribute("y", 0);
		this.background.setAttribute("width", "100%");
		this.background.setAttribute("height", "100%");
		//this.background.setAttribute("style", "fill:" + dijit.byId(this.id).getBackgroundColor());
		this.backgroundGroup.appendChild(this.background);
		this.svgNode.appendChild(this.backgroundGroup);

		// Map group:
		this.mapGroup = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "g");
		this.mapGroup.setAttribute("id", this.id + ".map");
		this.svgNode.appendChild(this.mapGroup);

		// Worldspace group:
		this.worldGroup = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "g");
		this.worldGroup.setAttribute("id", this.id + "._world");
		this.svgNode.appendChild(this.worldGroup);

		// Screen group:
		this.screenGroup = document.createElementNS(geomajas.gfx.svg.xmlns.svg, "g");
		this.screenGroup.setAttribute("id", this.id + "_screen");
		this.svgNode.appendChild(this.screenGroup);

		// append to container
		this.node.appendChild(this.svgNode);
	},

	/**
	 * sets the view size
	 */
	 _afterResize : function (){
		this.svgNode.setAttribute("width",  this.width);
		this.svgNode.setAttribute("height", this.height);
		this.svgNode.setAttribute("viewBox", "0 0 " + this.width + " " + this.height);		
	 },

	/**
	 * @private
	 */
	_findOrCreateData : function(data, /* hashtable {id, style, transform,...}*/ options) {
		var g = document.getElementById (options.id);
		if(!g){
			g = this._findOrCreateElement("g",options);	
			var xml = "<g xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">"+data+"</g>";	
			var fragment = new DOMParser().parseFromString(xml,"text/xml");	
			if(fragment) {
			    var children = fragment.childNodes;         
			    for(var i=0; i < children.length; i++) {    
					if (dojo.isMozilla) {
						node = children[i];
					} else {
						// Google Chrome:
						node = this._cloneElement(children[i]);
					}
					g.appendChild (node);
			    }
			}
		}		
	 },

	/**
	 * @private
	 */
	_findOrCreateElement : function (name, /* hashtable {id, style, transform,...}*/ options){
		if(!options.id){
			return null;
		}
		var element = null;
		if(options.id == this.id){
			element = this.mapGroup;
		} else {
			element = document.getElementById (options.id);
		}
		var isNew = (!element);
		var parent = this._getParentGroupForId(options);
		if(isNew){
			element = document.createElementNS (geomajas.gfx.svg.xmlns.svg, name);
			if(options.id){
				element.setAttribute ("id", options.id);
			}
			if (element.tagName == "image") {
				element.addEventListener("load", this._setLoadingImageFinished, false);
			}
		}
		if(options.style){
			//log.info("updating style of "+options.id);
			var style = options.style;
			//if (!style.symbol) {
				var css = this.styleDecoder.decode(style);
				this._updateAttribute(element, "style", css);
				if (style.fontSize) {
					this._updateAttribute(element, "font-size", style.fontSize);
				}
			//}
		}
		if(options.transform){
			this._updateAttribute(element,"transform",this._getTransform(options.transform));
		}
		if(isNew){		
			parent.appendChild (element);
		}
		return element;
	},
		
	_setLoadingImageFinished : function () {
		geomajasConfig["dispatcher"].setBusyState(false);
	},

	_setLoadingImageStarted : function () {
		geomajasConfig["dispatcher"].setBusyState(true);
	},

	/**
	 * @private
	 */
	_updateAttribute : function (element, name, value){
		var old = element.getAttribute(name);
		if(old == null || value != old){
			element.setAttribute(name,value);
		}
	},

	/**
	 * @private
	 */
	_updateAttributeNS : function (ns, element, name, value){
		var old = element.getAttribute(name);
		if(old == null || value != old){
			if (element.tagname == "image") {
				this._setLoadingImageStarted();
			}
			element.setAttributeNS(ns, name, value);
		}
	},

	/**
	 * @private
	 */
	_getParentGroupForId : function (options) {
		var id = options.id;
		//log.debug("_getParentGroupForId looking for parent for "+id);
		var last = id.lastIndexOf(".");
		if(last >= 0){
			var gId = id.substring(0, last);
			if(gId == this.id){
				//log.debug("_getParentGroupForId found parent mapGroup");
				return this.mapGroup;	
			} else {
				var parent = document.getElementById (gId);
				if(!parent){
					//log.debug("_getParentGroupForId creating parent group "+gId);
					return this._findOrCreateElement("g", {id:gId});
				} else {
					//log.debug("_getParentGroupForId found parent "+gId);
					return parent;
				}
			}
		}			
		else {
			//log.debug("_getParentGroupForId found parent screenGroup");
			return this.screenGroup;
		}
	},

	/**
	 * (Re)sets the world-to-view transformation matrix
	 */
	_getTransform : function (/**2D matrix*/ matrix){
		var transform = "";
		if (true && !(matrix.xx == 1 && matrix.yy == 1)) {
			transform += "scale("+matrix.xx+", "+matrix.yy+")"; // scale first
		}
		if (true) {
			var dx = matrix.dx;
			var dy = matrix.dy;
			if (matrix.xx && matrix.yy) {
				dx /= matrix.xx; // reverse order !!!
				dy /= matrix.yy; // reverse order !!!
			}
			transform += " translate("+dx.toFixed()+", "+dy.toFixed()+")"; // no space between 'translate' en '(' !!!
		}
		return transform;
	 },

	/**
	 * This is here for Google Chrome support, although it works in FF as well.
	 * @private
	 */
	_cloneElement : function (sourceElement) {
	 	if (sourceElement == null) { return; }
	 	if (sourceElement.nodeName == null) {
	 		log.warn("Element has no nodeName!");
	 		return null;
	 	}
	 	if (sourceElement.nodeName == "#text") {
	 		return document.createTextNode(sourceElement.nodeValue);
	 	} else {
	 		var clone = document.createElementNS(geomajas.gfx.svg.xmlns.svg, sourceElement.nodeName);
		 	for (var i=0; i<sourceElement.attributes.length; i++) {
		 		var attribute = sourceElement.attributes.item(i);
		 		if (attribute.value != null && attribute.value.length > 0) {
		 			var atClone = null;
		 			try {
		 				atClone = document.createAttributeNS(attribute.namespaceURI, attribute.name);
		 			} catch (e) {
		 				atClone = document.createAttribute(attribute.name);
		 			}
					atClone.value = attribute.value;
					clone.setAttributeNode(atClone);
		 		}
		 	}
			for (var i = 0; i < sourceElement.childNodes.length; i++) {
				var child = sourceElement.childNodes.item(i);
				var childClone = this._cloneElement(child);
				clone.appendChild (childClone);
			}
			return clone;
	 	}
	 }
});