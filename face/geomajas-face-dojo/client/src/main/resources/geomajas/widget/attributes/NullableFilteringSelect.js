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