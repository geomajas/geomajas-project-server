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

dojo.provide("geomajas.map.workflow.Activity");
dojo.require("geomajas._base");

dojo.declare("Activity", null, {

	/**
	 * @fileoverview Base workflow activity implementation.
	 * @class A workflow consists of a predetermined number of steps. These
	 * steps are called Activities. So basically, an implementation of this
	 * "interface" is one step in the workflow.<br/>
	 * The activities can have sub-activities, and are ordered in a tree-like
	 * structure.
	 * 
	 * @author Pieter De Graef
	 * @constructor
	 */
	constructor : function () {
		/** State can be RUNNING, WAITING or COMPLETED. */
		this.state;

		/** Current sub-activity busy. */
		this.currentActivity = null;

		/** Reference to the parent activity. */
		this.parent = null;

		/** Array of sub-activities. */
		this.subActivities = [];

		/** Featuretransaction object. */
		this.featureTransaction = null;
	},

	/**
	 * Since the order of steps is predetermined, an activity should be able to
	 * say, what the next step is.
	 * @return An Activity object.
	 */
	getNextActivity : function () {
		if (this.currentActivity != null && this.currentActivity.getState() == geomajas.ActivityState.WAITING) {
			return null;
		}
		if (this.currentActivity != null) {
			for (var i=0; i<this.subActivities.length-1; i++) {
				if (this.currentActivity.declaredClass == this.subActivities[i].declaredClass) {
					return this.subActivities[i+1];
				}
			}
		} else {
			return this.subActivities[0];
		}
		return null;
	},

	/**
	 * Start the activity.
	 */
	start : function () {
	},

	/**
	 * Sometimes an activity has to be put on hold (for example, waiting for
	 * user input). After that, it should be possible to resume. While on hold
	 * the state equals WAITING.
	 */
	resume : function () {
	},

	// Getters and setters:

	getState : function () {
		return this.state;
	},

	setState : function (state) {
		this.state = state;
	},

	getDataObject : function () {
	},

	setDataObject : function (dataObject) {
	},

	getFeatureTransaction : function () {
		return this.featureTransaction;
	},

	setFeatureTransaction : function (featureTransaction) {
		this.featureTransaction = featureTransaction;
	},

	getSubActivities : function () {
		return this.subActivities;
	},

	setSubActivities : function (subActivities) {
		this.subActivities = subActivities;
	},

	getCurrentActivity : function () {
		return this.currentActivity;
	},

	setCurrentActivity : function (currentActivity) {
		this.currentActivity = currentActivity;
	},

	getParent : function () {
		return this.parent;
	},

	setParent : function (parent) {
		this.parent = parent;
	}

});