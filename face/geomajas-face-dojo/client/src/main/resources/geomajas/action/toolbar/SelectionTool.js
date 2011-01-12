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

dojo.provide("geomajas.action.toolbar.SelectionTool");
dojo.require("geomajas.action.ToolbarTool");
dojo.require("geomajas.controller.SelectionController");

dojo.declare("SelectionTool", ToolbarTool, {

	/**
	 * @fileoverview Activate/Deactivate selection mode (DynamicToolbar).
	 * @class Tool for measuring distances. Uses a SelectionController behind
	 * the scenes.
	 * @author An Buyle
	 *
	 * @constructor
	 * @extends ToolbarTool
	 * @param id Unique identifier
	 * @param mapWidget MapWidget this tool has effect on.
	 * @param supportEditing Should editing be supported (passed on to the
	 *                       {@link SelectionController}).
	 * @param selectFromActiveLayerOnly: Should  only features from the active layer be selectable?
	  *@param coverageRatio	minimum ratio of area of feature geometry that has to be inside the
	 * 							selection rectangle to select/deselect it.
	 * @param pixelTolerance Number of pixels that describes the tolerance allowed when trying to select features
	 */
	constructor : function (id, mapWidget, supportEditing, selectFromActiveLayerOnly, coverageRatio, pixelTolerance) {
		/** Unique identifier */
		this.id = id;

		/** The image for this tool's button. */
		this.image = "selectionIcon";

		/** The tooltip reference. */
		this.tooltip = this.tooltipLocale.RectangleSelectionTool;
		
		/** Reference to the MapWidget. */
		this.mapWidget = mapWidget;

		var supportEditingAsBool;
		var selectFromActiveLayerOnlyAsBool;

		if (dojo.isString(supportEditing)) {
			supportEditingAsBool = (supportEditing == "true");
		}
		else { 
			supportEditingAsBool = supportEditing;
		}
		
		if (selectFromActiveLayerOnly && dojo.isString(selectFromActiveLayerOnly)) {
			selectFromActiveLayerOnlyAsBool = (selectFromActiveLayerOnly == "true");
		}
		else { 
			selectFromActiveLayerOnlyAsBool = selectFromActiveLayerOnly;
		}

		if (coverageRatio) {
			if (coverageRatio > 1) {
				this.coverageRatio = 1;
			} else if (coverageRatio < 0.1) {
				this.coverageRatio = 0.1;
			}
		} else {
			this.coverageRatio = 0.7;
		}

		if (pixelTolerance) {
			this.pixelTolerance = pixelTolerance;
		} else {
			this.pixelTolerance = 5;
		}

		/** MouseListener that defines the actions for mouse-events on the map */
		this.controller = new SelectionController(mapWidget, supportEditingAsBool,
				selectFromActiveLayerOnlyAsBool, this.coverageRatio, this.pixelTolerance);		
	},

	/**
	 * Add the SelectionController to the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onSelect : function (event) {
		this.selected = true;
		this.mapWidget.setController(this.controller);
	},

	/**
	 * Remove the SelectionController from the MapWidget.
	 * @param event Standard browser mouse-event.
	 */
	onDeSelect : function (event) {
		this.selected = false;
		this.mapWidget.setController(null);
	}
});