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