dojo.provide("geomajas.map.print.RasterLayerComponent");
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
dojo.declare("RasterLayerComponent", BaseComponent, {

	/**
	 * @class 
	 * A raster layer component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/* json object */ json, /* unique string */ id) {
		this.javaClass = "org.geomajas.printing.component.RasterLayerComponent";
	},

	setLayer : function (layer) {
		/**
		 * True if layer is visible.
		 */
		this.visible = layer.checkVisibility();
		/**
		 * True if layer is selected.
		 */
		this.selected = layer.isSelected();
		/**
		 * ID of this layer.
		 */
		this.layerId = layer.getId();				
	}

});
