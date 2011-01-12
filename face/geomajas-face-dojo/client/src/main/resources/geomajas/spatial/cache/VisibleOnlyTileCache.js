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

dojo.provide("geomajas.spatial.cache.VisibleOnlyTileCache");
dojo.require("geomajas.spatial.cache.SpatialCache");

dojo.declare("VisibleOnlyTileCache", TileCache, {

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
	},


	//-------------------------------------------------------------------------
	// TileCache overriding :
	//-------------------------------------------------------------------------

	/**
	 * Make a certain query on the cache. The cache will go over all nodes
	 * intersecting the given bounding box. If some of these nodes are not yet
	 * present in the cache, they will be fetched with the given filter.
	 * @param bbox The bounding bow wherein we are to search for nodes, and
	 *             apply the callback function.
	 * @param filter In case a node is missing, it is fetched with this extra
	 *               filter. Is usually null though.
	 * @param onDelete The callback function to be applied on the nodes
	 *                 deleted by this sync. This function
	 *                 should take a node as parameter.
	 * @param onUpdate The callback function to be applied on the nodes
	 *                 intersecting the given bounding box. This function
	 *                 should take a node as parameter.
	 */
	queryAndSync : function (bbox, filter, onDelete, onUpdate) {
		if(!this.layer.getMapModel().getMapView().isPanning()){
			// delete all tiles
			var e = this.nodes.getIterator();
			while(e.get()) {
		 		var tile = e.element.value;
		 		onDelete(tile);
			}
			// cancel all previous requests if scale changes !!!
			this.clear();
			this.nrDeferred = 0;
		}
		if(bbox.intersects(this.bbox)) {
			// check level
			var bestLevel = this._findTileLevel(bbox);
			if(bestLevel != this.currentLevel){
				this.currentLevel = bestLevel;
			}
			// needed tile codes:
			var tileCodes = this.calcCodesForBounds(bbox);
			// make a clone here as we are going to modify the actual node map !
			var currentNodes = this.nodes.clone();
			for (var i=0; i<tileCodes.length; i++) {
				if (!currentNodes.contains(tileCodes[i])) {
					// add the node
					var tile = this.addTile(tileCodes[i]);
					// fetch it
					tile.fetch(filter,onUpdate);
					// fetch/callback connected nodes
					tile.applyConnected(filter,onUpdate);
				} else {
					var tile = currentNodes.item(tileCodes[i]);
					// apply deferred
					tile.apply(onUpdate);
					// add connected nodes
					tile.applyConnected(filter,onUpdate);				
				}
			}
		}
		// just call the fetch done if nothing needs to be fetched !
		if(this.nrDeferred == 0){
			this.onFetchDone(this.layer.getId());
		}
	}
		
});
