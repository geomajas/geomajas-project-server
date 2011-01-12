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