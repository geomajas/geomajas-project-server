dojo.provide("geomajas.spatial.cache.experimental.RenderedTile");
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
dojo.require("geomajas.gfx.PainterVisitable");
dojo.require("geomajas.map.Feature");

dojo.declare("RenderedTile", SpatialNode, {

	/**
	 * @fileoverview Definition of a Tile.
	 * @class Definition of a Tile.
	 * @extends PainterVisitable
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function (code, bbox, cache) {
		/** Array of feature ID's. */
		this.featureIds = [];
		
		/** width in map units */
		this.width = null;
		
		/** height in map units */
		this.height = null;
		
		/** width in screen units */
		this.screenWidth = null;
		
		/** height in screen units */
		this.screenHeight = null;
		
		// In case of vector-rendering:

		/** dependend tile codes */
		this.codes = [];
		
		/** is the tile clipped : clipping is necessary to avoid too big coordinates */
		this.clipped = false;
		
		/** data holder: contains svg or vml from the server for features. */
		this.featureFragment = null;

		// In case of raster-rendering:

		/** Map of the features in this node */
		this.featureImage = null;
		
		
		this.rendered = false;
	},



	//-------------------------------------------------------------------------
	// Spatial node functions:
	//-------------------------------------------------------------------------

	/**
	 * Returns all features in this tile. Warning : this will not return the features from other tiles
	 * that intersect with this tile ! If you want to interact with all features, use the applyOnBounds()
	 * method.
	 */
	getFeatures : function () {
		var features =  this.cache.getFeaturesByCode(this.code);
		return features;
	},

	/**
	 * Fetch all data related to this tile.
	 */
	fetch : function (filter, callback) {
		this.cache.onTileFetching();
		// IE commands executed in sync to avoid the VML jitter !!!!
        var command = new JsonCommand("command.render.GetVectorTile",
                "org.geomajas.command.dto.GetVectorTileRequest", null, dojo.isIE > 0 ? true : false);
		command.addParam("layerId", this.cache.layer.layerId);
        command.addParam("crs",this.cache.layer.getMapModel().getCrs());
		command.addParam("code", this.code.toJSON());
		command.addParam("scale", this.cache.layer.getMapModel().getMapView().getCurrentScale());
		command.addParam("filter", filter);
		command.addParam("renderer", dojo.isIE ? "vml" : "svg");
		var styleDefs = [];
		for (var i=0; i<this.cache.layer.getStyles().count; i++) {
			styleDefs.push (this.cache.layer.getStyles().item(i).toJSON());
		}
		command.addParam("styleDefs", styleDefs);
		command.addParam("panOrigin", this.cache.layer.getMapModel().getMapView().getPanOrigin());
		command.addParam("paintGeometries", true);
		command.addParam("paintLabels", false);
		command.addParam("featureIncludes", geomajasConfig.lazyFeatureIncludesDefault);

		this.deferred = geomajasConfig.dispatcher.execute(command);
		log.info("Tile.fetch : "+this.code);
		this.deferred.addCallback(this,
			function(json){
				try {
					var features = this._fromJSON(json.tile);
					if (features) {
						for (var i=0; i<features.length; i++) {
							this.cache.features.add(features[i].getId(), features[i]);
						}
					}
					callback(this);
				} catch (e){
					log.error("Tile.fetch : exception !");
					for (var i in e) log.error(e[i]);
				} finally {
					// always called !
					this.cache.onTileFetched(json.executionTime);
				}
				this.rendered = true;
				return json;
			}
		);
	},

	applyConnected : function (filter, callback) {
		this.apply(
			function(tile) {
				var codes = tile.getCodes();
				for(var i = 0; i < codes.length; i++){
					//log.info("Tile.applyConnected : "+codes[i]);
					var t = tile.cache.addTile(codes[i]);
					//log.info("Tile.applyConnected : "+t.getStatus());
					if(t.getStatus() == t.status.EMPTY){
						t.fetch(filter,callback);
					} else {
						t.apply(callback);
					}
				}
			}
		);
	},
	

	/**
	 * Returns the current status of this Tile. Can be one of the following:
	 * <ul>
	 * <li>this.status.EMPTY</li>
	 * <li>this.status.LOADING</li>
	 * <li>this.status.LOADED</li>
	 * </ul>
	 */
	getStatus : function () {
		if (this.deferred == null) {
			return this.status.EMPTY;
		}
		if (this.featureIds == null || this.featureIds.length == 0) {
			return this.status.LOADING;
		}
		return this.status.LOADED;
	},
	
	isLoading : function () {
		return (this.featureIds == null || this.featureIds.length == 0);
	},

	

	//-------------------------------------------------------------------------
	// Some getters and setters:
	//-------------------------------------------------------------------------

	getLayer : function () {
		return this.cache.layer;
	},

	/**
	 * Returns an array of featureId's (localId) strictly within this Tile.
	 */
	getFeatureIds : function () {
		return this.featureIds;
	},

	/**
	 * Set the unique code.
	 * @param code An instance of {@link TileCode}.
	 */
	setCode : function (code) {
		this.code = code;
	},

	/**
	 * Return the width in map units 
	 */
	getWidth : function () {
		return this.width;
	},
	
	/**
	 * Return the height in map units 
	 */
	getHeight : function () {
		return this.height;
	},
	
	/**
	 * Return the width in screen units 
	 */
	getScreenWidth : function () {
		return this.screenWidth;
	},
	
	/**
	 * Return the height in screen units 
	 */
	getScreenHeight : function () {
		return this.screenHeight;
	},

	//-------------------------------------------------------------------------
	// Vector-rendering - getters and setters:
	//-------------------------------------------------------------------------

	/**
	 * set the user data
	 */
	setFeatureFragment : function (featureFragment) {
		this.featureFragment = featureFragment;
	},

	/**
	 * Return the user data of the node 
	 */
	getFeatureFragment : function () {
		return this.featureFragment;
	},

	/**
	 * is the tile clipped ?
	 */
	isClipped : function () {
		return this.clipped;
	},

	/**
	 * return the dependend code
	 */
	getCodes : function () {
		return this.codes;
	},

	//-------------------------------------------------------------------------
	// Raster-rendering - getters and setters:
	//-------------------------------------------------------------------------

	getFeatureImage : function () {
		return this.featureImage;
	},

	//-------------------------------------------------------------------------
	// Private functions:
	//-------------------------------------------------------------------------

	/**
	 * @private
	 * Parses a json object.
	 */
	_fromJSON : function (json) {
		this.code = new TileCode();
		this.code.fromJSON(json.code);
		this.width = json.tileWidth;
		this.height = json.tileHeight;
		this.screenWidth = json.screenWidth;
		this.screenHeight = json.screenHeight;
		var features = [];
		for (var i=0; i<json.features.list.length; i++) {
			var feature = new Feature();
			feature.setLayer(this.cache.layer);
			feature.fromJSON(json.features.list[i]);
			features.push(feature);
			this.featureIds.push(feature.getId());
		}
		
		if (json["featureContent"] != null) {
			if (json.contentType == "STRING_CONTENT") {
				this.featureFragment = json.featureContent;
				this.clipped = json.clipped;
				for(var i = 0; i < json.codes.list.length; i++){
					var code = new TileCode();
					code.fromJSON(json.codes.list[i]);
					this.codes.push(code);
				}
			} else if (json.contentType == "URL_CONTENT") {
				this.featureImage = new RasterImage();
				this.featureImage.setId(this.getId());

				var tempUrl = json.featureContent;
				if (tempUrl.startsWith("http")){
					this.featureImage.setUrl(json.featureContent);
				} else {
					this.featureImage.setUrl(geomajasConfig.serverBase + json.featureContent);
				}

				var mapView = this.cache.layer.getMapModel().getMapView();
				var transform = new WorldViewTransformation(mapView);
				
				// get the bounds
				var worldbounds = this.cache._calcBoundsForTileCode(this.code);

				// transform to viewspace
				var screenbounds = transform.worldBoundsToView(worldbounds);

				// pan with view
				var panDelta = transform.worldPointToView(mapView.getPanOrigin());
				screenbounds.x = Math.round(screenbounds.x - panDelta.getX());
				screenbounds.y = Math.round(screenbounds.y - panDelta.getY());

				this.featureImage.setBounds(screenbounds);
				this.featureImage.setLevel(this.code.tileLevel);
				this.featureImage.setXIndex(this.code.x);
				this.featureImage.setYIndex(this.code.y);
			}
		}

		return features;
	}
});
