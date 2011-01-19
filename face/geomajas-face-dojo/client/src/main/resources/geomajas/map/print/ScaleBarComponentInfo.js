dojo.provide("geomajas.map.print.ScaleBarComponentInfo");
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
dojo.declare("ScaleBarComponentInfo", PrintComponentInfo, {

	/**
	 * @class 
	 * A map component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function () {
//		private String unit = "units";
//		private int ticNumber;
//		private FontStyleInfo font;

		this.javaClass = "org.geomajas.plugin.printing.component.dto.ScaleBarComponentInfo";
		
		this.unit = "units";
		this.getLayoutConstraint().setAlignmentX(geomajas.LayoutConstraints.LEFT);
		this.getLayoutConstraint().setAlignmentY(geomajas.LayoutConstraints.BOTTOM);
		this.getLayoutConstraint().setMarginX(20);
		this.getLayoutConstraint().setMarginY(20);
		this.getLayoutConstraint().setWidth(200);
		this.font = new FontStyleInfo();
		this.font.setFamily("Dialog");
		this.font.setStyle("Plain");
		this.font.setSize(10);
	},

	getUnit : function () {
		return this.tunit;
	},

	setUnit : function (unit) {
		this.unit = unit;
	},

	getTicNumber : function () {
		return this.tticNumber;
	},

	setTicNumber : function (ticNumber) {
		this.ticNumber = ticNumber;
	},

	getFont : function () {
		return this.tfont;
	},

	setFont : function (font) {
		this.font = font;
	}

});
