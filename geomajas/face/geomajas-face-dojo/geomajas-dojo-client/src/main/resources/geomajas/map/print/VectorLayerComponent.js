dojo.provide("geomajas.map.print.VectorLayerComponent");
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
