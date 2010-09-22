dojo.provide("geomajas.map.store.RasterStore");
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