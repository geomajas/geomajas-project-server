dojo.provide("geomajas.map.print.MapConfigurationVisitor");
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
