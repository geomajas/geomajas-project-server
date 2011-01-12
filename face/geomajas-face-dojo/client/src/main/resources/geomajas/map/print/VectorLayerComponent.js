dojo.provide("geomajas.map.print.VectorLayerComponent");
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
dojo.declare("VectorLayerComponent", BaseComponent, {

	/**
	 * @class 
	 * A vector layer component (mirror of server object)
	 * @author Jan De Moerloose
	 *
	 * @constructor
	 */
	constructor : function (/* json object */ json, /* unique string */ id) {
		this.javaClass = "org.geomajas.printing.component.VectorLayerComponent";
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
		/**
		 * Array of stylefilters for this layer.
		 */
		this.styleDefs = layer.getStyles().toArray();
		/**
		 * Array of selected feature id's.
		 */
		this.selectedFeatureIds = layer.getSelectionStore().getKeyList();
		/**
		 * CQL filter.
		 */
		this.filter = layer.isFilterEnabled() ? layer.getFilterString() : null;
		/**
		 * True if labels are visible
		 */
		this.labelsVisible = layer.isLabeled();
				
	}


});
