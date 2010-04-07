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

dojo.provide("geomajas.map.editing.command.RemoveRingCommand");
dojo.require("geomajas.map.editing.command.EditCommand");

dojo.declare("RemoveRingCommand", EditCommand, {

	/**
	 * @fileoverview EditCommand for removing a coordinate from a feature.
	 * @class Removes a coordinate from a FeatureTransaction's newFeature.
	 * @author Jan De Moerloose & Pieter De Graef
	 *
	 * @constructor
	 * @extends EditCommand
	 * @param index The index of the coordinate that should be removed.
	 */
	constructor : function (index) {
		this.index = index;

		/** @private */
		this.ring = null;

		/** @private */
		this.editor = new GeometryEditor();
	},

	/**
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	execute : function (features) {
		if (features != null && features.length != 0) {
			var geometry = features[this.index[0]].getGeometry();
			var operation = new RemoveRingOperation(this.index[1]);
			this.editor.edit(geometry, operation);
			this.ring = operation.getRemovedRing();
		}
	},

	/**
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	undo : function (features) {
		if (features != null && features.length != 0) {
			var geometry = features[this.index[0]].getGeometry();
			var operation = new AddRingOperation (this.index[1], this.ring);
			this.editor.edit(geometry, operation);
		}
	},

	// Getters and setters:

	getCoordinate : function () {
		return this.coordinate;
	},

	setCoordinate : function (coordinate) {
		this.coordinate = coordinate;
	}
});