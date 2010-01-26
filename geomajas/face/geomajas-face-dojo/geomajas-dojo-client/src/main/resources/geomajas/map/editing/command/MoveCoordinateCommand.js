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

dojo.provide("geomajas.map.editing.command.MoveCoordinateCommand");
dojo.require("geomajas.map.editing.command.EditCommand");

dojo.declare("MoveCoordinateCommand", EditCommand, {

	/**
	 * @fileoverview EditCommand for moving/dragging a coordinate from a feature.
	 * @class Moves one or more coordinates from a FeatureTransaction's
	 * newFeature.
	 * @author Pieter De Graef
	 *
	 * @constructor
	 * @extends EditCommand
	 * @param source The original coordinate ArrayList.
	 * @param translateX x-delta.
	 * @param translateY y-delta.
	 */
	constructor : function (index, position) {
		/** The original coordinate index. */
		this.index = index;

		/** The new position. */
		this.position = position;
		
		/** @private */
		this.editor = new GeometryEditor();

		/** @private The old position. For the undo! */
		this.origPosition = null;
	},

	/**
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	execute : function (features) {
		if (features != null && features.length != 0) {
			var geometry = features[0].getGeometry();
			var temp = geometry.getCoordinateN(this.index);
			if (temp != null) {
				this.origPosition = temp.clone();
				this.editor.edit(geometry, new SetCoordinateOperation(this.index, this.position));
			}
		}
	},

	/**
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	undo : function (features) {
		if (features != null && features.length != 0) {
			if (this.origPosition != null) {
				var geometry = features[0].getGeometry();
				this.editor.edit(geometry, new SetCoordinateOperation(this.index, this.origPosition));
			}
		}
	},

	// Getters and setters:

	getSource : function () {
		return this.source;
	},

	setSource : function (source) {
		this.source = source;
	},

	getTranslateX : function () {
		return this.translateX;
	},

	setTranslateX : function (translateX) {
		this.translateX = translateX;
	},

	getTranslateY : function () {
		return this.translateY;
	},

	setTranslateY : function (translateY) {
		this.translateY = translateY;
	}

});