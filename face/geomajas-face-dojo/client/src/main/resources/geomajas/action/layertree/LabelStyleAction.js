dojo.provide("geomajas.action.layertree.LabelStyleAction");
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
dojo.require ("geomajas.action.LayerTreeAction");

dojo.declare("LabelStyleAction", LayerTreeAction, {

	/**
	 * @fileoverview Activate/Deactivate labelling (for LayerTree widget).
	 * @class Implementation of the LayerTreeTool class, for the
	 * LayerTree widget. This activesettable determines whether or not the
	 * labels for a layer should be visible or not.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends LayerTreeTool
	 * @param layerTree The LayerTree widget where this object should be
	 *                  added.
	 */
	constructor : function (layerTree) {
		/** Reference to the LayerTree widget */
		this.layerTree = layerTree;

		/** Identifier */
		this.id = layerTree.id + ".labelStyle";

		/** The name of the image that is to represent this tool. */
		this.image = "labelStyleIcon";

		/** Tooltip */
		this.tooltip = this.tooltipLocale.LabelStyleAction;
	},

	actionPerformed : function (event) {
	},
	
	getEnabledByLayer : function (layer) {
		if (layer == null || !(layer instanceof VectorLayer)) {
			return false;
		}
		return true;
	}
});
