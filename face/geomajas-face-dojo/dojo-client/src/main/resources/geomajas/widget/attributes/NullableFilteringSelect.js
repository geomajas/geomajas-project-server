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

dojo.provide("geomajas.widget.attributes.NullableFilteringSelect");
dojo.require("dijit.form.FilteringSelect");

/**
 * <p>
 * Extension of dojo's 'dijit.form.FilteringSelect' widget to potentially allow for empty values.
 * </p>
 * @author Pieter De Graef
 */
dojo.declare("geomajas.widget.attributes.NullableFilteringSelect", dijit.form.FilteringSelect, {
	
	required : true,

	setValue: function(/*String*/ value, /*Boolean?*/ priorityChange){
		// summary
		//	Sets the value of the select.
		//	Also sets the label to the corresponding value by reverse lookup.

		//#3347: fetchItemByIdentity if no keyAttr specified
		var self=this;
		var handleFetchByIdentity = function(item, priorityChange){
			if(item){
				if(self.store.isItemLoaded(item)){
					self._callbackSetLabel([item], undefined, priorityChange);
				}else{
					self.store.loadItem({
						item: item, 
						onItem: function(result, dataObject){
							self._callbackSetLabel(result, dataObject, priorityChange);
						}
					});
				}
			}else {
				if (self.required){
					self._isvalid=false;
				}
				// prevent errors from Tooltip not being created yet
				self.validate(false);
			}
		}
		this.store.fetchItemByIdentity({
			identity: value, 
			onItem: function(item){
				handleFetchByIdentity(item, priorityChange);
			}
		});
	},

	_callbackSetLabel: function(/*Array*/ result, /*Object*/ dataObject, /*Boolean?*/ priorityChange){
		// summary:
		//		Callback function that dynamically sets the label of the
		//		ComboBox
		
		// setValue does a synchronous lookup,
		// so it calls _callbackSetLabel directly,
		// and so does not pass dataObject
		// dataObject==null means do not test the lastQuery, just continue
		if(dataObject && dataObject.query[this.searchAttr] != this._lastQuery){
			return;
		}
		if(!result.length){
			//#3268: do nothing on bad input
			//this._setValue("", "");
			//#3285: change CSS to indicate error
			if(!this._focused){ this.valueNode.value=""; }
			dijit.form.TextBox.superclass.setValue.call(this, undefined, !this._focused);
			if (this.textbox.value != null && this.textbox.value != "") {
				this._isvalid=false;
			} else if (this.required) {
				this._isvalid=false;
			}
			this.validate(this._focused);
		}else{
			this._setValueFromItem(result[0], priorityChange);
		}
	},

	_openResultList: function(/*Object*/ results, /*Object*/ dataObject){
		// #3285: tap into search callback to see if user's query resembles a match
		if(dataObject.query[this.searchAttr] != this._lastQuery){
			return;
		}
		if (this.required) {
			this._isvalid = results.length != 0; // FIXME: should this be greater-than?
		} else {
			this._isvalid = true;
		}
		this.validate(true);
		dijit.form.ComboBoxMixin.prototype._openResultList.apply(this, arguments);
	}

});