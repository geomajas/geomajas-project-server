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

	/**
	 * The feature's ID that can be used to search for a feature.
	 */
	featureId : null,

	/**
	 * If no feature ID is given, the position can be used.
	 */
	position : null,
	
	/**
	 * If the position if used in searching for features, a searchType can also
	 * be added. The type of search. Can be either 1 (=search for only one
	 * feature), or 2 (=search all layers for features).
	 */
	searchType : 1,

	/**
	 * If the position is used in searching for features, you may want to
	 * specify in what layers to search. If no layerId's are given, the search
	 * will go over all visible layers by default.
	 */
	layerIds : null,
	
	buffer : -1,

	constructor : function (object) {
		if (object != null) {
			if (object.featureId){
				this.featureId = object.featureId;
			} else if (object.event) {
				if (object.event.getTargetId() != "") {
					this.featureId = object.event.getTargetId();
					this.position = object.event.getPosition();
				}
			} else if (object.position) {
				this.position = object.position;
			}
			if (object.searchType) {
				this.searchType = object.searchType; // 1 or 2 !!
			}
			if (object.layerIds) {
				this.layerIds = object.layerIds; // an array!!!
			}
		}
	},

	/**
	 * Called when all the features have been located by the MapModel.
	 * @param features Array of feature objects.
	 */
	onSetFeatureResult : function (features) {
	},

	getFeatureId : function () {
		return this.featureId;
	},

	getPosition : function () {
		return this.position;
	},

	containsLayerId : function (layerId) {
		for (i in this.layerIds) { 
			if (layerId == this.layerIds[i]) 
				return true;
		}
		return false;
	},

	getSearchType : function () {
		return this.searchType;
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
	}
});
