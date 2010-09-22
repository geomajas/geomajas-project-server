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

dojo.provide("geomajas.map.workflow.Workflow");
dojo.require("geomajas.map.workflow.Activity");
dojo.require("geomajas._base");

dojo.declare("Workflow", Activity, {

	/**
	 * @fileoverview Main activity. Workflow starts after editing.
	 * @class The workflow representation. This class is also an implementation
	 * of the {@link Activity} interface. The activity was a tree-like
	 * structure wherein each activity could contain sub-activities. Well, the
	 * top node of such an activity tree, is this workflow object.
	 * @author Pieter De Graef
	 * @constructor
	 * @extends Activity
	 */
	constructor : function () {
		this.dataObject = null;
		this.featureTransaction = null;
	},

	/**
	 * Does nothing...
	 */
	doOnWait : function () {
		log.info ("Workflow:doOnWait");
	},

	/**
	 * Go over all activities, and execute them one by one.
	 */
	start: function () {
		log.info ("Workflow:start");
		if (this.currentActivity == null) {
			this.setCurrentActivity(this.getNextActivity());
		}
		while (this.currentActivity != null) {
			this.currentActivity.setFeatureTransaction(this.featureTransaction);
			this.currentActivity.start();
			this.setCurrentActivity(this.currentActivity.getNextActivity());
		}
	},

	/**
	 * When the workflow has been halted, this function picks up again, and
	 * continues the work.
	 */
	resume : function () {
		log.info ("Workflow:resume");
		if (this.currentActivity == null) {
			log.warn ("cannot resume, no currentActivity");
			return;
		}
		if (this.getState() == geomajas.ActivityState.WAITING) {
			this.currentActivity.doOnWait();
		} else {
			this.currentActivity.resume();
			this.setCurrentActivity(this.currentActivity.getNextActivity());
			this.start();
		}
	},

	getDataObject : function () {
		return this.dataObject;
	},

	setDataObject : function (dataObject) {
		this.dataObject = dataObject;
	},

	getFeatureTransaction : function () {
		return this.featureTransaction;
	},

	setFeatureTransaction : function (featureTransaction) {
		this.featureTransaction = featureTransaction;
	}
});