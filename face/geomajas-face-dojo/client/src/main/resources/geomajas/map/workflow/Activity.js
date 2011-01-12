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