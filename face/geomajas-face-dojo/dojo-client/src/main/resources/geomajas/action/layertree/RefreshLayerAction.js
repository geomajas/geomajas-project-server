dojo.provide("geomajas.action.layertree.RefreshLayerAction");
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
dojo.require("geomajas.action.LayerTreeAction");

dojo.declare("RefreshLayerAction", LayerTreeAction, {

	/**
	 * Action for the layertree that refreshes the selected layer.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends LayerTreeAction
	 * @param layerTree Reference to the parent LayerTree widget.
	 */
	constructor : function (layerTree) {
		this.id = layerTree.id + ".RefreshLayerAction";

		this.layerTree = layerTree;

		/** The image for this tool's button. */
		this.image = "refreshLayerIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.RefreshLayerAction;

		/** Name of the feature table widget. */
		this.featureTable = "mainTable"; // default in this gegis module...
	},

	actionPerformed : function (event) {
		var layer = this.getLayerTree().getSelected();
		if (layer != null) {
			log.info("refreshing layer "+layer.getLabel());
			if(layer instanceof VectorLayer){
				layer.getFeatureStore().clear();
			}
			dojo.publish(this.layerTree.getMapModel().getRenderTopic(), [ layer, "delete"]);
			dojo.publish(this.layerTree.getMapModel().getRenderTopic(), [ layer, "all"]);
		}
	},

	getEnabledByLayer : function (layer) {
		if (layer == null) {
			return false;
		}
		return true;
	}
});
