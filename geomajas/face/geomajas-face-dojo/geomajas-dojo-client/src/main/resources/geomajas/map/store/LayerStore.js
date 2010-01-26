dojo.provide("geomajas.map.store.LayerStore");
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