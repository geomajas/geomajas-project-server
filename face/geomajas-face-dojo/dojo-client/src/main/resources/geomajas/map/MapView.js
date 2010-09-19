dojo.provide("geomajas.map.MapView");
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
dojo.require("geomajas.spatial.Bbox");
dojo.require("geomajas.spatial.Coordinate");
dojo.require("geomajas.spatial.Matrix2D");
dojo.require("geomajas.spatial.Vector2D");
dojo.require("geomajas.map.Camera");
dojo.require("geomajas.map.RenderTopicDistributor");

dojo.declare("MapView", null, {
	
	/**
	 * @fileoverview This class is responsible for the view on the map.
	 * @class This class is responsible for the view on the map. It knows where
	 * the camera is, what the scale is and so forth.
	 * Since this class is a part of a MapWidget, it also knows the map's width
	 * and height.<br>
	 * Furthermore there are functions to change the position on the map.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param topic Rendering topic for the MapWidget this view belongs to.
	 */
	constructor : function (topic) {
		this.topic = topic;
		this.topicHandler = dojo.subscribe(this.topic, this, "onChange");
		this.autoPublish = true;
		this.maxBounds = null;

		this.width = 0;
		this.height = 0;

		this.currentScale = 0;
		this.previousScale = 0;
		this.camera = new Camera();
		this.maximumScale = 10;
		
		this.panning = false;
		
		this.resolutions = null;
		this.resolutionIndex = null;
		
		this.panOrigin = new Coordinate(0,0);
		this.previousPanOrigin = new Coordinate(0,0);
		this.renderTopicDistributor = new RenderTopicDistributor(this.topic + "/ext", this);
	},
	
	getExternalRenderTopic : function () {
		return this.topic + "/ext";
	},

	// hook for mapview changes (before render occurs !!!)
	onChange : function () {
		log.info("onChange mapview");
	},

	/**
	 * returns the world-to-view space transformation matrix 
	 */
	getWorldToViewTransformation : function () {
		var dX = -(this.camera.getX() * this.currentScale) + this.width / 2;
		var dY = this.camera.getY() * this.currentScale + this.height / 2;
		return {xx: this.currentScale, xy: 0, yx: 0, yy: -this.currentScale, dx: dX, dy: dY};
	},
	

	/**
	 * returns the world-to-view space translation matrix 
	 */
	getWorldToViewTranslation : function () {
		var dX = -(this.camera.getX() * this.currentScale) + this.width / 2;
		var dY = this.camera.getY() * this.currentScale + this.height / 2;
		return {xx: 1, xy: 0, yx: 0, yy: 1, dx: dX, dy: dY};
	},
	
	/**
	 * returns the translation of coordinates relative to the pan origin to view coordinates
	 */
	getPanToViewTranslation : function () {
		var dX = -((this.camera.getX()-this.panOrigin.getX()) * this.currentScale) + this.width / 2;
		var dY = (this.camera.getY()-this.panOrigin.getY()) * this.currentScale + this.height / 2;
		return {xx: 1, xy: 0, yx: 0, yy: 1, dx: dX, dy: dY};
	},
	
	/**
	 * returns the translation of scaled world coordinates to coordinates relative to the pan origin
	 */
	getWorldToPanTranslation : function () {
		var dX = -(this.panOrigin.getX() * this.currentScale);
		var dY = this.panOrigin.getY() * this.currentScale ;
		return {xx: 1, xy: 0, yx: 0, yy: 1, dx: dX, dy: dY};
	},

	/**
	 * returns the world-to-view space translation matrix 
	 */
	getWorldToViewScaling : function () {
		return {xx: this.currentScale, xy: 0, yx: 0, yy: -this.currentScale, dx: 0, dy: 0};
	},

	/**
	 * Set the size of the map in pixels.
	 * @param width The map's width.
	 * @param height The map's height.
	 */
	setSize : function (width, height) {
		if (this.autoPublish) {
			this._pushPanData();
		}
		var oldbbox = this.getCurrentBbox();
		this.width = width;
		this.height = height;
		// reset the old center position
		this.setCenterPosition(oldbbox.getCenterPoint());
		if (this.autoPublish) {
			dojo.publish(this.topic, [null, "all"]);
		}
	},
	
	/**
	 * Set the precision of the map.
	 * @param precision The precision in number of decimals
	 */
	setPrecision : function (precision) {
		this.precision = precision;
	},
	
	getPrecision : function () {
		return this.precision;
	},

	/**
	 * Set the spatial reference id.
	 * @param srid The srid (should be an integer)
	 */
	setSRID : function (srid) {
		this.srid = srid;
	},
	
	getSRID : function () {
		return this.srid;
	},

	setCenterPosition : function (coordinate) {
		if (this.autoPublish) {
			this._pushPanData();
		}
		var center = this._calcCenterFromPoint(coordinate);
		this.camera.setPosition(center.getX(), center.getY());
		if (this.autoPublish) {
			dojo.publish(this.topic, [null, "all"]);
		}
	},
	
	setResolutions : function (resolutions) {
		log.info("setResolutions");
		this.resolutions = [];
		for(var i = 0; i < resolutions.length; i++){
			this.resolutions.push(resolutions[i]);
		}
	},
	
	getResolutions : function () {
		return this.resolutions;
	},

	/**
	 * Since the view on a GIS is usually a Bbox, it is possible to
	 * apply the box on the view. Meaning the box we fit to the view
	 * as good as possible.
	 * @param bbox An Bbox in world coordinates that determines the view
	 *              from now on.
	 * @param keepRotation Should the camera's angle be reset or not??
	 * @param option One of the zooming options: geomajas.ZoomOption.ZOOM_OPTION_EXACT,
	 * 											 geomajas.ZoomOption.ZOOM_OPTION_LEVEL_FIT,
	 *               							 geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CHANGE, 
	 *               							 geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CLOSEST. 
	 *               Default is geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CLOSEST.
	 */
	applyBbox : function (bbox, keepRotation, option) {
		var center = bbox.getCenterPoint();
		if (!this._isViewAllowed(center))
			return false;
		this._pushPanData();
		this.setAutoPublish(false);
		if (!keepRotation) { this.camera.setAlpha(0); }

        // fix some anomalies on small screens, prevent negative sizes.
        if (this.width<10) this.width=10;
        if (this.height<10) this.height=10;

		var wRatio = parseFloat(this.width / bbox.getWidth());
		var hRatio = parseFloat(this.height / bbox.getHeight());
		if (wRatio == "Infinity" && hRatio == "Infinity") {
			return;
		}
		if (wRatio < hRatio) { // Horizontal fit, vertical centering.
			//this.currentScale = wRatio;
			this.setCurrentScale (wRatio, option);
		} else { // Vertical fit, horizontal centering.
			//this.currentScale = hRatio;
			this.setCurrentScale (hRatio, option);
		}
		
		this.camera.setPosition (center.getX(), center.getY());
		
		// set pan origin equal to camera
		this.panOrigin = new Coordinate(center.getX(),center.getY());
	
		this.setAutoPublish(true);
		dojo.publish(this.topic, [null, "all"]);
	},

	/**
	 * Given the information in this MapView object, what is the currently
	 * visible area?
	 */
	getCurrentBbox : function () {
		var w = this._getViewSpaceWidth();
		var h = this._getViewSpaceHeight();
		var x = this.camera.getX() - w/2;
		var y = this.camera.getY() - h/2;

		var box = new Bbox();
		box.set(x, y, w, h);
		return box;
	},

	/**
	 * Return the current scale.
	 */
	getCurrentScale : function () {
		return this.currentScale;
	},

	setCurrentScale : function (currentScale, option) {
		if (this.autoPublish) {
			this._pushPanData();
		}
		var scale = this._snapToResolution(currentScale, option);
		if (scale > this.maximumScale) {
			scale = this.maximumScale;
		}
		this.currentScale = scale;		
		var center = this._calcCenterFromPoint(this.camera.getPosition());
		this.camera.setPosition(center.getX(), center.getY());
		if (this.autoPublish) {
			dojo.publish(this.topic, [null, "all"]);
		}
	},

	/**
	 * Move the view on the map. This happens by translating the camera
	 * in turn.
	 * @param x Translation factor along the X-axis in world space.
	 * @param y Translation factor along the Y-axis in world space.
	 */
	translate : function (x, y, status) {
		if (this.autoPublish) {
			this._pushPanData();
		}
		var c = this.camera.getPosition();
		var center = this._calcCenterFromPoint(new Coordinate(c.x + x, c.y + y));
		this.camera.setPosition (center.getX(), center.getY());
		if (this.autoPublish) {
			dojo.publish(this.topic, [null, status ? status : "all"]);
		}
	},

	/**
	 * Rotate the view on the map. This happend by rotating the camera
	 * in turn.
	 * @param angle The rotation factor (radial).
	 */
	rotate : function (angle) {
		if (this.autoPublish) {
			this._pushPanData();
		}
		this.camera.rotate (angle);

		if (this.autoPublish) {
			dojo.publish(this.topic, [null, "all"]);
		}
	},

	/**
	 * Adjust the current scale on the map by a new factor.
	 * @param delta Adjust the scale by factor "delta".
	 */
	scale : function (delta, option) {
		this.setCurrentScale (this.currentScale * delta, option);
	},
	
	
	isPanning : function () {
		return Math.abs(this.currentScale-this.previousScale) < 1.0E-10 && this.previousPanOrigin.equalsDelta(this.panOrigin, 1.0E-10);
	},

	toString : function () {
		return "VIEW: scale=" + this.currentScale + ", " + this.camera.toString();
	},

	// Getters:

	getCamera : function () {
		return this.camera;
	},

	getScale : function () {
		return this.currentScale;
	},

	getMapWidth : function () {
		return this.width;
	},

	getMapHeight : function () {
		return this.height;
	},

	getPanOrigin : function () {
		return this.panOrigin;
	},

	isAutoPublish : function () {
		return this.autoPublish;
	},

	setAutoPublish : function (autoPublish) {
		this.autoPublish = autoPublish;
	},

	getMaximumScale : function () {
		return this.maximumScale;
	},

	setMaximumScale : function (maximumScale) {
		this.maximumScale = maximumScale;
	},
	
	setMaxBounds : function (maxBounds) {
		this.maxBounds = maxBounds;
	},
	
	getMaxBounds : function () {
		return this.maxBounds;
	},

	// Private functions:

	/**
	 * returns false if point is outside of maxBounds (box must be at least half inside the allowed view)
	 */
	_isViewAllowed : function (center) {
		if (this.maxBounds == null) {
			return true;
		} else {
			var minC = this.maxBounds.getOrigin();
			var maxC = this.maxBounds.getEndPoint();
			if (center.getX() <minC.getX() || 
				center.getX() > maxC.getX() || 
				center.getY() < minC.getY() || 
				center.getY() > maxC.getY())
				return false;
			else
				return true;
		}
	},

	_pushPanData : function () {
		this.previousScale = this.currentScale;
		this.previousPanOrigin = this.panOrigin.clone();
	},
	
	/**
	 * @private
	 */
	_getViewSpaceWidth : function () {
		return parseFloat (this.width / this.currentScale);
	},

	/**
	 * @private
	 */
	_getViewSpaceHeight : function () {
		return parseFloat (this.height / this.currentScale);
	},
	
	_snapToResolution : function (scale, option) {
		if (option == null){
			option = geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CLOSEST;
		} else if (option == geomajas.ZoomOption.ZOOM_OPTION_EXACT) {
			return scale; /* STOP here */
		}
		log.info("old scale = "+this.currentScale);
		log.info("new scale = "+scale);
		log.info("old index = "+this.resolutionIndex);
		if(this.resolutions && this.resolutions.length>0){
			var newResolutionIndex = null;
			var screenResolution = (scale == 0 ? Number.MAX_VALUE : 1.0/scale);
			log.info("new screenResolution = "+screenResolution);
			if (screenResolution >= this.resolutions[0]) {
				newResolutionIndex = 0;
			} else if (screenResolution <= this.resolutions[this.resolutions.length-1]) {
				newResolutionIndex = this.resolutions.length-1;
			} else {
				for (var i = 0; i < this.resolutions.length-1; i++) {
					var upper = this.resolutions[i];
					var lower = this.resolutions[i + 1];
					if (screenResolution <= upper && screenResolution >= lower) {
						log.info("upper = "+upper+", lower="+lower);
						if (option == geomajas.ZoomOption.ZOOM_OPTION_LEVEL_FIT) {
							newResolutionIndex = i;
							break;
						} else {
							if ((upper - screenResolution) > (screenResolution - lower)) {
								newResolutionIndex = i+1;
								break;
							} else {
								newResolutionIndex = i;
								break;
							}
						}
					}
				}
			}
			if(this.resolutionIndex != null){
				if(newResolutionIndex == this.resolutionIndex && option == geomajas.ZoomOption.ZOOM_OPTION_LEVEL_CHANGE){
					if(scale > this.currentScale && newResolutionIndex < (this.resolutions.length-1)){
					   log.info("same index but higher scale, moving up");
						newResolutionIndex++;
					} else if( scale < this.currentScale && newResolutionIndex > 0){
					   log.info("same index but higher scale, moving down");
						newResolutionIndex--;
					}
				}
			}
			log.info("new index = "+newResolutionIndex);
			this.resolutionIndex = newResolutionIndex;
			log.info("new resolution = "+this.resolutions[this.resolutionIndex]);
			return 1.0/this.resolutions[this.resolutionIndex];
		} else {
			return scale;
		}		
	},

	/**
	 * @private
	 * Adjusts the center point of the map, to an allowed center point. This method tries to make
	 * sure the whole map extent is inside the maximum allowed bounds.
	 */
	_calcCenterFromPoint : function (worldCenter) {
		if (this.maxBounds == null) {
			return worldCenter;
		} else if (geomajasConfig.zoomMaxExtentBounds != null) {
			var temp = geomajasConfig.zoomMaxExtentBounds;
			this.maxBounds = new Bbox(temp.x, temp.y, temp.width, temp.height);
	}
		var w = this._getViewSpaceWidth() / 2;
		var h = this._getViewSpaceHeight() / 2;

		var minCoordinate = this.maxBounds.getOrigin();
		var maxCoordinate = this.maxBounds.getEndPoint();

		if ((w*2) > this.maxBounds.getWidth()) {
			worldCenter.setX(this.maxBounds.getCenterPoint().getX());
		} else {
			if ((worldCenter.getX() - w) < minCoordinate.getX() ) {
				worldCenter.setX(minCoordinate.getX() + w);
			}
			if ((worldCenter.getX() + w) > maxCoordinate.getX() ) {
				worldCenter.setX(maxCoordinate.getX() - w);
			}
		}
		
		if ((h*2) > this.maxBounds.getHeight()) {
			worldCenter.setY(this.maxBounds.getCenterPoint().getY());
		} else {
			if ((worldCenter.getY() - h) < minCoordinate.getY() ) {
				worldCenter.setY(minCoordinate.getY() + h);
			}
			if ((worldCenter.getY() + h) > maxCoordinate.getY() ) {
				worldCenter.setY(maxCoordinate.getY() - h);
			}
		}
		return worldCenter;
	}
});
