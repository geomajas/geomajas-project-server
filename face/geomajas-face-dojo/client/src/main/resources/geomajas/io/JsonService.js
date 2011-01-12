dojo.provide("geomajas.io.JsonService");
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
