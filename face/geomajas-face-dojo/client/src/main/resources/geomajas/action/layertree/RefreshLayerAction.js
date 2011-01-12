dojo.provide("geomajas.action.layertree.RefreshLayerAction");
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
