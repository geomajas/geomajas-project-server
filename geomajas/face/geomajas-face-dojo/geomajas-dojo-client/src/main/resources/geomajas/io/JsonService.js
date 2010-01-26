dojo.provide("geomajas.io.JsonService");
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
dojo.require("dojo.rpc.JsonService");

dojo.declare ("LoggingDeferred", dojo.Deferred, {
	
	addCallback : function(cb,cbf) {
		LoggingDeferred.superclass.addCallback.apply(this, [cb,cbf]);
		LoggingDeferred.superclass.addErrback.apply(this, [this,"logError"]);
	},
	
	logError : function(err){
		for(var w in err){
			log.error(w+":"+err[w]);
		}
	}
});
	


dojo.declare ("JsonService", dojo.rpc.JsonService, {

	sync : false,
	time : 60000,

	bind : function(method, parameters, deferredRequestHandler, url){
		var def = dojo.rawXhrPost({
			url: url||this.serviceUrl,
			postData: this.createRequest(method, parameters),
			contentType: this.contentType,
			timeout: this.time, 
			handleAs: "json",
			sync: this.sync
		});
		def.addCallbacks(this.resultCallback(deferredRequestHandler), this.errorCallback(deferredRequestHandler));
	},

	isSync : function () {
		return this.sync;
	},

	setSync : function (sync) {
		this.sync = sync;
	}/*,
	
	callRemote: function(method, params){
		// summary:
		// 		call an arbitrary remote method without requiring it to be
		// 		predefined with SMD
		//	method: string
		//		the name of the remote method you want to call.
		//	params: array
		//		array of parameters to pass to method

		var deferred = new LoggingDeferred();
		this.bind(method, params, deferred);
		return deferred;
	}*/
});
