dojo.provide("geomajas.map.FeatureReference");
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

dojo.declare("FeatureReference", null, {

	queryType : 1,

	/**
	 * This option can only be used in case of an intersects query. It accepts features whose geometry intersect with
	 * the given geometry for at least the given ratio. This number must always be a value between 0 and 1.
	 */
	ratio : -1,

	/**
	 * A list of layers we want to retrieve features from.
	 */
	layerIds : null,

	/**
	 * The type of search. Can be either 1 (=SEARCH_FIRST_LAYER), or 2 (=SEARCH_ALL_LAYERS).
	 */
	searchType : 1,

	/**
	 * The optional buffer that should be added around the location before executing the search.
	 */
	buffer : -1,

	featureIncludes : 0x7fff,

	position : null,

	constructor : function (event) {
		if (event != null) {
			this.position = event.getPosition();
		}
	},

	/**
	 * Called when all the features have been located by the MapModel.
	 * @param features Array of feature objects.
	 */
	onSetFeatureResult : function (features) {
	},

	getPosition : function() {
		return this.position;
	},

	getSearchType : function () {
		return this.searchType;
	},

	setSearchType : function (searchType) {
		this.searchType = searchType;
	},

	getQueryType : function () {
		return this.queryType;
	},

	setQueryType : function () {
		this.queryType = queryType;
	},

	getLayerIds : function () {
		return this.layerIds;
	},

	setLayerIds : function (layerIds) {
		this.layerIds = layerIds;
	},

	getBuffer : function (){
		return this.buffer;
	},

	setBuffer : function (buffer) {
		this.buffer = buffer;
	},

	getRatio : function () {
		return this.ratio;
	},

	setRatio : function (ratio) {
		this.ratio = ratio;
	},

	getFeatureIncludes : function () {
		return this.featureIncludes;
	},

	setFeatureIncludes : function (featureIncludes) {
		this.featureIncludes = featureIncludes;
	}
});
