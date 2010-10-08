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