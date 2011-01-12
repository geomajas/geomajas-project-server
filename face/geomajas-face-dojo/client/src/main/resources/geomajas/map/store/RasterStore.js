dojo.provide("geomajas.map.store.RasterStore");
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

dojo.require("geomajas.map.store.LayerStore");

dojo.declare("RasterStore", LayerStore, {

	/**
	 * @class Definition of a RasterStore. Raster collection handling...
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * Apply a callback function on every tile within certain bounds.
	 * @param bounds The bounds wherein to look for tiles.
	 * @callback onDelete function object. Should accept a tile as
	 *           argument.
	 * @callback onUpdate function object. Should accept a tile as
	 *           argument.
	 */
	applyAndSync : function (/*Bbox*/bounds, /*Function*/onDelete, /*Function*/onUpdate) {
		// Fetch, iterate and execute callback with feature as argument.
		dojo.unimplemented('RasterStore.applyAndSync');
	},
	
	/**
	 * return the total number of raster images in all nodes.
	 */
	getImageCount : function () {
	},
	
	/**
	 * clear the cache.
	 */
	clear : function () {
	},

	/**
	 * return an array of all nodes.
	 */
	getNodes : function () {
	}
			
});