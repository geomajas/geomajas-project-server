dojo.provide("geomajas.io.CommandDispatcher");
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
dojo.require("dojox.collections.ArrayList");

dojo.require("geomajas.io.JsonCommand");
dojo.require("geomajas.io.JsonService");

dojo.declare("CommandDispatcher", null, {

	/**
	 * @fileoverview Communicator with the server.
	 * @class General handling object for communication with the server through
	 * JSON commands.
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
		this.service = new JsonService({"serviceUrl": geomajasConfig.serverAddress});
		this.aSyncCommands = new dojox.collections.ArrayList ();
	},

	/**
	 * Execute the given command.
	 * @param command A JsonCommand object.
	 */
	execute : function (/*JsonCommand*/command) {
		var params = [command.cmdName, command.forSerialization(), geomajasConfig.userToken, dojo.locale];

		// returns a deferred request handler
		this.service.setSync(command.isSync());
		var deferred = this.service.callRemote("CommandDispatcher.execute", params);
		deferred.addCallback(this, "onCallback");
		deferred.addErrback(this, "onCallback");
		return deferred;
	},

	/**
	 * Command callback connection function.
	 * @param result The result (in JSON) of the command.
	 */
	onCallback : function (result) {
		try {
			if (result && result.errorMessages && result.errorMessages.list.length > 0) {
				alert(result.errorMessages.list[0]);
				return null;
			} else if(result.errorObject && result.errorObject.error.msg){
				alert(result.errorObject.error.msg);
				return null;
			}
		} catch (e) {
		}
		// must return result for the next callback 
		return result;
	},

	setActivityDiv : function (ad) {
		this.activityDiv = ad;
	},

	getActivityDiv : function () {
		return this.activityDiv;
	},

	/**
	 * Allow other time consuming processes than Commands to use ActivityDiv too
	 *
	 * DON'T FORGET TO DEACTIVATE WHEN FINISHED
	 */
	setBusyState : function (/*boolean*/ state) {
		if (this.activityDiv) {
			if (state == true)
				this.activityDiv.increment();
			else
				this.activityDiv.decrement();
		}
	},

	/**
	 * Make a log entry on the server, stating the given statement.
	 * @param level The level of logging:<ul><li>0 = LEVEL_DEBUG</li>
	 * <li>1 = LEVEL_INFO</li><li>2 = LEVEL_WARN</li><li>3 = LEVEL_ERROR</li></ul>
	 * @param statement The statement to log on the server.
	 */
	logOnServer : function (level, statement) {
		var command = new JsonCommand("command.Log", "org.geomajas.command.dto.LogRequest", null, false);
		command.addParam("level", level);
		command.addParam("statement", statement);
		var deferred = this.execute(command);
	},
	
	logError : function(err){
		for(var w in err){
			log.error(w+":"+err[w]);
		}
	}
});
