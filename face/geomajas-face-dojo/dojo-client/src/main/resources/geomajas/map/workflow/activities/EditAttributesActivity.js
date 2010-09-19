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

dojo.provide("geomajas.map.workflow.activities.EditAttributesActivity");
dojo.require("geomajas._base");
dojo.require("geomajas.map.workflow.Activity");

dojo.declare("EditAttributesActivity", Activity, {

	constructor : function (dispatcher) {
		this.dialogID = "EditAttributesActivity:dialog";
		this.dispatcher = dispatcher;
		this.featureTransaction = null;
		
		this.nextActivity = null;
	},

	/**
	 * No children, so the next activity, is the parent's next.
	 */
	getNextActivity : function () {
		log.info ("EditAttributesActivity:getNextActivity");
		return this.nextActivity;
	},

	start : function () {
		dijit.registry.remove(this.dialogID);
		var temp = dijit.byId(this.dialogID);
		if (temp) {
			temp.destroy();
		}
		log.info("EditAttributesActivity:start");
		var feature = this.featureTransaction.getNewFeatures()[0];
		var dialog = new geomajas.widget.FeatureEditDialog({id:this.dialogID, title:"Edit attributes", feature:feature});
		dojo.connect (dialog, "execute", dojo.hitch(this, "_onExecute"));
		dialog.show();
	},

	resume : function () {
		log.info ("EditAttributesActivity:resume");
		this.nextActivity = this.parent.getNextActivity();
	},

	/**
	 * @private
	 */
	_onExecute : function () {
		this.parent.setCurrentActivity(this);
		this.parent.resume();
	}
});