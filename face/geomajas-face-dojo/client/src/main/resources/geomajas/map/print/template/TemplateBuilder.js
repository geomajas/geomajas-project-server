dojo.provide("geomajas.map.print.template.TemplateBuilder");
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
dojo.declare("TemplateBuilder", null, {

	/**
	 * @class 
	 * Builder pattern for templates.
	 * 
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
	},
	
	buildTemplate : function () {
		var template = new PrintTemplateInfo();
		template.setPage(this.buildPage());
		return template; /*PrintTemplateInfo*/
	},	
	
	buildPage : function () {
		var page = new PageComponentInfo();
		page.addChild(this.buildMap());
		page.addChild(this.buildTitle());
		page.setTag("page");
		return page; /*PageComponentInfo*/
	},
	
	buildMap : function () {
		var map = new MapComponentInfo();
		map.addChild(this.buildScaleBar());
		map.addChild(this.buildLegend());
		map.addChild(this.buildArrow());
		return map; /*MapComponentInfo*/
	},
	
	buildArrow : function () {
		var arrow = new ImageComponentInfo();
		return arrow; /*ImageComponentInfo*/
	},
	
	buildLegend : function () {
		var legend = new LegendComponentInfo();
		return legend; /*LegendComponentInfo*/
	},
	
	buildScaleBar : function () {
		var scaleBar = new ScaleBarComponentInfo();
		return scaleBar; /*ScaleBarComponentInfo*/
	},
	
	buildTitle : function () {
		var label = new LabelComponentInfo();
		var style = new FontStyleInfo();
		style.setFamily("Dialog");
		style.setStyle("Italic");
		style.setSize(14);
		label.setFont(style);
		label.setBackgroundColor("0xFFFFFF");
		label.setBorderColor("0x000000");
		label.setFontColor("0x000000");
		label.getLayoutConstraint().setAlignmentY(geomajas.LayoutConstraints.TOP);
		label.getLayoutConstraint().setAlignmentX(geomajas.LayoutConstraints.CENTER);
		label.setTag("title");
		return label; /*LabelComponentInfo*/
	}
	
});
