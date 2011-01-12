dojo.provide("geomajas.map.store.LayerStore");
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

dojo.declare("LayerStore", null, {

	/**
	 * @class Definition of a FeatureStore. Feature collection handling...
	 * @author Pieter De Graef
	 *
	 * @constructor
	 */
	constructor : function () {
	},

	/**
	 * Does this store currently contain the feature with given id?
	 * @param id A feature's identifier.
	 * @returns Return true or false.
	 */
	contains : function (/*String*/id) {
		return false;
	},

	/**
	 * Add a new feature to the underlying datastructure.
	 * @param element Feature object.
	 */
	addElement : function (element) {
	},

	/**
	 * Remove a feature from the underlying datastructure. (if it's in there)
	 * @param element Feature object.
	 */
	removeElement : function (element) {
	},

	clear : function () {
	}

});