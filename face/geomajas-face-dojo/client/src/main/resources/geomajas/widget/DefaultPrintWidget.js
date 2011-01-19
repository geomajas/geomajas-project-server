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

	templatePath :dojo.moduleUrl("geomajas.widget",	"html/DefaultPrintWidget.html"),

	widgetsInTemplate : true,
	sourceMap : null,
	bounds : null,
	rectangle : null,
	printButton : null,
	downloadMethod : 0,

	printSettingsString : "",
	pageSizeString : "",
	titleString : "",
	scalebarString : "",
	northArrowString : "",
	rasterDPIString : "",
	
	postCreate : function() {
		this.inherited(arguments);
		this.printButton = dijit.byId(this.id+":print");
		this.printButton.onClick = dojo.hitch(this, "print");
		// singleton model class
		this.templateManager = geomajasConfig.printManager;
	},
	
	postMixInProperties : function(/* Object */args, /* Object */frag, /* Widget */
			parent) {
		var widgetLocale = dojo.i18n.getLocalization("geomajas.widget",	"printDialog");
		this.printSettingsString = widgetLocale.printSettings;
		this.pageSizeString = widgetLocale.pageSize;
		this.titleString = widgetLocale.title;
		this.scalebarString = widgetLocale.scalebar;
		this.northArrowString = widgetLocale.northArrow;
		this.rasterDPIString = widgetLocale.rasterDPI;
	},
	
	print : function() {
		this.printButton.setDisabled(true);
		var template = this._buildTemplate();
		var command = new JsonCommand("command.print.GetTemplate",
                "org.geomajas.plugin.printing.command.dto.PrintGetTemplateRequest", null, false);
		command.addParam("template", template);
		var deferred = geomajasConfig.dispatcher.execute(command);
		deferred.addCallback(this, "_printCallback");
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
	
	_printCallback : function (data) {
		log.info("DefaultPrintWidget got back documentId: " + data.documentId);
		var url = geomajasConfig.serverBase+"d/printing?documentId=" + data.documentId;
		url += "&name=" + "Kaartzicht.pdf";
		url += "&download=0";
		url += "&userToken=" + geomajasConfig.userToken;
		var panel = new geomajas.widget.experimental.DownloadDialog({
			id: "downloadPrint"+data.documentId,
			refocus:false,
			title: "File Download",
			location:url
		},null);
		this.printButton.setDisabled(false);
		panel.show();		
	},

	_buildTemplate : function () {
		var builder = new DefaultTemplateBuilder();
		builder.setApplicationId(geomajasConfig.applicationId);
		builder.setMapModel(this.sourceMap.getMapModel());
		builder.setMarginX(20);
		builder.setMarginY(20);
		var size = geomajas.PageSizes[this._getWidgetValue("pagesize")];
//		if ("landscape".equals(orientationGroup.getValue())) { // TODO: no option in printwidget
			builder.setPageHeight(size.getWidth());
			builder.setPageWidth(size.getHeight());
//		} else {
//			builder.setPageHeight(size.getHeight());
//			builder.setPageWidth(size.getWidth());
//		}
		
		builder.setTitleText(this._getWidgetValue("title"));
		builder.setWithArrow(this._getWidgetValue("arrow")=="on" ? true : false);
		builder.setWithScaleBar(this._getWidgetValue("scalebar")=="on" ? true : false);
		builder.setRasterDpi(this._getWidgetValue("rasterdpi"));

		return builder.buildTemplate();
	}

});
		
