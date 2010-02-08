dojo.provide("geomajas.widget.attributes.AssociationAttributeStore");
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
dojo.require("dojo.data.ItemFileReadStore");

dojo.declare("AssociationAttributeStore", dojo.data.ItemFileReadStore, {
	
	constructor: function(/* Object */ atDef){
		AssociationAttributeStore.superclass.constructor.call(this,{});
		log.info("constructor 2");
		this.atDef = atDef;
		log.info("constructor "+this.atDef.getName());
	},
                  
	_fetchItems: function(	/* Object */ keywordArgs, 
							/* Function */ findCallback, 
							/* Function */ errorCallback){
		//	summary: 
		//		See dojo.data.util.simpleFetch.fetch()
		var self = this;
		var filter = function(requestArgs, arrayOfItems){
			var items = [];
			if(requestArgs.query){
				var ignoreCase = requestArgs.queryOptions ? requestArgs.queryOptions.ignoreCase : false; 

				//See if there are any string values that can be regexp parsed first to avoid multiple regexp gens on the
				//same value for each item examined.  Much more efficient.
				var regexpList = {};
				for(var key in requestArgs.query){
					var value = requestArgs.query[key];
					if(typeof value === "string"){
						regexpList[key] = dojo.data.util.filter.patternToRegExp(value, ignoreCase);
					}
				}

				for(var i = 0; i < arrayOfItems.length; ++i){
					var match = true;
					var candidateItem = arrayOfItems[i];
					if(candidateItem === null){
						match = false;
					}else{
						for(var key in requestArgs.query) {
							var value = requestArgs.query[key];
							if (!self._containsValue(candidateItem, key, value, regexpList[key])){
								match = false;
							}
						}
					}
					if(match){
						items.push(candidateItem);
					}
				}
				findCallback(items, requestArgs);
			}else{
				// We want a copy to pass back in case the parent wishes to sort the array. 
				// We shouldn't allow resort of the internal list, so that multiple callers 
				// can get lists and sort without affecting each other.  We also need to
				// filter out any null values that have been left as a result of deleteItem()
				// calls in ItemFileWriteStore.
				for(var i = 0; i < arrayOfItems.length; ++i){
					var item = arrayOfItems[i];
					if(item !== null){
						items.push(item);
					}
				}
				findCallback(items, requestArgs);
			}
		};

		if(this._loadFinished){
			log.info("_loadFinished");
			filter(keywordArgs, this._getItemsArray(keywordArgs.queryOptions));
		}else{
			if(this._loadInProgress){
				log.info("_loadInProgress");
				this._queuedFetches.push({args: keywordArgs, filter: filter});
			}else{
				log.info("_loading");
				this._loadInProgress = true;
				var deferred =  this.executeCommand();
				deferred.addCallback(function(result){
					try{
						log.info("_load callback");
						self._getItemsFromLoadedData(
							{ 
								items : result.objects.list,
								identifier : self.atDef.getAssociationIdentifierName()
							}
						);
						self._loadFinished = true;
						self._loadInProgress = false;							
						filter(keywordArgs, self._getItemsArray(keywordArgs.queryOptions));
						self._handleQueuedFetches();
					}catch(e){
						for(var word in e){
							log.error(word+","+e[word]);
						}
						self._loadFinished = true;
						self._loadInProgress = false;
						errorCallback(e, keywordArgs);
					}
				});
				deferred.addErrback(function(error){
						log.info("_load errback");
						self._loadInProgress = false;
						errorCallback(error, keywordArgs);
				});
			}
		}
	},
	
	fetchItemByIdentity: function(/* Object */ keywordArgs){
		//	summary: 
		//		See dojo.data.api.Identity.fetchItemByIdentity()

		// Hasn't loaded yet, we have to trigger the load.
		if(!this._loadFinished){
			var self = this;
			if(this._loadInProgress){
				this._queuedFetches.push({args: keywordArgs});
			}else{
				this._loadInProgress = true;
				var deferred =  this.executeCommand();
				deferred.addCallback(function(result){
					try{
						log.info("_load callback");
						self._getItemsFromLoadedData(
							{ 
								items : result.objects.list, 
								identifier : self.atDef.getAssociationIdentifierName()
							}
						);
						self._loadFinished = true;
						self._loadInProgress = false;							
						var item = self._getItemByIdentity(keywordArgs.identity);
						if(keywordArgs.onItem){
							keywordArgs.onItem.call(scope, item);
						}
						self._handleQueuedFetches();
					}catch(e){
						for(var word in e){
							log.error(word+","+e[word]);
						}
						self._loadFinished = true;
						self._loadInProgress = false;
						errorCallback(e, keywordArgs);
					}
				});
				deferred.addErrback(function(error){
						self._loadInProgress = false;
						if(keywordArgs.onError){
							var scope =  keywordArgs.scope?keywordArgs.scope:dojo.global;
							keywordArgs.onError.call(scope, error);
						}
				});
			}
		}else{
			// Already loaded.  We can just look it up and call back.
			var item = this._getItemByIdentity(keywordArgs.identity);
			if(keywordArgs.onItem){
				var scope =  keywordArgs.scope?keywordArgs.scope:dojo.global;
				keywordArgs.onItem.call(scope, item);
			}
		}
	},
	
	_forceLoad: function(){
		//	summary: 
		//		Internal function to force a load of the store if it hasn't occurred yet.  This is required
		//		for specific functions to work properly.  
		var self = this;
		var deferred =  this.executeCommand();
		deferred.addCallback(function(result){
			try{
				//Check to be sure there wasn't another load going on concurrently 
				//So we don't clobber data that comes in on it.  If there is a load going on
				//then do not save this data.  It will potentially clobber current data.
				//We mainly wanted to sync/wait here.
				//TODO:  Revisit the loading scheme of this store to improve multi-initial
				//request handling.
				if (self._loadInProgress !== true && !self._loadFinished) {
					self._getItemsFromLoadedData(
						{ 
							items : result.objects.list, 
							identifier : self.atDef.getAssociationIdentifierName()
						}
					);
					self._loadFinished = true;
				}
			}catch(e){
				console.log(e);
				throw e;
			}
		});

		deferred.addErrback(function(error){
			throw error;
		});
	},

	executeCommand: function () {
		// prepare the command
		var command = new JsonCommand("command.feature.GetAssociation",
                "org.geomajas.command.dto.GetAssociationRequest", null, true);
		command.addParam("layerId", this.atDef.getLayer().layerId);
		command.addParam("attributeName",this.atDef.getName());
		var deferred =  geomajasConfig.dispatcher.execute(command);
		return deferred;
	}
});
