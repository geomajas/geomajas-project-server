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

dojo.provide("geomajas.map.workflow.activities.CommitActivity");
dojo.require("geomajas._base");
dojo.require("geomajas.map.workflow.Activity");

dojo.declare("CommitActivity", Activity, {

	/**
	 * @fileoverview Activity for persistence.
	 * @class Activity that persists the {@link FeatureTransaction} object to
	 * the server.
	 * @author Pieter De Graef
	 * @constructor
	 * @extends Activity
	 * @param editingTopic The topic to publish progress on.
	 * @param dispatcher The command dispatcher used to send a persistence
	 *                   command to the server.
	 */
	constructor : function (editingTopic, dispatcher) {
		this.editingTopic = editingTopic;
		this.dispatcher = dispatcher;
		this.featureTransaction = null;
	},

	/**
	 * There is no next activity!
	 * @return null.
	 */
	getNextActivity : function () {
		return null;
	},

	/**
	 * Persist to database, and publish.
	 */
	start : function () {
		log.info("CommitActivity:start");
		this.state = geomajas.ActivityState.RUNNING;
		var command = new JsonCommand("command.feature.PersistTransaction",
                "org.geomajas.command.dto.PersistTransactionRequest", null, false);
		command.addParam("featureTransaction", this.featureTransaction.toJSON());
		command.addParam("crs", this.featureTransaction.getCrs());
		var deferred = this.dispatcher.execute(command);
		deferred.addCallback(this, "_callback");
	},

	/**
	 * @private
	 */
	_callback : function (result) {
		try {
			log.info ("CommitActivity:_callback, topic: "+this.editingTopic);
			this.state = geomajas.ActivityState.COMPLETED;

			if (result.errorMessages.list.length == 0) {
				var ft = new FeatureTransaction(null, null, null);
				ft.fromJSON(result.featureTransaction, this.featureTransaction.getLayer(), this.featureTransaction.getCrs());
				dojo.publish(this.editingTopic, [ "commit", ft ]);
			}
		} catch(e){
			for(var word in e){
				log.error(e[word]);
			}
		}

		return result;
	},

	resume : function () {
		log.info ("CommitActivity:resume");
	},

	getDataObject : function () {
	},

	setDataObject : function (dataObject) {
	}
});