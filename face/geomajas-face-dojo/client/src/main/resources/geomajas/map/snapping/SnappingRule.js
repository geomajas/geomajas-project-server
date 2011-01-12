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

dojo.provide("geomajas.map.snapping.SnappingRule");
dojo.declare("SnappingRule", null, {

	/**
	 * @fileoverview General definition of a snapping rule.
	 * @class General definition of a snapping rule.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @param distance Snapping distance.
	 * @param layer Layer of the target snapping point.
	 * @param type Snapping type.
	 */
	constructor : function (distance, layer, type) {
		this.distance = distance;
		this.layer = layer;
		this.type = type;
	},
	
	getDistance : function () {
		return this.distance;
	},
	
	setDistance : function (distance) {
		this.distance = distance;
	},

	getLayer : function () {
		return this.layer;
	},
	
	setLayer : function (layer) {
		this.layer = layer;
	},

	getType : function () {
		return this.type.value;
	},
	
	setType : function (type) {
		this.type = type;
	}
});