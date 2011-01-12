dojo.provide("geomajas.map.print.LegendItemComponent");
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
dojo.declare("LegendItemComponent", BaseComponent, {

	/**
	 * @class 
	 * A legend item component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/* json object */ json, /* unique string */ id) {
		this.javaClass = "org.geomajas.printing.component.LegendItemComponent";
	},
	
	init : function( /* style def */def, /* string */label, /* type */ type, /* font */ font) {
		// icon component
		var icon = new BaseComponent({
			javaClass : "org.geomajas.printing.component.LegendIconComponent",
			label: label,
			layerType: type,
			def: def,
			id : this.id+".icon"
		});
		var label = new LabelComponent({
			javaClass : "org.geomajas.printing.component.LabelComponent",
			font: font,
			text: label,
			textOnly: true,
			id : this.id+".label"
		});
		this.addComponent(icon);
		this.addComponent(label);
	}

});
