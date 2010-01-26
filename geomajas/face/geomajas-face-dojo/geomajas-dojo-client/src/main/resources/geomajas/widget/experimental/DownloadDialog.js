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

dojo.provide("geomajas.widget.experimental.DownloadDialog");
dojo.require("dijit.Dialog");
dojo.requireLocalization("geomajas.widget", "downloadDialog");

dojo.declare("geomajas.widget.experimental.DownloadDialog", dijit.Dialog, {

	location : null,
	helpText : null,
	buttonText : null,

	postMixInProperties : function () {
		var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "downloadDialog");
		this.title = widgetLocale.title;
		this.helpText = widgetLocale.helpText;
		this.buttonText = widgetLocale.buttonText;
	},
	
	postCreate : function() {
		var content =
			"<div>"+this.helpText+"</div>" +
			"<div style='padding: 5px 0px;'><center><a href='"+this.location+"' onClick=\"dijit.byId('"+this.id+"').destroy()\" target='_blank'>"+this.buttonText+"</a></center></div>";
		this.setContent (content);
		this.inherited (arguments);
	}
});