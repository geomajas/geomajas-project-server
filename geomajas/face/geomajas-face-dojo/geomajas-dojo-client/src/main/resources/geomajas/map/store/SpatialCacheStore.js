dojo.provide("geomajas.map.store.SpatialCacheStore");
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
dojo.require("geomajas.map.store.FeatureStore");
dojo.require("geomajas.spatial.common");
dojo.require("dojox.collections.Dictionary");

dojo.declare("SpatialCacheStore", FeatureStore , {

	/**
	 * @class Implementation of a FeatureStore that gets it's elements remotely.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends FeatureStore
	 * @param layer The layer this store is meant for.
	 */
	constructor : function (layer) {
		this.layer = layer;
		this.cache = new RenderingIndependentTileCache(
			this.layer,
			this.layer.getMaxExtent().clone()
		);

		this.cleared = true;
	},

	getElements : function () {
		return this.cache.features;
	},

	getFeatureById : function (/*String (localId!!)*/id) {
		var features = this.getElements();
		var feature = null;
		if (features.contains(id)) {
			feature = features.item(id);
		}
		return feature;
	},

	contains : function (/*String (localId!!)*/id) {
		var features = this.getElements();
		return features.contains(id);
	},

	addElement : function (feature) {
		var features = this.getElements();
		features.add(feature.getLocalId(), feature);
	},

	removeElement : function (feature) {
		var features = this.getElements();
		if (features.contains(feature.getLocalId())) {
			features.remove(feature.getLocalId());
		}
	},

	clear : function () {
		if(!this.cleared){
			log.info ("Clearing cache for layer "+this.layer.getId());
			this.cache.clear();
			this.cleared = true;
		}	
	},

	isCleared : function () {
		return this.cleared;
	},

	getElementCount : function () {
		var features = this.getElements();
		return features.count;
	},

	/**
	 * Apply a callback function on every feature within certain bounds.
	 * @param bounds The bounds wherein to look for features.
	 * @param filter The CQL filter to apply.
	 * @callback Javascript function object. Should accept a feature as
	 *           argument.
	 */
	applyAndSync : function (/*Bbox*/bounds, /*String*/filter, /*Function*/onDelete, /*Function*/onUpdate) {
		this.cleared = false;
		this.cache.queryAndSync(bounds, filter, onDelete, onUpdate);
	},
		
	applyOnBounds : function (/*Bbox*/bounds, /*Function*/callback) {
		this.cleared = false;
		this.cache.query(bounds, callback);
	}
});
