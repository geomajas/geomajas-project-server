dojo.provide("geomajas.spatial.cache.TileCache");
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
dojo.require("geomajas.spatial.cache.SpatialCache");
dojo.require("dojox.collections.ArrayList");

dojo.declare("TileCache", SpatialCache, {

	/**
	 * @fileoverview Cache system using Tiles.
	 * @class Cache system using Tiles.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @param layer Reference to the VectorLayer.
	 * @param bbox The maximum bounding box for the cache.
	 */
	constructor : function (layer, bbox) {
		/** Feature dictionary. Uses feature.getId() as key. */
		this.features = new dojox.collections.Dictionary();


		/** @private Lastly calculated array of tilecodes. */
		this.currentTileCodes = null;

		/** @private Lastly calculated tilelevel. */
		this.currentLevel = 0;

		/** @private Lastly calculated minimum x index. */
		this.currentMinX = 0;

		/** @private Lastly calculated minimum y index. */
		this.currentMinY = 0;

		/** @private Lastly calculated maximum x index. */
		this.currentMaxX = 0;

		/** @private Lastly calculated maximum y index. */
		this.currentMaxY = 0;
		
		this.nrDeferred = 0;
	},



	//-------------------------------------------------------------------------
	// SpatialCache implementation :
	//-------------------------------------------------------------------------

	/**
	 * Return a dictionary of all features in the cache.
	 */
	getFeatures : function () {
		return this.features;
	},

	/**
	 * Returns an array of all features in a certain {@link Tile}. This tile is
	 * selected through it's unique code.
	 * @param code A {@link TileCode} instance. If the tile with this code is
	 *             not present, an empty array will be returned.
	 */
	getFeaturesByCode : function (code) {
		var array = [];
		var tile = this.nodes.item(code.toString());
		if (code != null && tile != null) {
			var featureIds = tile.getFeatureIds();
			for (var i=0; i<featureIds.length; i++) {
				array.push(this.features.item(featureIds[i]));
			}
		}
		return array;
	},
	
	/**
	 * Adds the tile with the specified code to the cache or simply returns the tile
	 * if it's already in the cache.
	 * @param code A {@link TileCode} instance.
	 */
	addTile : function (code) {
		var tile = this.nodes.item(code);
		if(tile == null){
			tile = new Tile(code, this._calcBoundsForTileCode(code), this);
			this.nodes.add(code.toString(), tile);
		} 
		return tile;
	},

	/**
	 * Make a certain query on the cache. The cache will go over all tiles
	 * intersecting the given bounding box. If some of these tiles are not yet
	 * present in the cache, they will be fetched with the given filter.
	 * @param bbox The bounding bow wherein we are to search for tiles, and
	 *             apply the callback function.
	 * @param filter In case a tile is missing, it is fetched with this extra
	 *               filter. Is usually null though.
	 * @param callback The callback function to be applied on the tiles
	 *                 intersecting the given bounding box. This function
	 *                 should take a tile as parameter.
	 */
	queryAndSync : function (bbox, filter, onDelete, onUpdate) {
		this.nrDeferred = 0;
		this.currentTileCodes = this.calcCodesForBounds(bbox);
		for (var i=0; i<this.currentTileCodes.length; i++) {
			if (!this.nodes.contains(this.currentTileCodes[i])) {
				var tile = new Tile(this.currentTileCodes[i], this._calcBoundsForTileCode(this.currentTileCodes[i]), this);
				this.nodes.add(this.currentTileCodes[i].toString(), tile);
				tile.fetch(filter, onUpdate);
			} else {
				var tile = this.nodes.item(this.currentTileCodes[i]);
				onUpdate(tile);
			}
		}
		// just call the fetch done if nothing needs to be fetched ! 
		if(this.nrDeferred == 0){
			this.onFetchDone(this.layer.getId());
		}
		log.error ("TileCache : queryAndSync => nrDererred=" + this.nrDeferred);
	},

	/**
	 * Make a certain query on the cache. The cache will go over all nodes
	 * intersecting the given bounding box and all nodes (or tiles) containing a feature
	 * that extends into one of these nodes (features are assigned to 1 node/tile but can 
	 * span over several nodes). If some of these nodes are not yet
	 * present in the cache, they will NOT be fetched.
	 * @param bbox The bounding box wherein we are to search for nodes, and
	 *             apply the callback function.
	 * @param callback The callback function to be applied on the nodes
	 *                 intersecting the given bounding box. This function
	 *                 should take a node as parameter.
	 */
	query : function (bbox, callback) {
		var codes = this.calcCodesForBounds(bbox);
		var setTiles = new dojox.collections.ArrayList([]);
		/* construct set of unique tiles */
		for (var i=0; i< codes.length; i++) {
			if (this.nodes.contains(codes[i])) {
				var tile = this.nodes.item(codes[i]);
				/* Also process tiles of features that partly lie in this tile but were assigned
				to another tile */
				if (!setTiles.contains(tile)) {
					setTiles.add(tile);
				}
				var codesExtraFeatures = tile.getCodes();
				for(var j = 0; j < codesExtraFeatures.length; j++){
					/* This tile will normaly also be cached */
					if (this.nodes.contains(codesExtraFeatures[j])) {
						var tileExtraFeature = this.nodes.item(codesExtraFeatures[j]);
						if (!setTiles.contains(tileExtraFeature)) {
							setTiles.add(tileExtraFeature);
						}
					}
				}
			}
		}
		for (var i=0; i< setTiles.count; i++) {						
			callback(setTiles.item(i));	
		}
	},

	/**
	 * Clear the entire cache! This will also cancel all currently undergoing
	 * fetch operations!
	 */
	clear : function () {
		this.inherited("clear", arguments);
		this.features = new dojox.collections.Dictionary();
	},

	onTileFetching : function () {
		this.nrDeferred++;
	},

	onTileFetched : function (executionTime){
		if (this.nrDeferred > 0) {
			this.nrDeferred--;
		}
		if (this.nrDeferred == 0) {
			this.onFetchDone(this.layer.getId());
		}
	},

	onFetchDone : function (layerId){
		log.info ("TileCache : onFetchDone is called from layer "+layerId);
	},

	//-------------------------------------------------------------------------
	// Private functions:
	//-------------------------------------------------------------------------

	/**
	 * @private
	 * Find the most suitable tile-level for the given bounds.
	 * @param bounds Bounding box of the currently visible map area.
	 */
	_findTileLevel : function(bounds) {
		var depth = 0;
		var baseX = this.bbox.width;
		var baseY = this.bbox.height;
		while(baseX > bounds.getWidth() && baseY > bounds.getHeight() && depth < 256){
			depth++;
			baseX /= 2.0;
			baseY /= 2.0;	
		}
		return depth;
	},

	/**
	 * @private
	 * Returns the bounds for the given TileCode object.
	 */
	_calcBoundsForTileCode : function (code) {
		var div = Math.pow(2, code.getTileLevel());
		// must round tile width/height to an integer number of pixels !
		var scale = this.layer.getMapModel().getMapView().getCurrentScale();
		var tileWidth = Math.ceil((scale * this.bbox.width)/ div)/scale;
		var tileHeight = Math.ceil((scale * this.bbox.height)/ div)/scale;
		var x = this.bbox.x + code.getX() * tileWidth;
		var y = this.bbox.y + code.getY() * tileHeight;
		return new Bbox(x, y, tileWidth, tileHeight);
	},


	/**
	 * @private
	 * Saves the complete array of TileCode objects for the given bounds.
	 */
	calcCodesForBounds : function (bounds) {
		var bbox = bounds.intersection(this.bbox); // clip screenbounds to maxbounds.
		// see optimizeForBbox !		
		var div = Math.pow(2, this.currentLevel);
		// must round tile width/height to an integer number of pixels !
		var scale = this.layer.getMapModel().getMapView().getCurrentScale();
		var tileWidth = Math.ceil((scale * this.bbox.width)/ div)/scale;
		var tileHeight = Math.ceil((scale * this.bbox.height)/ div)/scale;
	
		// Calculate bounds relative to extents.
		var relativeBoundX = Math.abs(bbox.x - this.bbox.x);
		var relativeBoundY = Math.abs(bbox.y - this.bbox.y);
		
		this.currentMinX = Math.floor(relativeBoundX / tileWidth);
		this.currentMinY = Math.floor(relativeBoundY / tileHeight);
		
		this.currentMaxX = Math.ceil((relativeBoundX + bbox.width ) / tileWidth) -1;
		this.currentMaxY = Math.ceil((relativeBoundY + bbox.height) / tileHeight) -1;
		
		var codes = [];
		// TODO: Hack because dojo sends a resize event when a hidden map 
		// (size = 0) gets is inside a resized component. And we dont want
		// to start calulating Infinity.
		if(tileWidth == 0 || tileHeight == 0) {
			return codes;
		}
        for (var i=this.currentMinX; i<=this.currentMaxX; i++) {
            for (var j=this.currentMinY; j<=this.currentMaxY; j++) {
                 var tileCode = new TileCode(this.currentLevel, i, j);
                 codes.push(tileCode);
            }
        }
		return codes;
	}
});