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