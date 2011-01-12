dojo.provide("geomajas.map.store.SpatialCacheStore");
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

	getFeatureById : function (/*String*/id) {
		var features = this.getElements();
		var feature = null;
		if (features.contains(id)) {
			feature = features.item(id);
		}
		return feature;
	},

	contains : function (/*String*/id) {
		var features = this.getElements();
		return features.contains(id);
	},

	addElement : function (feature) {
		var features = this.getElements();
		features.add(feature.getId(), feature);
	},

	removeElement : function (feature) {
		var features = this.getElements();
		if (features.contains(feature.getId())) {
			features.remove(feature.getId());
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
