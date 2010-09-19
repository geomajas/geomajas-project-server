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

dojo.provide("geomajas.map.workflow.activities.LayerValidationActivity");
dojo.require("geomajas._base");
dojo.require("geomajas.map.workflow.Activity");

dojo.declare("LayerValidationActivity", Activity, {

	/**
	 * @fileoverview Activity for validating the featuretransaction.
	 * @class Should be validating, be that is not implemented yet.
	 * Instead it simply publishes a validation event on the topic.
	 * @author Pieter De Graef
	 * @constructor
	 * @extends Activity
	 * @param editingTopic The topic to publish progress on.
	 */
	constructor : function (editingTopic) {
		this.valid = false;
	},

	/**
	 * No children, so the next activity, is the parent's next.
	 */
	getNextActivity : function () {
		if(this.valid){
			return this.parent.getNextActivity(); // no children
		} else {
			alert("The geometry is not valid!");
		}
	},

	start : function () {
		log.info ("LayerValidationActivity:start");
		this._validate();
		this.state = geomajas.ActivityState.COMPLETED;
		if(this.valid){
			dojo.publish(this.editingTopic, [ "validate", this.getFeatureTransaction() ]);
		}
	},

	resume : function () {
		log.info ("LayerValidationActivity:resume");
	},

	/**
	 * @private
	 */
	_validate : function(){
		this.valid = true;
		if (this.getFeatureTransaction().isGeometryChanged()) {
			var newFeatures = this.getFeatureTransaction().getNewFeatures();
			if(newFeatures){
				for (var i = 0; i< newFeatures.length; i++) {
					var feature = newFeatures[i];
					var geometry = feature.getGeometry();
					if(!geometry.isValid()){
						this.valid = false;
					}
					if (geometry.isEmpty()) {
						this.valid = false;
					}
					if (this._hasEmptyRing(geometry)) {
						this.valid = false;
					}
				}
			}	
		}
	},

	/**
	 * @private
	 */
	_hasEmptyRing : function (geometry) {
		if (geometry instanceof Polygon) {
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				var ring = geometry.getGeometryN(i);
				if (ring.isEmpty()) {
					return true;
				}
			}
		} else if (geometry instanceof MultiPolygon) {
			for (var i=0; i<geometry.getNumGeometries(); i++) {
				var polygon = geometry.getGeometryN(i);
				if (this._hasEmptyRing(polygon)) {
					return true;
				}
			}
		}
		return false;
	}
});