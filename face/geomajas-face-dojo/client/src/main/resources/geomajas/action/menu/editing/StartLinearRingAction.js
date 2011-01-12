dojo.provide("geomajas.action.menu.editing.StartLinearRingAction");
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
dojo.require("geomajas.action.Action");

dojo.declare("StartLinearRingAction", Action, {

	/**
	 * @fileoverview Action for starting a new hole. (rightmouse menu)
	 * @class Right mouse menu during editing for creating a new hole in a
	 * polygon.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends Action
	 * @param id This action's unique identifier.
	 * @param mapWidget Reference to a MapWidget object.
	 */
	constructor : function (id, mapWidget) {
		/** @private */
		this.mapWidget = mapWidget;

		/** Unique identifier */
		this.id = id;

		/** The action can be displayed as text only. */
		this.text = "Create hole";
		
		/** @private */
		this.editor = new GeometryEditor();
	},

	/**
	 * Set everything ready for the start of a new hole. This means setting the
	 * {@link EditingController} in INSERT_MODE, creating a new empty
	 * {@link LinearRing}, and adjusting the controller's geometry-index so 
	 * that the executing controller can find this empty ring.
	 */
	actionPerformed : function (event) {
		var curCon = this.mapWidget.getCurrentController();
		if (curCon == null || !(curCon instanceof EditingController)) {
			curCon = new EditingController (this.mapWidget);
			curCon._updateController();
			this.mapWidget.setController(curCon);
		}
		curCon.setMode(curCon.statics.INSERT_MODE);
		var trans = this.mapWidget.getMapModel().getFeatureEditor().getFeatureTransaction();
		
		var geometry = trans.getNewFeatures()[curCon.getFeatureIndex()].getGeometry();
		curCon.setGeometryIndex(curCon.getController().getNewRingIndex(geometry));
		var hole = geometry.getGeometryFactory().createLinearRing(null);
		var operation = new AddRingOperation (curCon.getGeometryIndex()[0], hole);
		this.editor.edit(geometry, operation);
	}
});
