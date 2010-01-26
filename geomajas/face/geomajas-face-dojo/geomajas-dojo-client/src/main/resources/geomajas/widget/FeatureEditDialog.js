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

dojo.provide("geomajas.widget.FeatureEditDialog");
dojo.require("dijit.Dialog");
dojo.require("dojox.xml.DomParser");

dojo.require("geomajas.widget.attributes.AttributeEditorFactory");

/**
 * @deprecated
 * Deprecated widget. Use a floating pane with a FeatureDetailEditor in it.
 */
dojo.declare("geomajas.widget.FeatureEditDialog", dijit.Dialog, {

	checkValidity : true,
	feature : null,
	editorFactory : null,
	ids : [],
	
	// Handles:
	changeHandles : null,
	validateHandles : null,

	constructor : function () {
		this.changeHandles = [];
		this.validateHandles = [];
	},

	/**
	 * Called when the "ok" button is pressed. This will apply the new values
	 * to the feature in the FeatureModel.
	 */
	execute : function () {
		log.error ("FeatureEditDialog.execute");
		var attr = this.feature.getLayer().getFeatureType().getAttributes();
		var keys = attr.getKeyList();
		for (var i=0; i < keys.length; i++) {
			var atDef = attr.item(keys[i]);
			var id = this._createEditorId(atDef);
			this.feature.setAttributeValue(atDef.getName(),this.editorFactory.getEditorValue(id,atDef));
		}
		this._destroyOldWidgets();
	},

	/**
	 * Show this dialog!
	 */
	show : function () {
		log.error ("FeatureEditDialog.show");
		while (this.changeHandles.length > 0) {
			dojo.disconnect(this.changeHandles.pop());
		}
		while (this.validateHandles.length > 0) {
			dojo.disconnect(this.validateHandles.pop());
		}

		this._createDetailTable();
		var okButton = dijit.byId (this.id + ":ok");
		if (okButton) {
			for (var i=0; i<this.ids.length; i++) {
				var editor = dijit.byId(this.ids[i]);
				if (editor && this.checkValidity) {
					this.changeHandles.push (dojo.connect(editor, "onChange", dojo.hitch(this, "_checkEditorValidity")));
					if (editor.validate) {
						this.validateHandles.push (dojo.connect(editor, "validate", dojo.hitch(this, "_checkEditorValidity")));
					}
				}
			}
		}
		this.inherited("show", arguments);

		// The dialog.show focuses first widget, we don't want that!
		setTimeout(dojo.hitch(this, function(){
			dijit.focus(this.domNode);
		}), 100);
	},

	hide : function () {
		log.error ("FeatureEditDialog.hide");
		this.inherited("hide", arguments);
	},

	setFeature : function (feature) {
		this.feature = feature;
	},

	destroy : function () {
		log.error ("FeatureEditDialog.destroy");
		while (this.changeHandles.length > 0) {
			dojo.disconnect(this.changeHandles.pop());
		}
		while (this.validateHandles.length > 0) {
			dojo.disconnect(this.validateHandles.pop());
		}
		this._destroyOldWidgets();
		var okButton = dijit.byId (this.id + ":ok");
		okButton.destroy();
		this.inherited ("destroy", arguments);
	},

	/**
	 * @private
	 */
	_createDetailTable : function () {
		if (this.editorFactory == null) {
			this.editorFactory = new AttributeEditorFactory();
		}
		var begin = "<div id='"+this.id+":grid' class='FeatureEditDialog' style='text-align: center;'><table class='FeatureEditDialog' border='1' cellpadding='0' cellspacing='0'><tbody class='FeatureEditDialog'>" +
						"<tr><th>Attribute</th><th>Value</th></tr>";
		var eind = 	"</tbody></table><button id='"+this.id+":ok' dojoType=dijit.form.Button type='submit' style='text-align: center;margin-top: 5px;'>OK</button></div>";

		var rows = "";

		var attr = this.feature.getLayer().getFeatureType().getAttributes();
		var keys = attr.getKeyList();
		// create the table with placeholder div's for editor widgets
		for (var i=0; i<keys.length; i++) {
			var atDef = attr.item(keys[i]);
			rows += this._textRow(atDef);
		}
		this.setContent(begin + rows + eind);

		// now we can replace the div's by the widgets !
		for (var i=0; i<keys.length; i++) {
			var atDef = attr.item(keys[i]);
			var id = this._createEditorId(atDef);
			this.editorFactory.create(this._createEditorId(atDef) , atDef, this.feature, dojo.byId(id));
			this.ids.push(id);
			if (!atDef.isEditable()) {
				var widget = dijit.byId(id);
				widget.setDisabled(true);
			}
		}	
	},

	/**
	 * @private
	 */
	_destroyOldWidgets : function () {
		for (var i=0; i<this.ids.length; i++) {
			var widget = dijit.byId(this.ids[i]);
			if (widget) {
				widget.destroy();
			}
		}
		this.ids = [];
	},

	/**
	 * @private
	 */
	_textRow : function (atDef) {
		var temp = "<tr><td>"+atDef.getLabel()+"</td>";
		temp += "<td><div class='noBorderTable'><div id="+this._createEditorId(atDef)+"></div></div></td>";
		return temp;
	},

	/**
	 * @private
	 */
	_createEditorId : function (atDef) {
		if (this.feature != null) {
			return "atribute:editor:" + this.feature.getId() + ":" + atDef.getName();
		} else {
			return "atribute:editor:" + atDef.getName();
		}
	},

	/**
	 * Check each editor for value validity. Disables the OK button if needed.
	 * @private
	 */
	_checkEditorValidity : function (value) {
		var okButton = dijit.byId(this.id + ":ok");
		if (okButton){
			for (var i=0; i<this.ids.length; i++) {
				var editor = dijit.byId(this.ids[i]);
				if (editor && editor.isValid) {
					if (!editor.isValid()) {
						okButton.setDisabled(true);
						return;
					}
				}
			}
			okButton.setDisabled(false);
		}
	}
});