dojo.provide("geomajas.widget.DefaultPrintWidget");
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
		
