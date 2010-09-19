dojo.provide("geomajas.map.store.FeatureStore");
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