dojo.provide("geomajas.map.store.InMemoryStore");
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
dojo.require("dojox.collections.Dictionary");


dojo.declare("InMemoryStore", FeatureStore , {

	/**
	 * @class FeatureStore implementation that stores all elements in memory.
	 * A layer uses this for storing it's selection.
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 * @extends FeatureStore
	 */
	constructor : function () {
		this.elements = new dojox.collections.Dictionary();
		// Quadtree or Rtree...
	},

	getElements : function () {
		return this.elements;
	},

	addElement : function (element) {
		if (!this.elements.contains(element.getId())) {
			this.elements.add (element.getId(), element);
		}
	},

	removeElement : function (element) {
		if (this.elements.contains(element.getId())) {
			this.elements.remove (element.getId());
		}
	},

	getFeatureById : function (/*String*/id) {
		if (this.elements.contains(id)) {
			return this.elements.item(id);
		}
		return null;
	},

	contains : function (/*String*/id) {
		return this.elements.contains(id);
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
		this.applyOnBounds(bounds,onUpdate);
	},
	
	applyOnBounds : function (/*Bbox*/bounds, callback) {
		// Fetch, iterate and execute callback with feature as argument.
	},

	getElementCount : function () {
		return this.elements.count;
	}
});