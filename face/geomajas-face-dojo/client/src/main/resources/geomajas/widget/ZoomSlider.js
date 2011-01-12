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
dojo.provide("geomajas.widget.ZoomSlider");

dojo.require("dijit.form.Slider");
dojo.require("geomajas.controller.PanToolController");
/**
 * This class creates a slider widget and panbuttons on the given mapwidget
 * @author An Buyle
 * @author Balder Van Camp
 */
dojo.declare("geomajas.widget.ZoomSlider", null, {
	
	mapWidget : null,
	slider : null,
	scaleItems : null,
	destroyables: [],
	
	postCreate: function() {
	},

	constructor : function (id) {
		if (id) {
			this.id = id;
		}
	},
	/**
	 * If a toolbarId is given the toolbarId will be passed to the PanToolController
	 * @param toolbarid
	 */
	setToolbarId : function(toolbarId) {
		this.toolbarid = toolbarid;
	},
	/**
	 * On setting the mapWidget the slider will be initialized. The map should already by fully build.
	 * PanButtons will be enabled by default, they can be disabled by calling disablePanButtons method
	 * @param set the mapWidget where this zoomSlider should appear/have effect on
	 */
	setMapWidget : function (mapWidget) {
		this.mapWidget = mapWidget;
		var resolutions = mapWidget.getMapView().getResolutions();
		var scales = [];

		if (resolutions != null) {
			for(var i=0; i<resolutions.length; i++) {
				var value = Math.round(mapWidget.getScaleUtil().getUnitLength() * resolutions[i] / 
						mapWidget.getScaleUtil().getPixelWidth());
				scales.push(value);
			}
			if (scales.length != 0) {
				this.setScaleArray(scales);
			}
		}
		
		if (scales.length == 0) {
			this.setScaleArray( [ 1500000, 1000000, 500000, 250000, 100000, 50000, 20000,
			                       10000,   5000, 	2000,   1000, 500, 250]); 
		}
			
		var curSliderValue = this._snapToSliderValue(mapWidget.getMapView().getCurrentScale());
	    
		var vertical= document.createElement('div');
		var rulesNode = document.createElement('div');

		mapWidget.domNode.appendChild(vertical);
		vertical.appendChild(rulesNode);
	    
	    var sliderRules = new dijit.form.VerticalRule({
	        count: this.scaleItems.length,
	        style: "width:4px;"
	    	},
	    	rulesNode);
	    
	    this.slider = new dijit.form.VerticalSlider({
	        name: "verticalSlider",
	        value: curSliderValue,
	        minimum: 0,
	        maximum: this.scaleItems.length-1,
	        discreteValues: this.scaleItems.length,
	        intermediateChanges: false,
	        style: "height:240px; position: absolute; left: 24px; top: 64px;"
	    	},
	    	vertical);
	
		dojo.connect(mapWidget.getMapView(), "setCurrentScale", this, "_updateSliderValue");

	    this.slider.onChange = dojo.hitch(this, "_onSliderChange");
	    this.enablePanButtons();
	},
	
	/**
	 * @private
	 * When a new value is entered in the combobox, try and zoom in or out on
	 * the map.
	 * @param label The label from the combobox. Either one of the selected 
	 *              values (1:x), or a simple integer (x).
	 */
	_onSliderChange : function (value) {
		if (this.inside_update) {
			return;
		}
		
		if (this.mapWidget == null) {
			log.error ("_onSliderChange: MapWidget is null!");
			return;
		}
		log.debug ("_onSliderChange: value=" + value);
		if (value >= 0) {
			this.inside_update = true;
			this.mapWidget.getMapView().setCurrentScale(this.scaleItems[parseInt(value)]);
			this.inside_update = false;
		}
	},
	
	
	/**
	 * Set a new list of scales for the scale Slider
	 * @param array The list of scale should be an array with integer values.
	 *              These will be filled in as (1:x) one by one, so better make
	 *              sure they are sorted! Example: [1000,5000,10000].
	 */
	setScaleArray : function(array) {
		if (array == null || !(array instanceof Array)) {
			log.debug("No array given in setScaleArray!");
			return;
		}
		this.scaleItems = [];
		for ( var i = 0; i < array.length; i++) {
			var item = "1 : " + parseInt(array[i]);
			var scale = this.mapWidget.getScaleUtil().stringToScale(item);
			this.scaleItems.push(scale);
		}
	},
	
	_updateSliderValue :  function (scale) {
		if (!this.inside_update) {
			// Ask scale again, because it may have been snapped to a resolution:
			scale = this.mapWidget.getMapView().getCurrentScale();
			var scale = this._snapToSliderValue(scale);
			
			this.inside_update = true;
			this.slider.setValue(scale, false);
			this.inside_update = false;
		}
	},
	
	_snapToSliderValue : function (scale) {
		
		var newValue = 0;
	
		log.info("new scale = "+scale);
		
		if(this.scaleItems && this.scaleItems.length>0){
			var screenResolution = (scale == 0 ? Number.MAX_VALUE : 1.0/scale);
			log.info("new screenResolution = "+screenResolution);
		
			if (screenResolution >= 1.0/this.scaleItems[0]) {
				newValue = 0;
			} else if (screenResolution <= 1.0/this.scaleItems[this.scaleItems.length-1]) {
				newValue = this.scaleItems.length-1;
			} else {
				for (var i = 0; i < this.scaleItems.length-1; i++) {
					var upper = 1/this.scaleItems[i];
					var lower = 1/this.scaleItems[i + 1];
					if (screenResolution >= lower) {
						log.info("upper = "+upper+", lower="+lower);
						if ((upper - screenResolution) > (screenResolution - lower)) {
							newValue = i+1;
							break;
						} else {
							newValue = i;
							break;
						}
					}
				}
			}
			log.info("new value = "+newValue);
			log.info("new scale slider resolution = "+this.scaleItems[newValue]);
		}
		return newValue;
	},
	
	panGroupX:				32,
	panGroupY:				32,
	panOpacity:				"0.9",
	
	upInPanGroupPicture : null,
	downInPanGroupPicture : null,
	leftInPanGroupPicture : null,
	rightInPanGroupPicture : null,
	panInPanGroupPicture : null,
	
	enablePanButtons : function () {
		if (this.upInPanGroupPicture != null) {
			return;
		}
		var rendertopic = this.mapWidget.getRenderTopic();
		// Make sure the featureTransaction is always underneath the pan-buttons:
		this.mapWidget.getGraphics().drawGroup({id:"zoomSliderfeatureTransaction"});

		// CENTER: Activate/deactivate Pan Tool:
		this.panInPanGroupPicture = new Picture(this.id + "_screen.centerPanPicture");
		this.panInPanGroupPicture.setWidth(16);
		this.panInPanGroupPicture.setHeight(16);
	
		this.panInPanGroupPicture.setPosition(new Coordinate(this.panGroupX-9, this.panGroupY-9));
		
		this.panInPanGroupPicture.setStyle(new PictureStyle("0.7"));
		this.panInPanGroupPicture.setHref(dojo.moduleUrl("geomajas.widget", "themes/images/hand.gif"));
		
		dojo.publish(rendertopic, [this.panInPanGroupPicture, "all"]);
		var panToolElement = this.mapWidget.getGraphics().getElementById(this.id + "_screen.centerPanPicture");
		var panToolSubject = new MouseListenerSubject(panToolElement);
		if (this.toolbarid) {
			panToolSubject.addListener(new geomajas.controller.PanToolController(this.mapWidget, true/*hideLabelsOnDrag*/, this.toolbarid));
		} else {
			panToolSubject.addListener(new geomajas.controller.PanToolController(this.mapWidget, true/*hideLabelsOnDrag*/));
		}
		this.mapWidget.getGraphics().setCursor("pointer", this.id + "_screen.centerPanPicture");
		this.destroyables.push(panToolSubject);		
		
		// UP:
		this.upInPanGroupPicture = new Picture(this.id + "_screen.upPanPicture");
		this.upInPanGroupPicture.setWidth(16);
		this.upInPanGroupPicture.setHeight(16);
	
		this.upInPanGroupPicture.setPosition(new Coordinate(this.panGroupX-9, this.panGroupY-28));
		
		this.upInPanGroupPicture.setStyle(new PictureStyle(this.panOpacity));
		this.upInPanGroupPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/arrow_up.png"));			
		dojo.publish(rendertopic, [this.upInPanGroupPicture, "all"]);
		var upElement = this.mapWidget.getGraphics().getElementById(this.id + "_screen.upPanPicture");
		var upSubject = new MouseListenerSubject(upElement);
		upSubject.addListener(new SimplePanController(this.mapWidget.getMapView(), new Coordinate(0, 1)));
		this.mapWidget.getGraphics().setCursor("pointer", this.id + "_screen.upPanPicture");
		this.destroyables.push(upSubject);

		// DOWN:
		this.downInPanGroupPicture = new Picture(this.id + "_screen.downPanPicture");
		this.downInPanGroupPicture.setWidth(16);
		this.downInPanGroupPicture.setHeight(16);
		this.downInPanGroupPicture.setPosition(new Coordinate(this.panGroupX-9, this.panGroupY+11));
		this.downInPanGroupPicture.setStyle(new PictureStyle(this.panOpacity));
		this.downInPanGroupPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/arrow_down.png"));			
		dojo.publish(rendertopic, [this.downInPanGroupPicture, "all"]);
		var downElement = this.mapWidget.getGraphics().getElementById(this.id + "_screen.downPanPicture");
		var downSubject = new MouseListenerSubject(downElement);
		downSubject.addListener(new SimplePanController(this.mapWidget.getMapView(), new Coordinate(0, -1)));
		this.mapWidget.getGraphics().setCursor("pointer", this.id + "_screen.downPanPicture");
		this.destroyables.push(downSubject);

		// LEFT:
		this.leftInPanGroupPicture = new Picture(this.id + "_screen.leftPanPicture");
		this.leftInPanGroupPicture.setWidth(16);
		this.leftInPanGroupPicture.setHeight(16);
		this.leftInPanGroupPicture.setPosition(new Coordinate(this.panGroupX-28, this.panGroupY-9));
		this.leftInPanGroupPicture.setStyle(new PictureStyle(this.panOpacity));
		this.leftInPanGroupPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/arrow_left.png"));			
		dojo.publish(rendertopic, [this.leftInPanGroupPicture, "all"]);
		var leftElement = this.mapWidget.getGraphics().getElementById(this.id + "_screen.leftPanPicture");
		var leftSubject = new MouseListenerSubject(leftElement);
		leftSubject.addListener(new SimplePanController(this.mapWidget.getMapView(), new Coordinate(-1, 0)));
		this.mapWidget.getGraphics().setCursor("pointer", this.id + "_screen.leftPanPicture");
		this.destroyables.push(leftSubject);

		// RIGHT:
		this.rightInPanGroupPicture = new Picture(this.id + "_screen.rightPanPicture");
		this.rightInPanGroupPicture.setWidth(16);
		this.rightInPanGroupPicture.setHeight(16);
		this.rightInPanGroupPicture.setPosition(new Coordinate(this.panGroupX+11, this.panGroupY-9));
		this.rightInPanGroupPicture.setStyle(new PictureStyle(this.panOpacity));
		this.rightInPanGroupPicture.setHref(dojo.moduleUrl("geomajas.widget", "html/images/arrow_right.png"));			
		dojo.publish(rendertopic, [this.rightInPanGroupPicture, "all"]);
		var rightElement = this.mapWidget.getGraphics().getElementById(this.id + "_screen.rightPanPicture");
		var rightSubject = new MouseListenerSubject(rightElement);
		rightSubject.addListener(new SimplePanController(this.mapWidget.getMapView(), new Coordinate(1, 0)));
		this.mapWidget.getGraphics().setCursor("pointer", this.id + "_screen.rightPanPicture");
		this.destroyables.push(rightSubject);

	},

	disablePanButtons : function () {
		if (this.upInPanGroupPicture != null) {
			var rendertopic = this.mapWidget.getRenderTopic();
			dojo.publish(rendertopic, [this.panInPanGroupPicture, "delete"]);
			dojo.publish(rendertopic, [this.upInPanGroupPicture, "delete"]);
			dojo.publish(rendertopic, [this.downInPanGroupPicture, "delete"]);
			dojo.publish(rendertopic, [this.leftInPanGroupPicture, "delete"]);
			dojo.publish(rendertopic, [this.rightInPanGroupPicture, "delete"]);

			this.panInPanGroupPicture = null;
			
			this.upInPanGroupPicture = null;
			this.downInPanGroupPicture = null;
			this.leftInPanGroupPicture = null;
			this.rightInPanGroupPicture = null;
		}
	},
	
	destroy: function(/*Boolean*/ finalize){
		dojo.forEach(this.destroyables, function(d) {d.destroy();});
		this.inherited(arguments);
	}

});
// ----------------------
