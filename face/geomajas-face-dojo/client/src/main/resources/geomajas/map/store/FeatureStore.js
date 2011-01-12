dojo.provide("geomajas.map.store.FeatureStore");
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

dojo.declare("FeatureStore", LayerStore, {

	/**
	 * @class Definition of a FeatureStore. Feature collection handling...
	 * @author Pieter De Graef & Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * Return a feature by it's id. Null if it's not in here.
	 */
	getFeatureById : function (/*String*/id) {
		dojo.unimplemented('FeatureStore.getFeatureById');
		return null; // a feature
	},

	/**
	 * Does this store currently contain the feature with given id?
	 * @param id A feature's identifier.
	 * @returns Return true or false.
	 */
	contains : function (/*String*/id) {
		dojo.unimplemented('FeatureStore.contains');
		return false; // a boolean
	},

	/**
	 * Apply a callback function on every feature within certain bounds.
	 * @param bounds The bounds wherein to look for features.
	 * @param filter The CQL filter to apply.
	 * @callback Javascript function object. Should accept a feature as
	 *           argument.
	 */
	applyAndSync : function (/*Bbox*/bounds, /*String*/filter, /*Function*/onDelete, /*Function*/onUpdate) {
		// Fetch, iterate and execute callback with feature as argument.
		dojo.unimplemented('FeatureStore.applyAndSync');
	},
	
	applyOnBounds : function (/*Bbox*/bounds, callback) {
		// Fetch, iterate and execute callback with feature as argument.
		dojo.unimplemented('FeatureStore.applyOnBounds');
	},

	getElementCount : function () {
	},
	
	getElements : function () {
	},

	getNodes : function () {
	},
	
	clear : function () {
	},

	isCleared : function () {
	}

});