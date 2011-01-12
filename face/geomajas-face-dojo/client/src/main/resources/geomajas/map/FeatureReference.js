dojo.provide("geomajas.map.FeatureReference");
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
