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

dojo.provide("geomajas.spatial.cache.SpatialNode");
dojo.require("geomajas.gfx.PainterVisitable");

dojo.declare("SpatialNode", PainterVisitable, {

	status : {
		EMPTY   : "Empty",
		LOADING : "Loading",
		LOADED  : "Loaded" 
	},

	/**
	 * @fileoverview Definition of a SpatialNode.
	 * @class Definition of a SpatialNode. A spatial node is a unique placeholder in the spatial cache.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function (code, bbox, cache) {
		/**
		 * Some implementation of the SpatialCode interface. Each Node in a
		 * spatial cache needs a unique code.
		 */
		this.code = code;

		/** Reference to the {@link SpatialCache}. */
		this.cache = cache;

		/**
		 * Each spatial node has bounds attached to it. Usually the spatial
		 * cache will know how to calculate bounds for a specific spatial code. 
		 */
		this.bbox = bbox;

		/** The deferred for fetching the data. */
		this.deferred = null;
	},



	//-------------------------------------------------------------------------
	// PainterVisitable implementation:
	//-------------------------------------------------------------------------

	/**
	 * Return a unique id, that can be used for rendering.
	 */
	getId : function () {
		return this.cache.layer.getId() + ".features." + this.code.toString();
	},

	/**
	 * Accept function for the painter visitor.
	 * @param visitor The PainterVisitor object that stores all Painters.
	 * @param bbox Map bounding box.
	 * @param recursive .......TODO (changes too much atm :-))
	 */
	accept : function (/*PainterVisitor*/visitor, /*Bbox*/ bbox, /*Boolean*/recursive) {
		visitor.visit(this);
	},
	
	apply : function (callback) {
		if(this.getStatus() == this.status.LOADED) {
			callback(this);
		} else if(this.getStatus() == this.status.LOADING){
			this.deferred.addCallback(this,
				function(json){
					callback(this);
					return json;
				}
			);
		}
	},



	//-------------------------------------------------------------------------
	// SpatialNode interface:
	//-------------------------------------------------------------------------

	/**
	 * Return the unique {@link SpatialCode} for this node.
	 */
	getCode : function () {
		return this.code;
	},

	/**
	 * Returns all features in this node in an array.
	 */
	getFeatures : function () {
		return [];
	},

	/**
	 * Fetch all data related to this node.
	 * @param filter When fetching it is possible to filter the data with this
	 *               filter object. Null otherwise.
	 * @param callback When this node's data comes from the server, it will be
	 *                 handled by this callback function.
	 */
	fetch : function (filter, callback) {
	},

	/**
	 * This node's bounding box.
	 */
	getBounds : function() {
		return this.bbox;
	},

	/**
	 * Returns the deferred object, responsible for fetching the data for this
	 * Tile.
	 */
	getDeferred : function () {
		return this.deferred;
	},

	/**
	 * Returns the current status of this node. Can be one of the following:
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
		var features = getFeatures();
		if (features == null || features.length == 0) {
			return this.status.LOADING;
		}
		return this.status.LOADED;
	}
}); 