dojo.provide("geomajas.widget.DefaultPrintWidget");
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
dojo.require("dijit.Dialog");
dojo.require("dojox.xml.DomParser");
dojo.requireLocalization("geomajas.widget", "printDialog");

dojo.declare("geomajas.widget.DefaultPrintWidget", [
		dijit.layout.LayoutContainer, dijit._Templated ], {

	templatePath :dojo.moduleUrl("geomajas.widget",
			"html/DefaultPrintWidget.html"),

	widgetsInTemplate : true,

	sourceMap : null,

	bounds : null,

	rectangle : null,
	
	printButton : null,
	
	downloadMethod : 0,

	printSettingsString : "",
	pageSizeString : "",
	titleString : "",
	dateString : "",
	northArrowString : "",
	rasterDPIString : "",
	
	postCreate : function() {
		this.inherited(arguments);
		printButton = dijit.byId(this.id+":print");
		printButton.onClick = dojo.hitch(this, "print");
		// singleton model class
		this.templateManager = geomajasConfig.printManager;
	},
	
	postMixInProperties : function(/* Object */args, /* Object */frag, /* Widget */
			parent) {
		var widgetLocale = dojo.i18n.getLocalization("geomajas.widget",
				"printDialog");
		this.printSettingsString = widgetLocale.printSettings;
		this.pageSizeString = widgetLocale.pageSize;
		this.titleString = widgetLocale.title;
		this.dateString = widgetLocale.date;
		this.northArrowString = widgetLocale.northArrow;
		this.rasterDPIString = widgetLocale.rasterDPI;
	},
	
	print : function() {
		var template = this.templateManager.getTemplateByName("Default-A4-landscape");
		if(template){
			this.templateManager.activateTemplate(template, this.sourceMap, true, true);
		}
		
		var defs = {
			rasterDPI : this._getWidgetValue("rasterdpi"),
			title : this._getWidgetValue("title"),
			withArrow : this._getWidgetValue("arrow")=="on" ? true : false,
			withDate : this._getWidgetValue("date")=="on" ? true : false
		};
				
		// default config
		this.templateManager.configureDefaults(defs);
		
		// send off !
		this.templateManager.print(true,this._getWidgetValue("pagesize"));
	},
	
	setSourceMap : function (mapWidget) {
		this.sourceMap = mapWidget;
	},
	
	setDownloadMethod : function (/*int*/downloadMethod) {
		this.downloadMethod = downloadMethod;
	},
	
	_getWidgetValue : function(/*string*/subId) {
		var widget = dijit.byId(this.id+":"+subId);
		if(widget){
			return widget.getValue();
		}
	},

	_callback : function (result) {
		window.open("./geomajas.pdf?documentId=" + result.documentId);
	}

});
		
