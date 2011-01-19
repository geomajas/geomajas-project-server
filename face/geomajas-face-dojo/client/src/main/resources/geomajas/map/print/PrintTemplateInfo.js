dojo.provide("geomajas.map.print.PrintTemplateInfo");
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
dojo.declare("PrintTemplateInfo", null, {

	/**
	 * @class 
	 * A print template Info (mirror of server object)
	 * @author Kristof Heirwegh
	 *
	 * @constructor
	 */
	constructor : function() {
//		private Long id;
//		private String name;
//		private boolean template;
//		private PageComponentInfo page;

		this.javaClass = "org.geomajas.plugin.printing.command.dto.PrintTemplateInfo";
	},
	
	getId : function() {
		return this.id;
	},

	setId : function(id) {
		this.id = id;
	},

	getName : function() {
		return this.name;
	},
	
	setName : function(name) {
		this.name = name;
	},

	getTemplate : function() {
		return this.template;
	},
	
	setTemplate : function(/*boolean*/template) {
		this.template = template;
	},

	getPage : function() {
		return this.page;
	},

	setPage : function(page) {
		this.page = page;
	}
		
});
