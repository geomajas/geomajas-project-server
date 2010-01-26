dojo.provide("geomajas.gfx.vml.VmlGraphicsContext");
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
dojo.require("geomajas.gfx.vml.VmlGroup");
dojo.require("geomajas.gfx.vml.decoders.VmlPathDecoder");
dojo.require("geomajas.gfx.vml.decoders.VmlStyleDecoder");
dojo.require("geomajas.spatial.transform.WorldViewTransformation");

geomajas.gfx.vml.xmlns = "urn:schemas-microsoft-com:vml";

dojo.extend(GraphicsContext, {

	/**
	 * @class Implementation of the "GraphicsContext" interface for drawing in
	 * VML.
	 * @author Pieter De Graef & Jan De Moerloose
	 *
	 * @constructor
	 * @extends GraphicsContext
	 * 
	 */
	 vmlNode : null,
	 clipNode : null,
	 backgroundGroup : null,
	 mapGroup : null,
	 screenGroup : null,
     styleDecoder : null,
     pathDecoder : null,
     precisionScale : 1.0,
	 
	/**
	 * (Re)sets the world-to-view transformation matrix
	 */
	 setWorldToViewTransformation : function (/**2D matrix*/ matrix){	 	
	 },
	 
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
		//log.debug("drawGroup for :" + options.id);
		if(options.type !== "div"){
			//log.debug("it's vml "+options.id);
			g = this._findOrCreateElement("vml:group",options);				
		} else {
			//log.debug("it's a div "+options.id);
			g = this._findOrCreateElement("div",options);				
			g.style.position="absolute";
		}
		//log.debug("drawGroup for :" + options.id+" done");
	},
	
	/**
	 * Draw a line.
	 */
	drawLine : function (/*Paintable*/line, /* hashtable {id, style, transform,...}*/ options) {
		//log.debug("drawLine for :" + options.id);
		var path = this._findOrCreateElement("vml:shape", options);
		if (line) {
			this._updateAttribute(path, "path", this.pathDecoder.decode(line, options.worldspace));
		}
		else if(options.path){
			this._updateAttribute(path, "path", options.path);
		}
	},
	
	/**
	 * Draw a polygon.
	 */
	drawPolygon : function (/*Paintable*/polygon, /* hashtable {id, style, transform,...}*/ options) {
		//log.debug("drawpolygon for :" + options.id);
		var path = this._findOrCreateElement("vml:shape", options);
		if (polygon) {
			this._updateAttribute(path, "path", this.pathDecoder.decode(polygon, options.worldspace));
		}
		else if(options.path){
			this._updateAttribute(path, "path", options.path);
		}
		this._updateAttribute(path, "fill-rule", "evenodd");
	},
	
	/**
	 * Draw a rectangle.
	 */
	drawRectangle : function (/*Paintable*/rectangle, /* hashtable {id, style, transform,...}*/ options) {
		//log.debug("drawRectangle for :" + options.id);
		var rect = this._findOrCreateElement("vml:rect", options);
		this._updateAttribute(rect, "left", rectangle.getPosition().getX());
		this._updateAttribute(rect, "top", rectangle.getPosition().getY());
		this._updateAttribute(rect, "width", rectangle.getWidth());
		this._updateAttribute(rect, "height", rectangle.getHeight());
	},
	
	/**
	 * Draw a circle.
	 */
	drawCircle : function (/*Paintable*/circle, /* hashtable {id, style, transform,...}*/ options) {
		//log.debug("drawCircle for :" + options.id);
		var circleElement = this._findOrCreateElement("vml:oval", options);
		this._updateAttribute(circleElement, "left", circle.getPosition().getX()-circle.getR());
		this._updateAttribute(circleElement, "top", circle.getPosition().getY()-circle.getR());
		this._updateAttribute(circleElement, "width", 2*circle.getR());
		this._updateAttribute(circleElement, "height", 2*circle.getR());
	},
	
	/**
	 * Draw a symbol/point object.
	 */
	drawSymbol : function (/*Paintable*/symbol, /* hashtable {id, style, transform,...}*/ options) {
		var point = this._findOrCreateElement("vml:shape", options);

		if (options.styleId != null) {
			point.setAttribute("type", "#" + options.styleId);
		} else {
			point.setAttribute("type", "#"+point.parentNode.id+".style");
		}

		if (symbol) {
			var coordinates = symbol.getCoordinates();
			this._updateAttribute(point, "left", coordinates[0].getX());
			this._updateAttribute(point, "top", coordinates[0].getY());
			//this._updateAttribute(point, "adj", "'"+coordinates[0].getX()+","+coordinates[0].getY()+"'");
		}
	},
	
	/**
	 * Draw a type (def/symbol for svg, shapetype for vml) 
	 */
	drawShapeType : function (/* hashtable {id}*/ options) {
		//log.info("drawing shape type "+options.id);		
		try {
			var symbol = options.style.symbol;
			if(!symbol){
				return;
			}
			var isNew = (document.getElementById (options.id) ? false : true);
			var shapeType = this._findOrCreateElement("vml:shapetype",{ id : options.id, style : options.style});
			shapeType.style.visibility = "hidden";
			if(isNew){
				//log.info("drawing shape type : prepraring formulas "+options.id);		
				var formulas = document.createElement("vml:formulas");
				shapeType.appendChild(formulas);
				// prepare 4 formulas (problem: how to extend this for complex geometries????)
				for(var i = 0; i < 4; i++){
					var f = document.createElement("vml:f");
					formulas.appendChild(f);
				}
//				for(var word in symbol){
//					log.info("drawing shape type : "+word+","+symbol[word]);
//				}
			}
			if(symbol.rect){
				//log.info("drawing shape type : rect ");		
				var formulas = shapeType.getElementsByTagName("f");	
				//log.info("drawing shape type : found formulas "+formulas.length+" in "+shapeType.id);
				
				var width = symbol.rect.w;
				var height = symbol.rect.h;
				if(options.transform && options.transform.xx != 0){
					//log.info("drawing shape type : scaling");
					var scale = options.transform.xx;
					width = (width * this.precisionScale / scale).toFixed();
					height = (height * this.precisionScale / scale).toFixed();
					//log.info("drawing shape type : scaling");
				}
				
				//log.info("drawing shape type : width "+width);
				//log.info("drawing shape type : height "+height);
				var f = document.createElement("vml:f");
				//log.info("drawing shape type : created formula "+f);
				f.eqn = "sum #0 "+"0 "+parseInt(width/2);			
				//log.info("drawing shape type : made formula "+f.eqn);
				formulas[0].parentNode.replaceChild(f,formulas[0]);
				//log.info("drawing shape type : replacing done");
				
				f = document.createElement("vml:f");
				f.eqn = "sum #1 "+"0 "+parseInt(height/2);			
				//log.info("drawing shape type : made formula "+f.eqn);
				formulas[1].parentNode.replaceChild(f,formulas[1]);

				f = document.createElement("vml:f");
				f.eqn = "sum #0 "+parseInt(width/2)+" 0";			
				//log.info("drawing shape type : made formula "+f.eqn);
				formulas[2].parentNode.replaceChild(f,formulas[2]);

				f = document.createElement("vml:f");
				f.eqn = "sum #1 "+parseInt(height/2)+" 0";			
				//log.info("drawing shape type : made formula "+f.eqn);
				formulas[3].parentNode.replaceChild(f,formulas[3]);
				
				shapeType.setAttribute("path", "m@0@1 l@2@1 @2@3 @0@3xe");
				//log.info("drawing shape type : "+shapeType.getAttribute("path"));
			} else if(symbol.circle){
				log.info("drawing shape type : circle ");
				var formulas = shapeType.getElementsByTagName("f");	
				//log.info("drawing shape type : found formulas "+formulas.length+" in "+shapeType.id);
						
				var f = document.createElement("vml:f");
				f.eqn = "sum #0 0 0";			
				formulas[0].parentNode.replaceChild(f,formulas[0]);
				
				f = document.createElement("vml:f");
				f.eqn = "sum #1 0 0";			
				formulas[0].parentNode.replaceChild(f,formulas[1]);
				var radius = symbol.circle.r;
				if(options.transform && options.transform.xx != 0){
					//rotate (scale only !!!!)
					var scale = options.transform.xx;
					radius = radius * this.precisionScale /scale;
					radius = radius.toFixed();
				}
				shapeType.setAttribute("path", "al @0 @1 "+radius+" "+radius+" 0 23592600x");
				//log.info("drawing shape type : "+shapeType.getAttribute("path"));
			}
		} catch(e){
			for(word in e){
				log.error(word+":"+e[word])
			}
			log.error("drawSymbol : could not add shape type");
		}
	},
	
	
	/**
	 * Draw a div image (from gif/jpg/png/...).
	 */
	_drawImageDiv : function (/*Paintable*/image, /* hashtable {id, style, transform,...}*/ options) {
		//log.debug("drawImage for :" + options.id);
		var divElement = this._findOrCreateElement("div", options);
		divElement.style.position="absolute";
		this._updateAttribute(divElement, "left", image.getPosition().getX());
		this._updateAttribute(divElement, "top", image.getPosition().getY());
		this._updateAttribute(divElement, "width", image.getWidth());
		this._updateAttribute(divElement, "height", image.getHeight());
		this._updateAttribute(divElement, "border", "none");
		this._updateAttribute(divElement, "display", "inline");
		
		var imageElement = this._findOrCreateElement("img", {id : options.id+".img"});
		log.debug ("SRC before = "+image.getHref());
		imageElement.src = image.getHref();
		log.debug ("SRC after = "+imageElement.src);
		
		this._updateAttribute(imageElement, "width", "100%");
		this._updateAttribute(imageElement, "height", "100%");
		if(options.style instanceof PictureStyle){
			var opacity = options.style.getOpacity();
			if(opacity != null){
				try {
				  imageElement.style.filter = "alpha(opacity="+ 100*opacity+")";
				} catch(e) {
					//log.error("image opacity not supported by this browser !!!!");
				}			
			}
		}
	},
	
	/**
	 * Draw an image (from gif/jpg/png/...).
	 */
	drawImage : function (/*Paintable*/image, /* hashtable {id, style, transform,...}*/ options) {
		//log.debug("drawImage for :" + options.id);
		if(options.type !== "div"){
			// If there is a transform object, create a group first:
			if (options.transform) {
				var group = this._findOrCreateElement("vml:group", options);
				group.style.position = "absolute";
				group.style.width=image.getWidth()+"px";
				group.style.height=image.getHeight()+"px";
				var img = this._findOrCreateElement("vml:image", {id:options.id+".img"});
				img.style.width = "100%";
				img.style.height = "100%";
				img.src = image.getHref();
			} else {
				var img = this._findOrCreateElement("vml:image", options);
				img.style.position="absolute";
				img.style.top=image.getPosition().getY()+"px";
				img.style.left=image.getPosition().getX()+"px";
				img.style.width=image.getWidth()+"px";
				img.style.height=image.getHeight()+"px";
				img.src = image.getHref();
				if(options.style instanceof PictureStyle){
					var opacity = options.style.getOpacity();
					if(opacity != null){
						try {
							img.style.filter = "alpha(opacity="+ 100*opacity+")";
						} catch(e){
						//log.error("image opacity not supported by this browser !!!!");
						}			
					}
				}
			}
		} else {
			this._drawImageDiv(image, options);
		}
	},

	/**
	 * Draw some text on the screen.
	 */
	drawText : function (/*Paintable*/text, /* hashtable {id, style, transform,...}*/ options) {
		var textbox = this._findOrCreateElement("vml:textbox", options);
		if (textbox) {
			this._updateAttribute(textbox, "left", text.getPosition().getX());
			this._updateAttribute(textbox, "top", text.getPosition().getY());

			if (options.style != null && options.style instanceof FontStyle) {
				textbox.style.fontSize = options.style.getFontSize();
				textbox.style.color = options.style.getFillColor();
				textbox.style.fontFamily = options.style.getFontFamily();
				textbox.style.fontWeight = options.style.getFontWeight();
				textbox.style.fontStyle = options.style.getFontStyle();
			}
			var textWidth = this.width - text.getPosition().getX();
			if (textWidth <= 0) {
				textWidth = 10;
			}
			this._updateAttribute(textbox, "width", textWidth);

			while (textbox.firstChild) {
				textbox.removeChild(textbox.firstChild);
			}
			var textNode = document.createTextNode(text.getText());
			textbox.appendChild(textNode);
		}
	},

	/**
	 * Return the length in pixels of a given {@link Text} object.
	 * @param text A paintable {@link Text} object.
	 * @return The size in pixels.
	 */
	computeTextLength : function (/*Paintable*/text) {
		return 100;
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
    	if (typeof CollectGarbage != 'undefined') {
    		CollectGarbage();
    	}
	},

	/**
	 * Set the background color.
	 */
	setBackgroundColor : function (/*String*/color) {
		this.backgroundGroup.style.backgroundColor = color;
	},

	/**
	 * Hide something with the given id
	 */
	hide : function (/*String*/id) {
		//log.info("hiding "+id);
		var element = document.getElementById (id);
		if (element && element.style) {
			element.style.visibility = "hidden";
		} 
	},
	
	/**
	 * Unhide something with the given id
	 */
	unhide : function (/*String*/id) {
		//log.info("unhiding "+id);
		var element = document.getElementById (id);
		if (element && element.style) {
			element.style.visibility = "inherit";
		}
	},
	
	
	/**
	 * cleans up to avoid for memory leaks
	 */
	destroy : function () {
		dojo._destroyElement(this.vmlNode);
		delete this.vmlNode;
		dojo._destroyElement(this.clipNode);
		delete this.clipNode;
		dojo._destroyElement(this.backgroundGroup);
		delete this.backgroundGroup;
		dojo._destroyElement(this.mapGroup);
		delete this.mapGroup;
		dojo._destroyElement(this.screenGroup);
		delete this.screenGroup;
	},

	
	/**
	 * returns the root DOM node of this context
	 */
	getNode : function () {
		return this.vmlNode;
	},

	/**
	 * returns the xml representation of the root node as a string
	 */
	getXml : function () {
		return this.clipNode.outerHTML;		
	},
	
	_getXml : function(id){
		var element = document.getElementById (id);
		return element.outerHTML;
	},
	
	/**
	 * Set a specific cursor type.
	 */
	setCursor : function (/*String*/cursor, elementID) {
		if (elementID == null) {
			this.node.style.cursor=cursor;
		} else {
			var element = document.getElementById(elementID);
			element.style.cursor=cursor;
		}
	},
	
	setPrecision : function (precision) {
		this.precision = precision;
		this.precisionScale = Math.pow(10.0, this.precision);
	},
	
	setBackgroundColor : function (/*String*/backgroundColor) {
		this.backgroundGroup.style.backgroundColor = backgroundColor;
	},

	/**
	 * Browser-specific initialization
	 */
	_afterInit : function(){
		try  {
			// Add VML style definition:
	    	var stl = document.createStyleSheet();
	   	 	stl.addRule("vml\\:*", "behavior: url(#default#VML);");
	
	       	document.namespaces.add("vml", geomajas.gfx.vml.xmlns);
			this.pathDecoder = new VmlPathDecoder();
			this.styleDecoder = new VmlStyleDecoder();
			
			this.clipNode = document.createElement("div");
			this.clipNode.style.width = this.width+"px";
			this.clipNode.style.height = this.height+"px";
			this.clipNode.style.left = this.bbox.x+"px";
			this.clipNode.style.top = this.bbox.y+"px";
			this.clipNode.style.clip = "rect(0 "+(this.width)+"px "+(this.height)+"px 0)";
			this.clipNode.style.overflow = "hidden";
			this.clipNode.style.position = "absolute";
			this.node.appendChild(this.clipNode);
			
			this.vmlNode = document.createElement("div");
			this.vmlNode.id = this.id+"_root";
			// JDM: screen shapes disappear if this is absolute !!!!
			//this.vmlNode.style.position = "absolute";
			this.clipNode.appendChild(this.vmlNode);
			
			// Background		
			this.backgroundGroup = document.createElement("div");
			this.backgroundGroup.id = this.id+"_background";
			this.backgroundGroup.style.width = this.width+"px";
			this.backgroundGroup.style.height = this.height+"px";
			this.backgroundGroup.style.position = "absolute";
			this.vmlNode.appendChild(this.backgroundGroup);

			// Map group:
			this.mapGroup = document.createElement("div");
			this.mapGroup.id = this.id+"_map";
			this.mapGroup.style.width = this.width+"px";
			this.mapGroup.style.height = this.height+"px";
			this.mapGroup.style.position = "absolute";
			this.mapGroup.id = this.id+"_map";
			this.vmlNode.appendChild(this.mapGroup);

			// Screen group:
			this.screenGroup = document.createElement("vml:group");
			this.screenGroup.id = this.id+"_screen";
			this.screenGroup.style.position = "absolute";
			this.screenGroup.coordsize = this.width+" "+this.height;			
			this.screenGroup.style.width = this.width+"px";			
			this.screenGroup.style.height = this.height+"px";	
			
			// Place something top-left the screengroup, to make sure it is placed correctly... pitiful indeed.
			var origin = document.createElement("vml:rect");
			origin.setAttribute("id", this.id+"_screen.origin_rect");
			origin.style.position = "absolute";
			origin.style.top = 0;
			origin.style.left = 0;
			this.screenGroup.appendChild(origin);

			this.vmlNode.appendChild(this.screenGroup);
		}
		catch(e){
			for(var word in e){
				log.error("_afterInit failed "+word+":"+e[word]);
			}
		}
	},
	
	/**
	 * sets the view size
	 */
	 _afterResize : function (){
	 	// clip the outer div
		this.clipNode.style.width = this.width;
		this.clipNode.style.height = this.height;
		this.clipNode.style.left = this.bbox.x;
		this.clipNode.style.top = this.bbox.y;
		this.clipNode.style.clip = "rect(0 "+(this.width)+"px "+(this.height)+"px 0)";

		this.backgroundGroup.style.width = this.width+"px";
		this.backgroundGroup.style.height = this.height+"px";

		this.mapGroup.style.width = this.width+"px";
		this.mapGroup.style.height = this.height+"px";
		
		this.screenGroup.coordsize = this.width+" "+this.height;			
		this.screenGroup.style.width = this.width+"px";			
		this.screenGroup.style.height = this.height+"px";
	 },
	
	_findOrCreateData : function(data, /* hashtable {id, style, transform,...}*/ options) {
		var g = document.getElementById (options.id);
		if(!g){
		    g = this._findOrCreateElement("vml:group",options);	
 			g.innerHTML = data;
		}
	},
	
	_findOrCreateElement : function (name, /* hashtable {id, style, transform,...}*/ options){
		if(!options.id){
			return null;
		}
		var element = null;
		var isNew = false;
		if(options.id == this.id) {
			element = this.mapGroup;
		} else {
			element = document.getElementById (options.id);
		}
		var parent = this._getParentGroupForId(options);
		if(!element){
			element = document.createElement (name);
			if(options.id){
				element.id = options.id;
			}							
			parent.appendChild (element);
			isNew = true;
		}
		if(name == "vml:group"){
			//log.info("updating vml group");
			this._updateVmlGroup(element, parent, options);
		} else if(name.match("vml:")) {
			//log.info("updating vml element");
			if(isNew){
				var stroke = document.createElement ("vml:stroke");
				var fill = document.createElement ("vml:fill");
				element.appendChild(stroke);
				element.appendChild(fill);
			}
			if(name == "vml:shape") {
				//log.info("updating vml shape");
				this._updateVmlShape(element, parent, options);
			} else {
				//log.info("updating vml other");
				this._updateVmlOther(element, parent, options);		
			}
		} else if(name.match("div")){
			//log.info("updating html div");
			this._updateHtmlDiv(element, parent, options);
		} else {
			//log.info("updating html other");
			this._updateHtmlOther(element, parent, options);
		}
		return element;
	},

	_updateVmlGroup : function (element, parent, options){	
		//log.info("_updateVmlGroup");
		// size
		if(options.width){
			element.coordsize = options.width+" "+options.height;			
			element.style.width = options.width+"px";			
			element.style.height = options.height+"px";	
			element.style.position = "absolute";	
		} else {
			element.coordsize = this.width+" "+this.height;			
			element.style.width = "100%";			
			element.style.height = "100%";	
			element.style.position = "absolute";	
		}
		// style : create a shapetype for the children
		if(options.style && !options.style.symbol){	
			//log.info("updating shape type "+options.id);		
			var shape = this._findOrCreateElement("vml:shapetype",{ id : options.id+".style", style : options.style});
			shape.style.visibility = "hidden";
			
			if(options.style instanceof FontStyle) {
				element.style.color = options.style.fillColor;
				element.style.fontSize = options.style.fontSize;
				element.style.fontFamily = options.style.fontFamily;
				element.style.fontWeight = options.style.fontWeight;
				element.style.fontStyle = options.style.fontStyle;
			}
		}
		// transform : translate only !!!
		if(options.transform){
			// a very bad way to distinguish between translation and scaling !!!
			if(options.transform.dx != 0 || options.transform.dy != 0){
				//log.info("translating "+element.id+" to "+options.transform.dx+"px,"+options.transform.dy+"px");
				element.style.left = options.transform.dx.toFixed()+"px";
				element.style.top = options.transform.dy.toFixed()+"px";
				//log.info("setting coordsize for "+options.id);
			} 
		}
		//log.info("_updateVmlGroup done");
	},
	
	_updateVmlShape : function (element, parent, options){
		// size	
		if(parent.coordsize){
			//log.info("element "+options.id+" inherits coordsize "+parent.coordsize);
			element.coordsize = parent.coordsize;			
			element.style.width = "100%";			
			element.style.height = "100%";	
			element.style.position = "absolute";	
		} else {
			element.coordsize = this.width+" "+this.height;			
			//log.info("element "+options.id+" gets coordsize "+element.coordsize);
			element.style.width = this.width;			
			element.style.height = this.height;	
			element.style.position = "absolute";	
		}
		// style
		var style = options.style;
		// try to copy the parent style first
		var gId = this._getStyleId(options.id);
		//log.info("_updateVmlShape : fetching parent style "+gId);
		var shapetype = document.getElementById (gId);	
		if(shapetype){
			//log.info("copying shape style from parent");
			this._copyVmlStyle(shapetype, element);
		}			
		// possibly override with own style
		if(style instanceof ShapeStyle) {
			//log.info("updating own shape style");
			this._updateVmlStyle(element, style);	
		} else if (style instanceof FontStyle) {
			var css = this.styleDecoder.decode(style);
			this._updateAttribute(element, "style", css);
		}
		//log.info("done updating shape style");
		// transform N/A
	},
	
	_updateVmlOther : function (element, parent, options){
		//log.info("_updateVmlOther : start");
		// style
		element.style.position = "absolute";
		var style = options.style;
		// try to copy the parent style first
		var gId = this._getStyleId(options.id);
		//prevent style copy to self
		if (gId != options.id) {			
			var shapetype = document.getElementById (gId);	
			if(shapetype) {
				this._copyVmlStyle(shapetype, element);
			} else {
				
			}		
		}	
		// possibly override with own style
		//log.info("style = "+typeof(style));
		if(style instanceof ShapeStyle) {
			//log.info("style = shape");
			this._updateVmlStyle(element, style);	
		} else if (style instanceof FontStyle) {
			var css = this.styleDecoder.decode(style);
			this._updateAttribute(element, "style", css);
		}
		//log.info("_updateVmlOther : done");
		// transform N/A
	},
	
	_updateHtmlDiv : function (element, parent, options){
		//log.info("updating html div "+element.id);
		// size
		element.style.position = "absolute";
		// style
		// transform : translation only !!!!
		if(options.transform){
			// a very bad way to distinguish between translation and scaling !!!
			if(options.transform.dx != 0 || options.transform.dy != 0){
				//translate
				element.style.width = this.width+"px";
				element.style.height = this.height+"px";
				//log.info("translating "+element.id+" to "+options.transform.dx+"px,"+options.transform.dy+"px");
				element.style.left = options.transform.dx.toFixed()+"px";
				element.style.top = options.transform.dy.toFixed()+"px";
				//log.info("setting coordsize for "+options.id);
				element.coordsize = this.width+" "+ this.height;
			} 
		}
		//log.info("updating html div really done");
	},
	
	_updateHtmlOther : function (element, parent, options){
		// size
		element.style.position = "absolute";
		// style
		if(options.style){
			var css = this.styleDecoder.decode(options.style);
		}
		this._updateAttribute(element, "style", css);					
		// transform N/A
	},
	
	_updateVmlStyle : function (element, style){
		// size
		element.style.position = "absolute";
		if (style instanceof FontStyle) {
			if (style.fontSize && style.fontSize !== "") {
				element.style.fontSize = style.fontSize;
			}
			if (style.fillColor && style.fillColor !== "") {
				element.style.color = style.fillColor;
			}
			if (style.fontFamily && style.fontFamily !== "") {
				element.style.fontFamily = style.fontFamily;
			}
			if (style.fontWeight && style.fontWeight !== "") {
				element.style.fontWeight = style.fontWeight;
			}
			if (style.fontStyle && style.fontStyle !== "") {
				element.style.fontStyle = style.fontStyle;
			}
			return;
		}
		if (style.fillColor && style.fillColor !== "") {
			//log.info("setting fill color for "+element.id+","+style.fillColor);
			element.filled = true;
			if (element.fill == null) {
				element.fill = {};
			}
			element.fill.color = style.fillColor;
		} 
		if (style.fillOpacity && style.fillOpacity !== "") {
			if (element.fill == null) {
				element.fill = {};
			}
			//log.info("setting stroke opacity for "+element.id+","+style.fillOpacity);
			element.fill.opacity = style.fillOpacity;
		}
		if (style.strokeColor && style.strokeColor !== "") {
			//log.info("setting stroke color for "+element.id+","+style.strokeColor);
			element.stroked = true;
			if (element.stroke == null) {
				element.stroke = {};
			}
			element.stroke.color = style.strokeColor;
		} 
		if (style.strokeOpacity && style.strokeOpacity !== "") {
			if (element.stroke == null) {
				element.stroke = {};
			}
			//log.info("setting stroke opacity for "+element.id+","+style.strokeOpacity);
			element.stroke.opacity = style.strokeOpacity;
		}
		if (style.strokeWidth && style.strokeWidth !== "") {			
			element.strokeweight = style.strokeWidth;
		}
		if (style.dashArray && style.dashArray !== "") {
			// todo
		}
	},

	// copy the vml style from an existing element to another
	_copyVmlStyle : function (from, to){
		log.info("_copyVmlStyle("+ from.id +","+to.id+")");
		// size
		to.style.position = "absolute";
		if (from.style.fontSize && from.style.fontSize !== "") {
			to.style.fontSize = from.style.fontSize;
		}
		if (from.style.color && from.style.color !== "") {
			to.style.color = from.style.color;
		}
		if (from.style.fontFamily && from.style.fontFamily !== "") {
			to.style.fontFamily = from.style.fontFamily;
		}
		if (from.style.fontWeight && from.style.fontWeight !== "") {
			to.style.fontWeight = from.style.fontWeight;
		}
		if (from.style.fontStyle && from.style.fontStyle !== "") {
			to.style.fontStyle = from.style.fontStyle;
		}
		
		to.filled = from.filled;
		if (from.fill != null) {
			if (to.fill == null) {
				to.fill = from.fill;
			}
			to.fill.color = from.fill.color;
			to.fill.opacity = from.fill.opacity;
		}
		to.stroked = from.stroked;
		if (from.stroke != null) {
			if (to.stroke == null) {
				to.stroke = from.stroke;
			}
			to.stroke.color = from.stroke.color;
			to.stroke.opacity = from.stroke.opacity;
		}
		to.strokeweight = from.strokeweight;
	},
		
	_updateAttribute : function (element, name, value){
		if(name == "width") {
			var old = element.style.width;
			if(old == null || value !== old){
				element.style.width = value;
			}
		} else if(name == "height") {
			var old = element.style.height;
			if(old == null || value !== old){
				element.style.height = value;
			}
		}  else if(name == "top") {
			var old = element.style.top;
			if(old == null || value !== old){
				element.style.top = value;
			}
		}  else if(name == "left") {
			var old = element.style.left;
			if(old == null || value !== old){
				element.style.left = value;
			}
		}  else {
			var old = element.getAttribute(name);
			if(old == null || value !== old){
				element.setAttribute(name,value);
			}
		}
	},

	/**
	 * @private
	 */
	_getParentGroupForId : function (options) {
		var id = options.id;
		var last = id.lastIndexOf(".");
		if(last >= 0){
			var gId = id.substring(0, last);
			if(gId == this.id){
				return this.mapGroup;	
			} else {
				var parent = document.getElementById (gId);
				if(!parent){
					if(options.type !== "div"){
						var group = this._findOrCreateElement("vml:group", {id:gId});
					} else {
						var group = this._findOrCreateElement("div", {id:gId});
					}
					group.style.position="absolute";
					return group;
				} else {
					return parent;
				}
			}
		}			
		else {
			return this.screenGroup;
		}
	},
	
	/**
	 * @private
	 */
	_getStyleId : function (id) {
		var ids = id.split(".");
		var styleId = ids[0];
		for(var i = 1; i < ids.length-1; i++){
			styleId += "."+ids[i];
		}
		return styleId+".style";
	},
	
	
	/**
	 * @private
	 */
	_parseFragment : function (data) {
		// todo
	}

});
