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

