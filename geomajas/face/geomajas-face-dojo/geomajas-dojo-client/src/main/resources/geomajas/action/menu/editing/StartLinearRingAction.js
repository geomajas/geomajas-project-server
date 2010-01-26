dojo.provide("geomajas.action.menu.editing.StartLinearRingAction");
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
