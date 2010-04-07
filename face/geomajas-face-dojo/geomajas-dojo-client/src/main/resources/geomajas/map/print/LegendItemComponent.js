dojo.provide("geomajas.map.print.LegendItemComponent");
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
