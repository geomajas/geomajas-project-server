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

dojo.provide("geomajas.widget.ScaleSelect");
dojo.require("dijit.form.ComboBox");
dojo.requireLocalization("geomajas.widget", "scaleSelect");

/**
 * @fileoverview 
 * This widget connects to a MapWidget, and synchronizes with the map's scale.
 * It has a combobox, with which one can select a new scale level for the map,
 * or in which one can type a new scale level. But the synchronization goes in
 * both directions. If one zooms in or out on the map, this widget will 
 * automatically display the new scale.
 * If one tries to fill in a new scale level maually, be sure to type an 
 * integer: if you fill in integer X, the widget will apply scale 1:X.
 * 
 * Also, this widget can be added to a toolbar, with the toolbar.addChild() 
 * method.
 * 
 * TODO: A combobox is a ValidationTextBox, so perhaps we should take 
 * advantage of the regexp, the promptmessage, the invalidmessage, etc.
 * 
 * @author Pieter De Graef
 */
dojo.declare("geomajas.widget.ScaleSelect", [ dijit._Widget, dijit._Templated,
		dijit._Container ], {

	widgetsInTemplate :true,
	templatePath :dojo.moduleUrl("geomajas.widget", "html/ScaleSelect.html"),

	scaleText : "Scale : ", // The text that comes before the combobox.
	mapWidget : null, // Reference to the MapWidget with which to sync scales.

	/**
	 * Set the scale-text according to internationalization definitions.
	 */
	postMixInProperties : function (/*Object*/args, /*Object*/frag, /*Widget*/parent) {
		var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "scaleSelect");
		this.scaleText = widgetLocale.scaleText;
		this.inherited(arguments);
	},

	/**
	 * On startup of this widget, automatically set some scales to fill the
	 * combobox with. Also attach a function on the "onChange" event.
	 */
	startup : function() {
		var cmb = dijit.byId(this.id + ":cmb");
		if (cmb != null) {
			this.setScaleArray( [ 500, 1000, 2500, 5000, 7500, 10000, 20000,
					50000, 100000, 500000, 1000000, 5000000, 10000000 ]);
			cmb.onChange = dojo.hitch(this, "_onChange");
		}
		this.inherited(arguments);
	},

	/**
	 * This widget needs to be connected to a MapWidget to which it must
	 * synchronize scales. This function does that.
	 * @param mapWidget The MapWidget object to synchronize scales to.
	 */
	setMapWidget : function(mapWidget) {
		this.mapWidget = mapWidget;
		if (this.mapWidget != null) {
			this._updateScaleLabel(this.mapWidget.getMapView().getCurrentScale());
			this.connect(this.mapWidget.getMapView(), "setCurrentScale", dojo.hitch(this, "_updateScaleLabel"));
			this.connect(this.mapWidget.getScaleUtil(), "unitLengthCallback", dojo.hitch(this, "_onUnitLengthCallback"));
			
			var resolutions = this.mapWidget.getMapView().getResolutions();
			if (resolutions != null) {
				var scales = [];
				for(var i=resolutions.length-1; i>=0; i--) {
					var scale = parseFloat(1 / resolutions[i]);
					var temp = scale * this.mapWidget.getScaleUtil().getPixelWidth();
					var value = Math.round(this.mapWidget.getScaleUtil().getUnitLength() / temp);
					scales.push(value);
				}
				this.setScaleArray(scales);
			}
		}
	},

	/**
	 * Set a new list of scale options in the combobox.
	 * @param array The list of scale should be an array with integer values.
	 *              These will be filled in as (1:x) one by one, so better make
	 *              sure they are sorted! Example: [1000,5000,10000].
	 */
	setScaleArray : function(array) {
		if (array == null || !(array instanceof Array)) {
			log.debug("No array given in ScaleSelect.setScaleArray!");
			return;
		}
		var cmb = dijit.byId(this.id + ":cmb");
		if (cmb != null) {
			var store = {
				data : {
					identifier :"count",
					items : []
				}
			};
			for ( var i = 1; i <= array.length; i++) {
				var item = {
					name :"" + i,
					label :"1 : " + this._integerToDottedString(array[i - 1]),
					count :i
				};
				store.data.items.push(item);
			}
			cmb.store = new dojo.data.ItemFileReadStore(store);
		} else {
			log.error("Could not find the scale combobox. (" + this.id
					+ ":cmb)");
		}
	},



	//-------------------------------------------------------------------------
	// Private methods
	//-------------------------------------------------------------------------

	/**
	 * @private
	 * When a new value is entered in the combobox, try and zoom in or out on
	 * the map.
	 * @param label The label from the combobox. Either one of the selected 
	 *              values (1:x), or a simple integer (x).
	 */
	_onChange : function (label) {
		if (this.mapWidget == null) {
			log.error ("This ScaleSelect ("+this.id+") is not connected to a" +
					" MapWidget yet!");
			return;
		}
		if (label == null || label == "") {
			var scale = this.mapWidget.getMapView().getCurrentScale();
			this._updateScaleLabel(scale);
			return;
		}
		var scale = this.mapWidget.getScaleUtil().stringToScale(label);
		if (scale >= 0) {
			this.mapWidget.getMapView().setCurrentScale(scale);
		} else {
			var cmb = dijit.byId(this.id + ":cmb");
			cmb.setDisplayedValue("");
		}
	},

	/**
	 * @private
	 * React to the mapview's setCurrentScale, and update the combobox label,
	 * without having it fire the onChange event again.
	 * @param scale The current scale from the MapView object.
	 */
	_updateScaleLabel : function (scale) {
		if (this.mapWidget == null) {
			log.error ("This ScaleSelect ("+this.id+") is not connected to a" +
					" MapWidget yet!");
			return;
		}
		// Ask scale again, because it may have been snapped to a resolution:
		scale = this.mapWidget.getMapView().getCurrentScale();
		var label = this.mapWidget.getScaleUtil().scaleToString(scale);

		var cmb = dijit.byId(this.id + ":cmb");
		cmb._onChangeActive = false; // little hack.
		cmb.setDisplayedValue(label);
		cmb._onChangeActive = true;
	},

	/**
	 * @private
	 * Transforms an integer like 32000 to string 32.000
	 */
	_integerToDottedString : function (value) {
		var strValue = "" + value;
		var count = strValue.length - 3;
		while (count > 0) {
			var before = strValue.substring(0, count);
			var after = strValue.substring(count);
			strValue = before + "." + after;
			count = count - 3;
		}
		return strValue;
	},

	/**
	 * @private
	 */
	_onUnitLengthCallback : function (result) {
		var scale = this.mapWidget.getMapView().getCurrentScale();
		this._updateScaleLabel(scale);
	}
});