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

dojo.provide("geomajas.widget.ProgressDialog");
dojo.require("dijit.Dialog");
dojo.require("dijit.ProgressBar");
dojo.require("dojox.xml.DomParser");

dojo.declare("geomajas.widget.ProgressDialog", dijit.Dialog, {

	ids : [],
	progressId : 0,
	okButton : null,
	/**
	 * Called when the "ok" button is pressed.
	 */
	execute : function () {
		this._destroyOldWidgets();
	},

	/**
	 * Show this dialog!
	 */
	show : function () {
		this._createProgressDialog();
		okButton = dijit.byId (this.id + ":ok");
		
		okButton.setDisabled(true);

		this.inherited("show", arguments);
		// The dialog.show focuses first widget, we don't want that!
		setTimeout(dojo.hitch(this, function(){
			dijit.focus(this.domNode);
		}), 100);
	},

	hide : function () {
//		this._destroyOldWidgets();
		this.inherited("hide", arguments);
	},

	destroy : function () {
		this._destroyOldWidgets();
		okButton.destroy();
		this.inherited ("destroy", arguments);
	},
	
	setProgressId : function (/*int*/ id) {
		this.progressId = id;
	},
	
	startProgress : function () {
		this._fetchProgress();
	},

	/**
	 * @private
	 */
	_fetchProgress : function () {
		var command = new JsonCommand("command.progress.Get",
                "org.geomajas.command.dto.GetProgressRequest", null, false);
		command.addParam("taskId", this.progressId);

		var deferred = geomajasConfig.dispatcher.execute(command);
		deferred.addCallback(this, "_fetchProgressCallback");
	},
	
	/**
	 * @private
	 */
	_fetchProgressCallback : function (result) {
		var progressBar = dijit.byId(this.id+":progressBar");
		progressBar.update({progress: result.progress});
		

		if(result.ready) {
			okButton.setDisabled(false);
		} else {
			setTimeout(dojo.hitch(this, "_fetchProgress"), 250);
		}			
	},
	
	/**
	 * @private
	 */
	_createProgressDialog : function () {
		var begin = "<div id='"+this.id+"' class='ProgressDialog' style='text-align: center;'>";
		
		var middle = "<div dojoType='dijit.ProgressBar' style='width:300px' id='"+this.id+":progressBar'></div>";		

		var end = 	"<button id='"+this.id+":ok' dojoType=dijit.form.Button type='submit' style='text-align: center;margin-top: 5px;'>OK</button></div>";

		this.setContent(begin + middle + end);
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
	}
});