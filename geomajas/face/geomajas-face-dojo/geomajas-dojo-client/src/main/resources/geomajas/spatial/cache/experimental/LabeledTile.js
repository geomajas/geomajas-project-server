dojo.provide("geomajas.spatial.cache.experimental.LabeledTile");
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
        var command = new JsonCommand("command.render.GetRenderedTile",
                "org.geomajas.command.dto.GetRenderedTileRequest", null, dojo.isIE > 0 ? true : false);
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
		if (this.rendered) {
			command.addParam("paintGeometries", false);
		} else {
			command.addParam("paintGeometries", true);
		}
		command.addParam("paintLabels", true);

		this.deferred = geomajasConfig.dispatcher.execute(command);
		log.info("Tile.fetch : "+this.code);
		this.deferred.addCallback(this,
			function(json){
				try {
					var features = this._fromJSON(json.tile);
					if (features) {
						for (var i=0; i<features.length; i++) {
							this.cache.features.add(features[i].getLocalId(), features[i]);
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

				var tempUrl = json.labelImage;
				if (tempUrl.startsWith("http")){
					this.labelImage.setUrl(json.labelContent);
				} else {
					this.labelImage.setUrl(geomajasConfig.serverBase + json.labelContent);
				}                                                   
				this.code = new TileCode();
				this.code.fromJSON(json.code);

				this.labelImage.setBounds(new Bbox(0, 0, json.screenWidth, json.screenHeight));
				this.labelImage.setLevel(this.code.tileLevel);
				this.labelImage.setXIndex(this.code.x);
				this.labelImage.setYIndex(this.code.y);
			}
		}

		return this.inherited("_fromJSON", arguments);
	}
});
 
