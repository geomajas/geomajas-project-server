dojo.provide("geomajas.map.print.PrintTemplateManager");
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
dojo.require("geomajas.widget.experimental.DownloadDialog");
dojo.requireLocalization("geomajas.map.print", "printTemplateManager");

dojo.declare("PrintTemplateManager", null, {

	/**
	 * @class 
	 * This class maintains print templates
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
		/** dispatcher */
		this.dispatcher = geomajasConfig.dispatcher;
		/** holds all templates */
		this.templateStore = null;
		/** holds all prints */
		this.printStore = null;
		/** the current print*/
		this.currentPrint = null;
		/** the transformation from print to screen coordinates */
		this.currentTransform = null;
		/** the map widget */
		this.map = null;
		/** fill the stores */
		this._fetchTemplates();
		/** i18n locale */
		this.locale = dojo.i18n.getLocalization("geomajas.map.print", "printTemplateManager");

	},

	
	/**
	 * Returns a dojo.data.store for the PrintTemplate (name,...) objects.
	 * This is meant for comboboxes.
	 */
	getTemplateStore : function () {
		return this.templateStore;
	},
	
	/**
	 * Returns a dojo.data.store for the PrintTemplate (name,...) objects.
	 * This is meant for comboboxes.
	 */
	getPrintStore : function () {
		return this.printStore;
	},

	/**
	 * Returns the current template
	 */
	getCurrentPrint : function () {
		return this.currentPrint;
	},

	savePrintAs : function (name) {
		if(this.getPrintByName(name)){
			if (confirm(this.locale.printExists)) {
				this.currentPrint.setName(name);
				this.currentTransform.applyToPage();
				this._savePrint();
			}
		} else {
			this.currentPrint.setName(name);
			this.currentTransform.applyToPage();
			this._savePrint();
		}
	},
	
	deletePrint : function (name) {
		var command = new JsonCommand("command.print.DeleteTemplate",
                "org.geomajas.extension.command.dto.PrintDeleteTemplateRequest", null, false);
		command.addParam("name", name);
		var deferred = this.dispatcher.execute(command);
		deferred.addCallback(this, "_deletePrintCallback");
	},
	
	addViewPort : function () {
		if(this.currentPrint){
			this.currentPrint.addViewPort(this.map.id);
			dojo.publish(this.map.getMapModel().getRenderTopic(), [ this.currentPrint.getPage(), "all"]);
		}
	},
	
	/**
	 * returns the template with the specified name
	 */
	getTemplateByName : function (name) {
		var item = null;
		this.templateStore.fetchItemByIdentity({
			identity : name,
			onItem : function(it){item = it;}				
		});
		return item;
	},
	
	/**
	 * returns the template with the specified name
	 */
	getPrintByName : function (name) {
		var item = null;
		this.printStore.fetchItemByIdentity({
			identity : name,
			onItem : function(it){item = it;}				
		});
		return item;
	},
	
	/**
	 * sets the currently active template
	 * If locationIsCurrent is true, the current map location is taken.
	 */
	activateTemplate : function (/* PrintTemplate */ template, /* MapWidget */map, /* boolean */ locationIsCurrent, fixPrintToView) {
		this.currentPrint = dojo.clone(template);
		this.currentPrint.template = false;
		this.map = map;
		this.currentTransform = new PrintTransformation(map.getMapView(), map.id, this.currentPrint, locationIsCurrent);
		if(!locationIsCurrent){
			this.currentTransform.navigateToPage();
		}
		if(fixPrintToView) {
			this.currentTransform.fixPrintToView();
		}
	},
	
	/**
	 * sets the current template to null
	 */
	deactivateTemplate : function () {
		this.currentPrint = null;
		this.map = null;
		if(this.currentTransform){
			this.currentTransform.destroy();
			this.currentTransform = null
		}
	},
	
	/**
	 * configures the defaults for the current template
	 */
	configureDefaults : function (defs) {
		// default config
		var visitor = new DefaultConfigurationVisitor(defs);	
		this.visitTemplate(visitor);
	},
	
	/**
	 * Extracts the defaults from the current print
	 */
	extractDefaults : function () {
		// default config
		var visitor = new DefaultConfigurationVisitor({extract:true});	
		this.visitTemplate(visitor);
		return visitor;
	},

	/**
	 * prints a pdf with the current template
	 */
	print : function (useMap, pageSize) {
		if(useMap) {
			this.currentTransform.applyToPage();
			var visitor = new MapConfigurationVisitor(this.map.getMapModel());
			this.visitTemplate(visitor);
		}
		var command = new JsonCommand("command.print.GetTemplate",
                "org.geomajas.extension.printing.command.dto.PrintGetTemplateRequest", null, false);
		command.addParam("downloadMethod", 0);
		command.addParam("fileName", this.currentPrint.getName()+".pdf");
		command.addParam("template", this.currentPrint);
		if(pageSize) {
			command.addParam("pageSize", pageSize);
		}
		var deferred = this.dispatcher.execute(command);
		deferred.addCallback(this, "_printCallback");
	},
	
	/**
	 * returns the current print transformation
	 */
	getTransform : function () {
		return this.currentTransform;
	},
	
	/**
	 * returns the current print transformation
	 */
	visitTemplate : function (visitor) {
		return this.currentPrint.getPage().accept(visitor);
	},

	_fetchTemplates : function () {
		var command = new JsonCommand("command.print.ListTemplates",
                "org.geomajas.command.EmptyCommandRequest", null, false);
		var deferred = this.dispatcher.execute(command);
		deferred.addCallback(this, "_fetchTemplatesCallback");
	},
	
	_fetchTemplatesCallback : function (data) {
		var templates = [];
		var prints = [];
		for(var i = 0; i < data.templates.list.length; i++){
			var item = new PrintTemplate(data.templates.list[i], this);
			if(item.template){
				templates.push(new PrintTemplate(data.templates.list[i], this));
			} else {
				prints.push(new PrintTemplate(data.templates.list[i], this));
			}
		}
		this.templateStore = new ArrayDataStore(templates, "name");

		this.printStore = new ArrayDataStore(prints, "name");
	},
	
	_fetchTemplatesAfterCallback : function (data) {
		log.error("_fetchTemplatesAfterCallback");
	},
	
	_fetchTemplatesAfterErrback : function (data) {
		log.error("_fetchTemplatesAfterErrback");
	},
	
	_savePrint : function () {
		var command = new JsonCommand("command.print.SaveTemplate",
                "org.geomajas.extension.printing.command.dto.PrintSaveTemplateRequest", null, false);
		command.addParam("template",this.currentPrint);
		var deferred = this.dispatcher.execute(command);
		deferred.addCallback(this, "_savePrintCallback");
	},
	
	_savePrintCallback : function (data) {
		alert(this.locale.printSaved);
		this.printStore.addItem(dojo.clone(this.currentPrint));
	},
	
	_printCallback : function (data) {
		log.info("DefaultPrintWidget got back documentId: " + data.documentId);
		var url = geomajasConfig.serverBase+"/geomajas.pdf?documentId=" + data.documentId;

		var panel = new geomajas.widget.experimental.DownloadDialog({
			id: "downloadPrint"+data.documentId,
			refocus:false,
			title: "File Download",
			location:url
		},null);
		panel.show();		
	},
	
	_deletePrintCallback : function (data) {
		if(!data.error) {
			this.printStore.removeItem(data.name);
		}
	}
});
