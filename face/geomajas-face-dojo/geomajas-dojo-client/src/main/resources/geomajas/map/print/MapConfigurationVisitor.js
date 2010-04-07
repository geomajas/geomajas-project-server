dojo.provide("geomajas.map.print.MapConfigurationVisitor");
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
dojo.declare("MapConfigurationVisitor", BaseComponentVisitor, {

	/**
	 * @class Visits the print page and applies the current layer configuration
	 *        to the main map and legend (as tagged).
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/*Layer*/ mapModel) {
		this.mapModel = mapModel;
	},
	
	visitMap : function (map) {
		if(map.getTag() == "map") {
			map.copyLayers(this.mapModel);
		}
	},

	visitLegend : function (legend) {
		if(legend.getTag() == "legend") {
			legend.copyLayers(this.mapModel);
		}
	}
	
});
