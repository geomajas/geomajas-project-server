dojo.provide("geomajas.map.store.InMemoryStore");
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
		if (!this.elements.contains(element.getLocalId())) {
			this.elements.add (element.getLocalId(), element);
		}
	},

	removeElement : function (element) {
		if (this.elements.contains(element.getLocalId())) {
			this.elements.remove (element.getLocalId());
		}
	},

	getFeatureById : function (/*String*/id) {
		var pos = id.lastIndexOf(".");
		if (pos >= 0) {
			id = id.substring(pos + 1);
		}
		if (this.elements.contains(id)) {
			return this.elements.item(id);
		}
		return null;
	},

	contains : function (/*String*/id) {
		var pos = id.lastIndexOf(".");
		if (pos >= 0) {
			id = id.substring(pos + 1);
		}
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