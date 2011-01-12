dojo.provide("geomajas.spatial.cache.experimental.LabeledTile");
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

dojo.declare("LabeledTile", RenderedTile, {

	/**
	 * @fileoverview Definition of a Tile.
	 * @class Definition of a Tile.
	 * @extends PainterVisitable
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function (tile) {
		this.featureIds = tile.featureIds;
		this.width = tile.width;
		this.height = tile.height;
		this.screenWidth = tile.screenWidth;
		this.screenHeight = tile.screenHeight;
		this.codes = tile.codes;
		this.clipped = tile.clipped;
		this.featureFragment = tile.featureFragment;
		this.featureImage = tile.featureImage;
		this.rendered = tile.rendered;
		this.code = tile.code;
		this.cache = tile.cache;
		this.bbox = tile.bbox;
		this.deferred = tile.deferred;

		// In case of vector-rendering:

		/** data holder: contains svg or vml from the server for labels. */
		this.labelFragment = null;
		
		// In case of raster-rendering:

		/** Map of the features in this node */
		this.labelImage = null;
	},



	//-------------------------------------------------------------------------
	// Spatial node functions:
	//-------------------------------------------------------------------------

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
		command.addParam("styleInfo", this.cache.layer.getNamedStyle().toJSON());
		command.addParam("panOrigin", this.cache.layer.getMapModel().getMapView().getPanOrigin());
		if (this.rendered) {
			command.addParam("paintGeometries", false);
		} else {
			command.addParam("paintGeometries", true);
		}
		command.addParam("paintLabels", true);
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
				return json;
			}
		);
	},

	//-------------------------------------------------------------------------
	// Vector-rendering - getters and setters:
	//-------------------------------------------------------------------------

	/**
	 * set the user data
	 */
	setLabelFragment : function (labelFragment) {
		this.labelFragment = labelFragment;
	},

	/**
	 * Return the user data of the node 
	 */
	getLabelFragment : function () {
		return this.labelFragment;
	},


	//-------------------------------------------------------------------------
	// Raster-rendering - getters and setters:
	//-------------------------------------------------------------------------

	getLabelImage : function () {
		return this.labelImage;
	},

	//-------------------------------------------------------------------------
	// Private functions:
	//-------------------------------------------------------------------------

	/**
	 * @private
	 * Parses a json object.
	 */
	_fromJSON : function (json) {
		if (json["labelContent"] != null) {
			if (json.contentType.value == "STRING_CONTENT") {
				this.labelFragment = json.labelContent;
			} else if (json.contentType.value == "URL_CONTENT") {
				this.labelImage = new RasterImage();
				this.labelImage.setId(this.getId());

				var tempUrl = json.labelContent;
				if (tempUrl.startsWith("http")){
					this.labelImage.setUrl(json.labelContent);
				} else {
					this.labelImage.setUrl(geomajasConfig.serverBase + json.labelContent);
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

				this.labelImage.setBounds(screenbounds);
				this.labelImage.setLevel(this.code.tileLevel);
				this.labelImage.setXIndex(this.code.x);
				this.labelImage.setYIndex(this.code.y);
			}
		}

		return this.inherited("_fromJSON", arguments);
	}
});
 
