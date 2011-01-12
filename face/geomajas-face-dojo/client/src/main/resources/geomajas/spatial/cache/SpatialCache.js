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

dojo.provide("geomajas.spatial.cache.SpatialCache");
dojo.declare("SpatialCache", null, {

	/**
	 * @fileoverview Definition of a SpatialCache. Caches objects based on bounds.
	 * @class Definition of a SpatialCache. Caches objects based on bounds.
	 * @author Jan De Moerloose & Pieter De Graef
	 *
	 * @constructor
	 * @param layer Reference to the VectorLayer.
	 * @param bbox The maximum bounding box for the cache.
	 */
	constructor : function (layer, bbox) {
		/** Reference to the VectorLayer. */
		this.layer = layer;

		/** The maximum bounding box for the cache. */
		this.bbox = bbox;

		/** SpatialNode dictionary. Uses SpatialCode.toString() as key. */
		this.nodes = new dojox.collections.Dictionary();
	},



	//-------------------------------------------------------------------------
	// Spatial Cache interface :
	//-------------------------------------------------------------------------

	/**
	 * Return a dictionary of all features in the cache.
	 */
	getFeatures : function () {
		return new dojox.collections.Dictionary();
	},

	/**
	 * Returns an array of all features in a certain {@link SpatialNode}. This
	 * node is selected through it's unique {@link SpatialCode}.
	 * @param code A {@link SpatialCode} instance. If the node with this code is
	 *             not present, an empty array will be returned.
	 */
	getFeaturesByCode : function (code) {
		return [];
	},

	/**
	 * Clear the entire cache! This will also cancel all currently undergoing
	 * fetch operations!
	 */
	clear : function () {
		log.debug("SpatialCache.clear for layer "+this.layer.getId());
		this.cancelDeferred();
		this.nodes = new dojox.collections.Dictionary();
	},
	
	cancelDeferred :  function () {
		log.debug("SpatialCache.cancelDeferred for layer "+this.layer.getId());
		var keys = this.nodes.getKeyList();
		for (var i=0; i<keys.length; i++) {
			var node = this.nodes.item(keys[i]);
			if (node.getDeferred() != null) {
				try {
					node.getDeferred().cancel();
				} catch (e){
					log.error("SpatialCache.clear : exception cancelling deferreds!");
					for (var i in e) log.error(e[i]);
				}
			}
		}
	},

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
	},
	
	/**
	 * Make a certain query on the cache. The cache will go over all nodes
	 * intersecting the given bounding box. If some of these nodes are not yet
	 * present in the cache, they will be fetched with the given filter.
	 * @param bbox The bounding bow wherein we are to search for nodes, and
	 *             apply the callback function.
	 * @param callback The callback function to be applied on the nodes
	 *                 intersecting the given bounding box. This function
	 *                 should take a node as parameter.
	 */
	query : function (bbox, callback) {
		
	},

	/**
	 * Returns the VectorLayer-reference.
	 */
	getLayer : function () {
		return this.layer;
	},

	/**
	 * Returns an array of all the SpatialCodes of the nodes currently present
	 * in the cache.
	 */
	getNodeList : function () {
		var codeArray = [];
		var keys = this.nodes.getKeyList();
		for (var i=0; i<keys.length; i++) {
			codeArray.push(this.nodes.item(keys[i]).getCode());
		}
		return codeArray;
	}
	
	
});

