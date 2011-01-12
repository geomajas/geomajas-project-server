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