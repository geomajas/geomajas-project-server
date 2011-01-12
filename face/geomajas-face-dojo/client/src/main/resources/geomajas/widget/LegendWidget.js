dojo.provide("geomajas.widget.LegendWidget");
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
dojo.require("dijit._Widget");
dojo.require("dijit.layout._LayoutWidget");
dojo.requireLocalization("geomajas.widget", "legend");


dojo.require("geomajas.event.common");
dojo.require("geomajas.gfx.common");
dojo.require("geomajas.map.common");

/**
 * @fileoverview This widget shows all the styles of all the layers of a certain map.
 * @author Pieter De Graef
 */
dojo.declare("geomajas.widget.LegendWidget", [dijit.layout.BorderContainer, dijit._Templated], {

	widgetsInTemplate : true,
	templatePath : dojo.moduleUrl("geomajas.widget", "html/Legend.html"),

	graphics : null,
	mapModel : null,    // The map's model. Holds layers.
	textStyle : null,
	polygon : null,
	lineString : null,
	lineHeight : 25,
	letterWidth : 8.5, // nattevingerwerk
	textMargin : 40, // 10 + 25 + 5 zie drawstyle
	
	editor : null,
	automaticResize : false, // Resize the widget automatically?
	checkShapeTypeSize : false, // Should we resize shapetypes when they are too large?
	backgroundColor : "#FFFFFF",
	rasterIcon : "images/world.png",
	lastCount : 0,
	
	// Connection handles:
	layerHandle : null,
	visibleHandle : null,
	
	// collects controllers, etc... to call on destroy
	destroyables : [],

	postCreate : function () {
		this.graphics = new GraphicsContext(this.id+":graphics", 100, 100);
		this.textStyle = new FontStyle("#000000", 14, "Arial, Verdana", "normal", "normal");

		var factory = new GeometryFactory(null, 2);
		this.lineString = factory.createLineString([new Coordinate(12,-20), new Coordinate(5,-15), new Coordinate(15,-5), new Coordinate(25,-12)]);
		var ring = factory.createLinearRing([new Coordinate(5,-18), new Coordinate(5,-5), new Coordinate(22,-5), new Coordinate(22,-18), new Coordinate(5,-18)]);
		this.polygon = factory.createPolygon(ring, null);

		this.editor = new GeometryEditor();

		this.graphics.setBackgroundColor(this.backgroundColor);
		this.destroyables.push(this.graphics);
	},

	resize : function (size){
		this.inherited(arguments);
		this.layout();
	},

	layout: function(){
		if (this.mapModel != null) {
			this._render();
		}

		var innerPane = dojo.byId(this.id+":pane");
		innerPane.style.width = (this._contentBox ? this._contentBox.w : 10)+"px";
		innerPane.style.height = (this._contentBox ? this._contentBox.h : 10)+"px";
	},

	destroy : function (/*Boolean*/ finalize) {
		dojo.disconnect(this.layerHandle);
		dojo.disconnect(this.visibleHandle);
		dojo.forEach(this.destroyables, function(d) {d.destroy();});
		dojo.unsubscribe(this.mapChangeHandle);
		this.inherited(arguments);
	},

	setMapModel : function (mapModel) {
		log.debug("LegendWidget.setMapModel => initializes the legend.");
		this.mapModel = mapModel;

		try {
			this._render();
		} catch (e) {
			log.error ("Rendering of the LegendWidget failed after setting the mapModel. Is it visible at all?");
		}
		if(this.mapModel){
			this.destroyables.push(this.mapModel);
			this.mapChangeHandle = dojo.subscribe(mapModel.getRenderTopic(), this, "_onMapViewChange");
		}
	},

	_onMapViewChange : function(event, feature) {
		if (event == null || event instanceof MapModel || event instanceof LayerTreeNode) {
			if (feature == "all") {
				log.debug("updating legend: " + event + " - " + feature);
				this.layout();
			}
		}
	},
	
	/**
	 * @private
	 */
	_render : function () {
		// First we delete (refresh every time!)
		for (var i=1; i<=this.lastCount; i++) {
			this.graphics.deleteShape(this.id + "geom" + i);
			this.graphics.deleteShape(this.id + "style" + i);
		}
		
		// Then we draw:
		var layers = this.mapModel.getVisibleLayerList();
		var maxWidth = 0;
		var width = 100;
		var height = this.lineHeight;

		if (layers.count == 0) {
			// If no layer is visible, we should say so:
			var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "legend");
			maxWidth = widgetLocale.noLayersVisible.length * this.letterWidth;
			this.graphics.setScreenBox(new Bbox(0,0,100,height));
			var text = new Text(this.id+"style"+count, widgetLocale.noLayersVisible);
			var textX = 10;
			var textY = parseInt(this.lineHeight/2 + this.textStyle.getFontSize()/2);
			text.setPosition(new Coordinate(textX, textY));
			this.graphics.drawText(text, {id:this.id+"style"+count, style:this.textStyle});
			maxWidth = widgetLocale.noLayersVisible.length * this.letterWidth;
			
		} else {
			var count = 0;
			var legendStyleDef = new Array();
			var legendLayerType = new Array();
			
			// collecting items
			for (var i=0; i<layers.count; i++) {
				var layer = layers.item(i);
				if (layer instanceof VectorLayer) {
					var styles = layer.getStyles();
					for (var j=0; j<styles.count; j++) {
						legendStyleDef[count] = styles.item(j);
						legendLayerType[count] = layer.getLayerType();
						width = this.textMargin + legendStyleDef[count].getName().length * this.letterWidth;
						if (width > maxWidth) {
							maxWidth = width;
						}
						count++;
					}
				} else if (layer instanceof RasterLayer) {
					legendStyleDef[count] = new FeatureStyleInfo(null, layer.getLabel(), null, new PictureStyle(layer.getStyle()));
					legendLayerType[count] = layer.getLayerType();
					width = this.textMargin + legendStyleDef[count].getName().length * this.letterWidth;
					if (width > maxWidth) {
						maxWidth = width;
					}
					count++;
				}
			}
		
			// reset size first for IE, also reset graphics first for IE
			height = 10 + count * this.lineHeight + 10;
			if (dojo.isIE)
				this.graphics.setScreenBox(new Bbox(0,0,0,0));
			this.graphics.setScreenBox(new Bbox(0,0,maxWidth,height));
	
			// really painting
			for (var i=0; i < count; i++) {
				this._drawStyle(i + 1, legendStyleDef[i], legendLayerType[i]);
			}
		}

		this.lastCount = count;
	},

	/**
	 * @private
	 */
	_drawStyle : function (count, styleDef, layerType) {
		var translateX = 10;
		var translateY = 10 + count * this.lineHeight;
		if (layerType == geomajas.LayerTypes.LINESTRING || layerType == geomajas.LayerTypes.MULTILINESTRING) {
			var operation = new TranslationOperation(translateX, translateY);
			var geometry = this.lineString.clone();
			this.editor.edit(geometry, operation);
			var style = styleDef.getStyle().clone();
			style.setStrokeWidth("2");
			this.graphics.drawLine(geometry, {id:this.id + "geom" + count, style:style});
		} else if (layerType == geomajas.LayerTypes.POLYGON || layerType == geomajas.LayerTypes.MULTIPOLYGON) {
			var operation = new TranslationOperation(translateX, translateY);
			var geometry = this.polygon.clone();
			this.editor.edit(geometry, operation);
			var style = styleDef.getStyle().clone();
			style.setStrokeWidth("2");
			this.graphics.drawPolygon(geometry, {id:this.id + "geom" + count, style:style});
		} else if (layerType == geomajas.LayerTypes.POINT ||  layerType == geomajas.LayerTypes.MULTIPOINT) {
			var style = styleDef.getStyle().clone();
			if (this.checkShapeTypeSize && style.symbol) {
				if (style.symbol.rect) {
					var max = this.lineHeight - 5;
					if (style.symbol.rect.w > max) {
						style.symbol.rect.w = max;
					}
					if (style.symbol.rect.h > max) {
						style.symbol.rect.h = max;
					}
				} else if (style.symbol.circle) {
					var max = (this.lineHeight / 2) - 5;
					if (style.symbol.circle.r > max) {
						style.symbol.circle.r = max;
					}
				}
			}
			this.graphics.drawShapeType({id:this.id + "geom_" + count + ".style", style:style, transform:{xx:1}});
			style.symbol = null;
			var factory = new GeometryFactory();
			var point = factory.createPoint(new Coordinate(translateX+10, translateY-10));
			this.graphics.drawSymbol(point, {id:this.id + "geom" + count, style:style, styleId:this.id + "geom_" + count + ".style"});
		} else if (layerType == geomajas.LayerTypes.RASTER) {
			var picture = new Picture(this.id + "geom"+count);
			picture.setWidth(18);
			picture.setHeight(18);
			picture.setPosition(new Coordinate(translateX + 4, translateY - 20));
			picture.setStyle(styleDef.getStyle());
			picture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/world.png"));
			this.graphics.drawImage(picture, {id:picture.getId(), style:picture.getStyle()});
		}

		var string = styleDef.getName();
		var text = new Text(this.id + "style" + count, string);
		var textX = translateX + this.lineHeight + 5;
		var textY = parseInt(translateY - this.lineHeight + (this.lineHeight-this.textStyle.getFontSize())*0.5 );
		text.setPosition(new Coordinate(textX, textY));
		this.graphics.drawText(text, {id:this.id + "style" + count, style:this.textStyle});
	},

	// Getters and setters:

	getTextStyle : function () {
		return this.textStyle;
	},

	setTextStyle : function (textStyle) {
		this.textStyle = textStyle;
	},

	getPolygon : function () {
		return this.polygon;
	},

	setPolygon : function (polygon) {
		this.polygon = polygon;
	},

	getLineString : function () {
		return this.lineString;
	},

	setLineString : function (lineString) {
		this.lineString = lineString;
	},

	getBackgroundColor : function () {
		return this.backgroundColor;
	},

	setBackgroundColor : function (backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.graphics.setBackgroundColor(backgroundColor);
	},

	getAutomaticResize : function () {
		return this.automaticResize;
	},

	setAutomaticResize : function (automaticResize) {
		this.automaticResize = automaticResize;
	}
});
