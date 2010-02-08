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