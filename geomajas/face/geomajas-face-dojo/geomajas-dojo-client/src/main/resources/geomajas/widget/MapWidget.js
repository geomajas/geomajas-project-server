dojo.provide("geomajas.widget.MapWidget");
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
dojo.require("dijit._Widget");
dojo.require("dijit.layout._LayoutWidget");

dojo.require("geomajas._base");
dojo.require("geomajas.event.common");
dojo.require("geomajas.gfx.common");
dojo.require("geomajas.map.common");
dojo.require("geomajas.controller.SimplePanController");
dojo.require("geomajas.controller.OverviewRectController");

/**
 * This is the main widget representing a map...
 * @author Pieter De Graef
 */
dojo.declare(
	"geomajas.widget.MapWidget",
	dijit.layout._LayoutWidget,
	{
		mapModel : null,    // The map's model. Holds layers.
		mapView : null,     // The map's viewing object. Determines the visible area.
		mouseListenerSubject : null, // MouseEvent connector.
		currentController : null, // The currently active MouseListener. Only one at a time allowed!

		scaleUtil : null,
		unitType : "METRIC",
		dynamicOverview : true,

		// Graphical objects:
		renderTopic : null,
		painterVisitor : null, // visites all painter, and gives them the right objects to paint.
		graphics : null,  // SvgGraphicsContext.

		// Graphical config stuff:
		backgroundColor : "#FFFFFF",
		lineSelectStyle : new ShapeStyle("yellow", null, "yellow", null, null, null, null),
		pointSelectStyle : new ShapeStyle("yellow", null, "yellow", null, null, null, null),
		polygonSelectStyle : new ShapeStyle("yellow", null,"yellow", null, null, null,null),
		scaleStyle : new FontStyle("#000000", 10, "Courier New, Arial, Verdana", "normal", "normal"),
		
		// Handlers:
		offsetHandler : null,
		renderTopicHandler : null,
		scaleHandler : null,
		selectionHandle : null,
		editingHandle : null,
		
		// collects controllers, etc... to call on destroy
		destroyables : [],
		
		postCreate : function () {
			log.info("MapWidget : postCreate");
			this.renderTopic = this.id + "/render";
			this.mapView = new MapView (this.renderTopic);
			this.graphics = new GraphicsContext(this.id, 100, 100);
			this.graphics.setBackgroundColor(this.backgroundColor);
			this.scaleUtil = new ScaleUtil();
			this.painterVisitor = new PainterVisitor(this.graphics);
			// floating panes can move without render(), capture this event before it gets to the map !!!
			this.offsetHandler = this.connect(this.graphics.getNode(), "onmousedown", "updateOffset");

			this.mouseListenerSubject = new MouseListenerSubject (this.graphics.getNode(), 30);
			this.mouseScrollListenerSubject = new MouseScrollListenerSubject (this.graphics.getNode());
			this.mouseScrollListenerSubject.addListener(new ZoomOnScrollController(this.mapView, 2, 0.5));
			this.renderTopicHandler = dojo.subscribe(this.renderTopic, this, "render");
			this.addPainter("MapModel", new MapModelPainter(this.mapView));
			this.addPainter("VectorLayer", new VectorLayerPainter(this.mapView));
			this.addPainter("Feature", new FeaturePainter(this, this.mapView));
			this.addPainter("Tile", new TilePainter(this.mapView));
			this.addPainter("RenderedTile", new RenderedTilePainter(this.mapView));
			this.addPainter("LabeledTile", new LabeledTilePainter(this.mapView));
			this.addPainter("RasterLayer", new RasterLayerPainter(this.mapView));
			this.addPainter("RasterImage", new RasterImagePainter(this.mapView));
			this.addPainter("RasterNode", new RasterNodePainter());
			this.addPainter("Line", new DefaultPaintablePainter());
			this.addPainter("PaintableGeometry", new DefaultPaintablePainter());
			this.addPainter("Circle", new CirclePainter());
			this.addPainter("Rectangle", new RectanglePainter());
			this.addPainter("Picture", new ImagePainter());
			this.addPainter("Text", new TextPainter());
			this.addPainter("FeatureTransaction", new FeatureTransactionPainter(this.mapView));
			this.addPainter("BaseComponent", new BaseComponentPainter(this.mapView));
			this.addPainter("LegendComponent", new BaseComponentPainter(this.mapView));
			this.addPainter("LabelComponent", new BaseComponentPainter(this.mapView));
			this.addPainter("MapComponent", new BaseComponentPainter(this.mapView));
			this.addPainter("PageComponent", new PageComponentPainter(this.mapView));
			this.addPainter("ViewPortComponent", new ViewPortComponentPainter(this.mapView));

			// Propagation policy:
//			if (dojo.isIE) {
				this.focus = true;
				this.mouseListenerSubject.setStopPropagation(true);
//			} else {
//				this.focus = false;
//				this.mouseListenerSubject.setStopPropagation(false);
//			}
			this.destroyables.push(this.graphics);
			this.destroyables.push(this.mouseListenerSubject);
			this.destroyables.push(this.mouseScrollListenerSubject);
			log.info("MapWidget : postCreate done");
		},
			
		onFocus : function () { // Only called in IE
			this.mouseListenerSubject.setStopPropagation(true);
		},
		
		onBlur : function () { // Only called in IE
			this.mouseListenerSubject.setStopPropagation(false);
		},
		
		destroy: function(/*Boolean*/ finalize){
			// summary:
			//		stub function. Override to implement custom widget tear-down
			//		behavior.
			dojo.unsubscribe(this.renderTopicHandler);
			dojo.unsubscribe(this.selectionHandle);
			dojo.unsubscribe(this.editingHandle);
			this.disableScaleBar();
			this.disablePanButtons();
			dojo.forEach(this.destroyables, function(d) {d.destroy();});
			this.inherited(arguments);
		},

		attachListenerToElement : function (id, listener, stopPropagation) {
			var element = this.graphics.getElementById(id);
			if (element) {
				if (listener instanceof MouseListener) {
					var subject = new MouseListenerSubject(element, 30);
					subject.setStopPropagation(stopPropagation);
					subject.addListener(listener);
				}
				// else: keylistener, ???
			}
		},

		setController : function (/*EventListener*/listener) {
			log.info("MapWidget.setController(" + (listener == null ? "null" : listener.getName()) +")");
			var oldController = this.currentController;
			if (oldController != null) {
				oldController.onDeactivate();
				this.mouseListenerSubject.removeListener(oldController);
				this.currentController = null;
			}
			if(listener != null){
			this.currentController = listener;
			this.currentController.onActivate();
				this.mouseListenerSubject.addListener(this.currentController);
			}
			this.onSetController(oldController, this.currentController);
		},

		/**
		 * event hookup function;
		 */
		onSetController : function (oldController, newController) {			
		},

		addPainter : function (/*String*/clazz, /*Painter*/painter) {
			this.painterVisitor.registerPainter (clazz, painter);
		},

		removePainter : function (/*String*/clazz, /*Painter*/painter) {
			this.painterVisitor.unregisterPainter (clazz, painter);
		},

		isLoaded : function () {
			return true;
		},

		/**
		 * Overridden to adjust the graphics context and map view
		 */
		layout: function(){
			this.mapWidth = this._contentBox.w;
			this.mapHeight = this._contentBox.h;
			this.graphics.setScreenBox(new Bbox(
				this._contentBox.l,this._contentBox.t,
				this._contentBox.w,this._contentBox.h));
			this.mapView.setSize (this.mapWidth, this.mapHeight);

			if (this.scaleBar != null) {
				this.disableScaleBar();
				this.enableScaleBar();
			}
			if (this.upPicture != null) {
				this.disablePanButtons();
				this.enablePanButtons();
			}
		},

		render : function (object, status) {
			try {
				if(this.mapModel == null){
					log.warn("MapWidget.render : map model not ready, on "+this.id);
					return;
				}
				if(object == null) {
					object = this.mapModel;
					this.updateOffset();				
				}
				if(!object.getId){
					log.error("MapWidget.render : missing object id for "+object.declaredClass);
					return;
				}
				if(status == "delete"){
					var painters = this.painterVisitor.getPaintersForObject(object);
					for (var i=0; i<painters.count; i++) {
						log.info("deleting object "+object.getId());
						painters.item(i).deleteShape(object, this.graphics);
					}
				} else {
					if(status == "all"){
						object.accept(this.painterVisitor, this.mapView.getCurrentBbox().clone(), true);
					} else if(status == "update") {
						object.accept(this.painterVisitor, this.mapView.getCurrentBbox().clone(), false);
					}
				}
				//log.fatal("inner xml = "+this.graphics.getXml());
			}
			catch(e){
				for(var word in e){
					log.error("rendering failed");
					log.error(word+":"+e[word]);
				}
			}
		},
		
		updateOffset : function () {
			//recalculate the offset for sizable map window, so the event position will be correct
			var c = new Coordinate(
					this._findposx(this.domNode) + 1,
					this._findposy(this.domNode) + 1
			);
			this.mouseListenerSubject.setOffset(c);
		},


		// When overview-map:
		targetMapId : null,
		targetRect : null,
		setOverviewReference : function (targetMapId) {
			this.targetMapId = targetMapId;
			var targetMap = dijit.byId (targetMapId);
			var targetView = targetMap.getMapView();
			dojo.connect (targetMap, "render", this, "_onTargetMapRender");
			dojo.connect (this, "layout", this, "_onTargetMapRender");
			this._onTargetMapRender(null);
			this.mouseScrollListenerSubject.removeListener(new ZoomOnScrollController(this.mapView,0.5));
		},

		/**
		 * @private
		 */
		_onTargetMapRender : function (object, event) {
			if (object || event == "update") {
				return;
			}
			var targetMap = dijit.byId (this.targetMapId);
			
			// MaxExtents:
			var maxBounds = this.mapView.getMaxBounds();
			if (maxBounds != null) {
				var transform = new WorldViewTransformation(this.mapView);
				maxBounds = transform.worldBoundsToView(maxBounds);
				var maxExtent = new Rectangle(this.id + "maxExtent");
				maxExtent.setPosition(maxBounds.getOrigin());
				maxExtent.setWidth (maxBounds.getWidth());
				maxExtent.setHeight (maxBounds.getHeight());
				maxExtent.setStyle(new ShapeStyle("#668822", "0", "#668822", "1", "2", null, null));
				dojo.publish(this.renderTopic, [maxExtent, "update"]);
			}
						
			// zoom to new bbox if necessary
			var bbox = targetMap.getMapView().getCurrentBbox();
						
			// zoom to new bbox if necessary
			var overviewbox = this.getMapView().getCurrentBbox();
			if(this.dynamicOverview && !overviewbox.contains(bbox)){
				this.getMapView().applyBbox(overviewbox.union(bbox));
			}
			
			var transform = new WorldViewTransformation(this.mapView);
			var viewBegin = transform.worldPointToView(bbox.getOrigin());
			var viewEnd = transform.worldPointToView(bbox.getEndPoint());

			var width = Math.abs(viewEnd.getX() - viewBegin.getX());
			var height = Math.abs(viewEnd.getY() - viewBegin.getY());
			viewBegin.setY(viewBegin.getY() - height);

			if (this.targetRect == null) {
				this.targetRect = new Rectangle(this.id+"target");
				this.targetRect.setPosition(viewBegin);
				this.targetRect.setWidth (width);
				this.targetRect.setHeight (height);
				this.targetRect.setStyle(new ShapeStyle("#FFCC00", "0.45", "#FF8800", "1", "1.5", null, null));
				dojo.publish(this.renderTopic, [this.targetRect, "update"]);

				var targetRectElement = this.graphics.getElementById(this.id+"target");
				this.setController(new OverviewRectController(this, this.targetMapId));
			} else {
				this.targetRect.setPosition(viewBegin);
				this.targetRect.setWidth (width);
				this.targetRect.setHeight (height);
				dojo.publish(this.renderTopic, [this.targetRect, "update"]);
			}			
		},

		// Getters and setters:

		getGraphics : function () {
			return this.graphics;
		},

		getCurrentController : function () {
			return this.currentController;
		},

		getMapModel : function () {
			return this.mapModel;
		},

		setMapModel : function (/*MapModel*/mapModel) {
            log.debug("set MapModel on "+this.id)
			this.mapModel = mapModel;
			this.mapModel.setMapView(this.mapView);
			this.mapModel.enableSelection (this.id);
			this.mapModel.enableEditing (this.id);
			this.mapModel.setRenderTopic(this.renderTopic);
			this.destroyables.push(this.mapModel);

			this.selectionHandle = dojo.subscribe(this.mapModel.getSelectionTopic(), this, "_onSelection");
			this.editingHandle = dojo.subscribe(this.mapModel.getEditingTopic(), this, "_onEditing");
            this.scaleUtil.setUnitLength(mapModel.getUnitLength());
            this.scaleUtil.setPixelWidth(this.pixelWidth);
            this._updateScaleText();
		},
		
        /**
         * Set a new pixel width.
         * @param pixelWidth The width of a pixel in meters
         */
        setPixelWidth : function (pixelWidth) {
            this.pixelWidth = pixelWidth;
        },

		getScaleUtil : function () {
			return this.scaleUtil;
		},

		/**
		 * @private
		 */
/*		_unitLengthCallback : function (result){
			this.unitLength = result.unitLength;
			this._updateScaleText();
		},*/

		getMapView : function () {
			return this.mapView;
		},

		setMapView : function (/*MapView*/mapView) {
			this.mapView = mapView;
		},

		getPainterVisitor : function () {
			return this.painterVisitor;
		},

		getRenderTopic : function () {
			return this.renderTopic;
		},

		getBackgroundColor : function () {
			return this.backgroundColor;
		},

		setBackgroundColor : function (backgroundColor) {
			this.backgroundColor = backgroundColor;
			this.graphics.setBackgroundColor(backgroundColor);
		},

		getLineSelectStyle : function () {
			return this.lineSelectStyle;
		},

		setLineSelectStyle : function (lineSelectStyle) {
			this.lineSelectStyle = lineSelectStyle;
		},

		getPointSelectStyle : function () {
			return this.pointSelectStyle;
		},

		setPointSelectStyle : function (pointSelectStyle) {
			this.pointSelectStyle = pointSelectStyle;
		},

		getPolygonSelectStyle : function () {
			return this.polygonSelectStyle;
		},

		setPolygonSelectStyle : function (polygonSelectStyle) {
			this.polygonSelectStyle = polygonSelectStyle;
		},

		getTargetRect : function () {
			return this.targetRect;
		},

		setCursor : function (cursor) {
			this.graphics.setCursor(cursor);
		},

		/**
		 * @private
		 */
		_onSelection : function (event, feature) {
			if (event == "addElement") {
				dojo.publish(this.renderTopic, [feature, "update"]);
			} else if (event == "removeElement"){
				dojo.publish(this.renderTopic, [feature, "delete"]);
			}
		},

		/**
		 * @private
		 */
		_onEditing : function (event, ft) {
			if (event != null) {
				log.info("MapWidget:_onEditing : received "+event);
				if (event == "commit") {
					if (ft == null) {
						return;
					}
					var layer = ft.getLayer();
					layer.getFeatureStore().clear();
					dojo.publish (this.getRenderTopic(), [ layer, "delete" ]);											
					dojo.publish (this.getRenderTopic(), [ layer, "all" ]);											
				}
			}
		},

		/**
		 * @private
		 */
		_stopPropagation : function (evt) {
			var event = new HtmlMouseEvent (evt);
			if (event.getButton() == event.statics.RIGHT_MOUSE_BUTTON) {
				// Is actually meant for HTML events, not SVG events, but seems to work.
				event.stopPropagation();
			}
			return false;
		},



		// SCALEBAR STUFF : 
		scaleBar : null,
		scaleText : null,
		enableScaleBar : function (){
			if (this.scaleBar == null) {
				this.scaleBar = new Picture(this.id + "_screen.scaleBar");
				this.scaleBar.setPosition (new Coordinate(25, parseInt(this.graphics.getHeight()-26)));
				this.scaleBar.setWidth (150);
				this.scaleBar.setHeight(16);
				this.scaleBar.setStyle(new PictureStyle("0.6"));
				this.scaleBar.setHref(dojo.moduleUrl("geomajas.widget", "html/images/scalebar.gif"));
				dojo.publish(this.renderTopic, [ this.scaleBar, "all" ]);

				this.scaleText = new Text(this.id + "_screen.scaleText", "");
				this.scaleText.setPosition (new Coordinate(79, parseInt(this.graphics.getHeight() - 31)));
				this.scaleText.setStyle(this.scaleStyle);
				dojo.publish(this.renderTopic, [ this.scaleText, "all" ]);

				this.scaleHandler = dojo.connect (this.mapView, "setCurrentScale", this, "_updateScaleText");
				this._updateScaleText();
			}
		},

		/**
		 * @private
		 */
		_updateScaleText : function () {
			if (this.scaleText != null) {
				var text = this.scaleUtil.readableScaleFormat(this.mapView.getCurrentScale(), 150, this.unitType);
				this.scaleText.setText(text);
				dojo.publish(this.renderTopic, [this.scaleText, "update" ]);
			}
		},

		disableScaleBar : function () {
			if (this.scaleBar != null) {
				dojo.disconnect (this.scaleHandler);
				dojo.publish(this.renderTopic, [this.scaleBar, "delete" ]);
				dojo.publish(this.renderTopic, [this.scaleText, "delete" ]);
				this.scaleBar = null;
				this.scaleText = null;
			}
		},


		// Pan buttons:
		upPicture : null,
		downPicture : null,
		leftPicture : null,
		rightPicture : null,
		upRightPicture : null,
		downRightPicture : null,
		downLeftPicture : null,
		upLeftPicture : null,
		enablePanButtons : function () {
			if (this.upPicture != null) {
				return;
			}
			// Make sure the featureTransaction is always underneath the pan-buttons:
			this.graphics.drawGroup({id:"featureTransaction"});

			// UP:
			this.upPicture = new Picture(this.id + "_screen.upPanPicture");
			this.upPicture.setWidth(18);
			this.upPicture.setHeight(18);
			this.upPicture.setPosition(new Coordinate(parseInt(this.graphics.getWidth()/2-9), 0));
			this.upPicture.setStyle(new PictureStyle("0.7"));
			this.upPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/nav_up.gif"));			
			dojo.publish(this.renderTopic, [this.upPicture, "all"]);
			var upElement = this.graphics.getElementById(this.id + "_screen.upPanPicture");
			var upSubject = new MouseListenerSubject(upElement);
			upSubject.addListener(new SimplePanController(this.mapView, new Coordinate(0, 1)));
			this.graphics.setCursor("pointer", this.id + "_screen.upPanPicture")
			this.destroyables.push(upSubject);

			// DOWN:
			this.downPicture = new Picture(this.id + "_screen.downPanPicture");
			this.downPicture.setWidth(18);
			this.downPicture.setHeight(18);
			this.downPicture.setPosition(new Coordinate(parseInt(this.graphics.getWidth()/2-9), parseInt(this.graphics.getHeight()-18)));
			this.downPicture.setStyle(new PictureStyle("0.7"));
			this.downPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/nav_down.gif"));			
			dojo.publish(this.renderTopic, [this.downPicture, "all"]);
			var downElement = this.graphics.getElementById(this.id + "_screen.downPanPicture");
			var downSubject = new MouseListenerSubject(downElement);
			downSubject.addListener(new SimplePanController(this.mapView, new Coordinate(0, -1)));
			this.graphics.setCursor("pointer", this.id + "_screen.downPanPicture")
			this.destroyables.push(downSubject);

			// LEFT:
			this.leftPicture = new Picture(this.id + "_screen.leftPanPicture");
			this.leftPicture.setWidth(18);
			this.leftPicture.setHeight(18);
			this.leftPicture.setPosition(new Coordinate(0, parseInt(this.graphics.getHeight()/2-9)));
			this.leftPicture.setStyle(new PictureStyle("0.7"));
			this.leftPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/nav_left.gif"));			
			dojo.publish(this.renderTopic, [this.leftPicture, "all"]);
			var leftElement = this.graphics.getElementById(this.id + "_screen.leftPanPicture");
			var leftSubject = new MouseListenerSubject(leftElement);
			leftSubject.addListener(new SimplePanController(this.mapView, new Coordinate(-1, 0)));
			this.graphics.setCursor("pointer", this.id + "_screen.leftPanPicture")
			this.destroyables.push(leftSubject);

			// RIGHT:
			this.rightPicture = new Picture(this.id + "_screen.rightPanPicture");
			this.rightPicture.setWidth(18);
			this.rightPicture.setHeight(18);
			this.rightPicture.setPosition(new Coordinate(parseInt(this.graphics.getWidth()-18), parseInt(this.graphics.getHeight()/2-9)));
			this.rightPicture.setStyle(new PictureStyle("0.7"));
			this.rightPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/nav_right.gif"));			
			dojo.publish(this.renderTopic, [this.rightPicture, "all"]);
			var rightElement = this.graphics.getElementById(this.id + "_screen.rightPanPicture");
			var rightSubject = new MouseListenerSubject(rightElement);
			rightSubject.addListener(new SimplePanController(this.mapView, new Coordinate(1, 0)));
			this.graphics.setCursor("pointer", this.id + "_screen.rightPanPicture")
			this.destroyables.push(rightSubject);

			// UP-RIGHT:
			this.upRightPicture = new Picture(this.id + "_screen.upRightPanPicture");
			this.upRightPicture.setWidth(18);
			this.upRightPicture.setHeight(18);
			this.upRightPicture.setPosition(new Coordinate(parseInt(this.graphics.getWidth()-18), 0));
			this.upRightPicture.setStyle(new PictureStyle("0.7"));
			this.upRightPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/nav_up_right.gif"));			
			dojo.publish(this.renderTopic, [this.upRightPicture, "all"]);
			var upRightElement = this.graphics.getElementById(this.id + "_screen.upRightPanPicture");
			var upRightSubject = new MouseListenerSubject(upRightElement);
			upRightSubject.addListener(new SimplePanController(this.mapView, new Coordinate(1, 1)));
			this.graphics.setCursor("pointer", this.id + "_screen.upRightPanPicture")
			this.destroyables.push(upRightSubject);
			
			// DOWN-RIGHT:
			this.downRightPicture = new Picture(this.id + "_screen.downRightPanPicture");
			this.downRightPicture.setWidth(18);
			this.downRightPicture.setHeight(18);
			this.downRightPicture.setPosition(new Coordinate(parseInt(this.graphics.getWidth()-18), parseInt(this.graphics.getHeight()-18)));
			this.downRightPicture.setStyle(new PictureStyle("0.7"));
			this.downRightPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/nav_down_right.gif"));			
			dojo.publish(this.renderTopic, [this.downRightPicture, "all"]);
			var downRightElement = this.graphics.getElementById(this.id + "_screen.downRightPanPicture");
			var downRightSubject = new MouseListenerSubject(downRightElement);
			downRightSubject.addListener(new SimplePanController(this.mapView, new Coordinate(1, -1)));
			this.graphics.setCursor("pointer", this.id + "_screen.downRightPanPicture")
			this.destroyables.push(downRightSubject);

			// DOWN-LEFT:
			this.downLeftPicture = new Picture(this.id + "_screen.downLeftPanPicture");
			this.downLeftPicture.setWidth(18);
			this.downLeftPicture.setHeight(18);
			this.downLeftPicture.setPosition(new Coordinate(0, parseInt(this.graphics.getHeight()-18)));
			this.downLeftPicture.setStyle(new PictureStyle("0.7"));
			this.downLeftPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/nav_down_left.gif"));			
			dojo.publish(this.renderTopic, [this.downLeftPicture, "all"]);
			var downLeftElement = this.graphics.getElementById(this.id + "_screen.downLeftPanPicture");
			var downLeftSubject = new MouseListenerSubject(downLeftElement);
			downLeftSubject.addListener(new SimplePanController(this.mapView, new Coordinate(-1, -1)));
			this.graphics.setCursor("pointer", this.id + "_screen.downLeftPanPicture")
			this.destroyables.push(downLeftSubject);

			// UP-LEFT:
			this.upLeftPicture = new Picture(this.id + "_screen.upLeftPanPicture");
			this.upLeftPicture.setWidth(18);
			this.upLeftPicture.setHeight(18);
			this.upLeftPicture.setPosition(new Coordinate(0, 0));
			this.upLeftPicture.setStyle(new PictureStyle("0.7"));
			this.upLeftPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/nav_up_left.gif"));			
			dojo.publish(this.renderTopic, [this.upLeftPicture, "all"]);
			var upLeftElement = this.graphics.getElementById(this.id + "_screen.upLeftPanPicture");
			var upLeftSubject = new MouseListenerSubject(upLeftElement);
			upLeftSubject.addListener(new SimplePanController(this.mapView, new Coordinate(-1, 1)));
			this.graphics.setCursor("pointer", this.id + "_screen.upLeftPanPicture")
			this.destroyables.push(upLeftSubject);
		},

		disablePanButtons : function () {
			if (this.upPicture != null) {
				dojo.publish(this.renderTopic, [this.upPicture, "delete"]);
				dojo.publish(this.renderTopic, [this.downPicture, "delete"]);
				dojo.publish(this.renderTopic, [this.leftPicture, "delete"]);
				dojo.publish(this.renderTopic, [this.rightPicture, "delete"]);
				dojo.publish(this.renderTopic, [this.upRightPicture, "delete"]);
				dojo.publish(this.renderTopic, [this.downRightPicture, "delete"]);
				dojo.publish(this.renderTopic, [this.downLeftPicture, "delete"]);
				dojo.publish(this.renderTopic, [this.upLeftPicture, "delete"]);

				this.upPicture = null;
				this.downPicture = null;
				this.leftPicture = null;
				this.rightPicture = null;
				this.upRightPicture = null;
				this.downRightPicture = null;
				this.downLeftPicture = null;
				this.upLeftPicture = null;
			}
		},
		
		/**
		 * @private
		 */
		_findposx: function (obj) { 
		    var curleft = 0; 
		    if (obj.offsetParent) 
		    { 
		        while (obj.offsetParent) 
		        { 
		            curleft += obj.offsetLeft;
		            curleft += obj.clientLeft; 
		            obj = obj.offsetParent; 
		        } 
		    } 
		    else if (obj.x) 
		        curleft += obj.x; 
	         
		    return curleft; 
		}, 
	
		/**
		 * @private
		 */
		_findposy : function (obj) { 
		    var curtop = 0; 
		    if (obj.offsetParent) 
		    { 
		        while (obj.offsetParent) 
		        { 
		            curtop += obj.offsetTop;
		            curtop += obj.clientTop; 
		            obj = obj.offsetParent; 
		        } 
		    } 
		    else if (obj.y) 
		        curtop += obj.y; 
	     
		    return curtop; 
		}
});
