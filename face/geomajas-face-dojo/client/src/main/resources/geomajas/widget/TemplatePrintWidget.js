dojo.provide("geomajas.widget.TemplatePrintWidget");
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
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dijit.TitlePane");
dojo.requireLocalization("geomajas.widget", "templatePrintWidget");

dojo.declare("geomajas.widget.TemplatePrintWidget", [dijit.layout.LayoutContainer, dijit._Templated], {

	templatePath : dojo.moduleUrl("geomajas.widget", "html/TemplatePrintWidget.html"),
	widgetsInTemplate : true,

	sourceMap : null,
	templateManager: geomajasConfig.printManager,

	bounds : null,
	rectangle : null,
	printName : null,
	
	_templateCombo : null,
	_printCombo : null,
	_openPrintButton : null,
	_createPrintButton : null,
	_deletePrintButton : null,
	_panMapRadio : null,
	_panPrintRadio : null,
	_addViewPortButton : null,
	_nameText : null,
	_saveButton : null,
	_printButton : null,
	_editPane: null,
	
	_pagesize : null,
	_date : null,
	_title : null,
	_arrow : null,
	_rasterdpi : null,

	
	openPrintString : "",
	selectTemplateString : "",
	panningMovesString : "",
	movesMapString : "",
	movesPrintString : "",
	viewPortsString : "",
	nameString : "",
	addString : "",
	printString : "",
	saveString : "",
	printSettingsString : "",
	pageSizeString : "",
	titleString : "",
	dateString : "",
	northArrowString : "",
	rasterDPIString : "",
	createPrintString : "",
	selectPrintString : "",
	editPrintString : "",
	showPrintString : "",
	deletePrintString : "",
	
	

	postCreate : function () {
		this.inherited(arguments);
		this.templateManager = geomajasConfig.printManager;
		this._templateCombo.store = this.templateManager.getTemplateStore();
		this._printCombo.store = this.templateManager.getPrintStore();
		this._openPrintButton.setAttribute("disabled", true);
		this._createPrintButton.setAttribute("disabled", true);
		this._deletePrintButton.setAttribute("disabled", true);
		this._disableEditPane(true);
	},
	
	postMixInProperties : function(/*Object*/args, /*Object*/frag, /*Widget*/parent){
		var widgetLocale = dojo.i18n.getLocalization("geomajas.widget", "templatePrintWidget");
		this.openPrintString = widgetLocale.openPrint;
		this.selectTemplateString = widgetLocale.selectTemplate;
		this.panningMovesString = widgetLocale.panningMoves;
		this.movesMapString = widgetLocale.movesMap;
		this.movesPrintString = widgetLocale.movesPrint;
		this.viewPortsString = widgetLocale.viewPorts;
		this.nameString = widgetLocale.name;
		this.addString = widgetLocale.add;
		this.printString = widgetLocale.print;
		this.saveString = widgetLocale.save;
		this.printSettingsString = widgetLocale.printSettings;
		this.pageSizeString = widgetLocale.pageSize;
		this.titleString = widgetLocale.title;
		this.dateString = widgetLocale.date;
		this.northArrowString = widgetLocale.northArrow;
		this.rasterDPIString = widgetLocale.rasterDPI;
		this.printString = widgetLocale.print;
		this.createPrintString = widgetLocale.createPrint;
		this.selectPrintString = widgetLocale.selectPrint;
		this.editPrintString = widgetLocale.editPrint;
		this.showPrintString = widgetLocale.showPrint;
		this.deletePrintString = widgetLocale.deletePrint;
	},

	setSourceMap : function (mapWidget) {
		this.sourceMap = mapWidget;
	},


	_print : function () {
		// user can set defaults
		this._applyDefaults();
		// send off !
		this.templateManager.print(true, null);
	},

	_save : function () {
		var name = this._nameText.getValue();
		// user can set defaults
		this._applyDefaults();
		// save
		this.templateManager.savePrintAs(name);
	},

	_deletePrint: function () {
		var template = this._printCombo.item;
		if(template){
			this.templateManager.deletePrint(template.getName());
			this._printCombo.setDisplayedValue("");
		}
	},
	
	_applyDefaults : function() {
		var defs = {
				rasterDPI: this._rasterdpi.getValue(),
				title:     this._title.getValue(),
				withArrow: this._arrow.getValue() == "on" ? true : false,
				withDate:  this._date.getValue() == "on" ? true : false
		};
		// default config
		this.templateManager.configureDefaults(defs);
	},
	
	_addport : function () {
		this.templateManager.addViewPort();		
	},

	_applyPanMode : function () {
		if(this.templateManager.getTransform()){
			var fixToMap = this._panPrintRadio.getValue() == "on" ? true : false;
			if(fixToMap){
				this.templateManager.getTransform().fixPrintToWorld();
			} else {
				this.templateManager.getTransform().fixPrintToView();
			}
		}
	},
	
	_selectTemplate : function (value) {
		if(value){
			this.printName = this.sourceMap.getMapModel().getId()+"-"+value;
			this._createPrintButton.setAttribute("disabled", false);
		} else {
			this.printName = null;
			this._createPrintButton.setAttribute("disabled", true);
		}
	},
	
	_selectPrint : function (value) {
		if(value){
			this.printName = value;
			this._openPrintButton.setAttribute("disabled", false);
			this._deletePrintButton.setAttribute("disabled", false);
		} else {
			this.printName = null;
			this._openPrintButton.setAttribute("disabled", true);
			this._deletePrintButton.setAttribute("disabled", true);
		}
	},

	_createPrint : function () {
		var template = this._templateCombo.item;
		if(template){
			this._nameText.setValue(this.printName);
			this.templateManager.activateTemplate(template, this.sourceMap, true, true);
			this.sourceMap.getMapModel().removePaintableObject("printTemplate");
			var page = this.templateManager.getCurrentPrint().getPage();
			this.sourceMap.getMapModel().addPaintableObject("printTemplate",page);
			this._disableEditPane(false);
			this._panMapRadio.setValue("on");
		}
	},
	
	_openPrint : function () {
		var template = this._printCombo.item;
		if(template){
			this._nameText.setValue(template.getName());
			this.templateManager.activateTemplate(template, this.sourceMap, false, false);
			this.sourceMap.getMapModel().removePaintableObject("printTemplate");
			var page = this.templateManager.getCurrentPrint().getPage();
			this.sourceMap.getMapModel().addPaintableObject("printTemplate",page);
			this._disableEditPane(false);
			var defs = this.templateManager.extractDefaults();
			this._rasterdpi.setValue(defs.rasterDPI);
			this._title.setValue(defs.title);
			this._arrow.setValue(defs.withDate);
			this._date.setValue(defs.withArrow);
			this._panPrintRadio.setValue("on");
		}
	},

	_disableEditPane : function(isDisabled) {
		this._panMapRadio.setAttribute("disabled", isDisabled);
		this._panPrintRadio.setAttribute("disabled", isDisabled);
		this._addViewPortButton.setAttribute("disabled", isDisabled);
		this._nameText.setAttribute("disabled", isDisabled);
		this._saveButton.setAttribute("disabled", isDisabled);
		this._printButton.setAttribute("disabled", isDisabled);
		this._editPane.setAttribute("disabled", isDisabled);
		
		//this._pagesize.setAttribute("disabled", isDisabled);
		this._date.setAttribute("disabled", isDisabled);
		this._title.setAttribute("disabled", isDisabled);
		this._arrow.setAttribute("disabled", isDisabled);
		this._rasterdpi.setAttribute("disabled", isDisabled);
	},

	
	/**
	 * custom function on destroy
	 */
	uninitialize: function(){
		this.sourceMap.getMapModel().removePaintableObject("printTemplate");
		this.templateManager.deactivateTemplate();
	}

});
