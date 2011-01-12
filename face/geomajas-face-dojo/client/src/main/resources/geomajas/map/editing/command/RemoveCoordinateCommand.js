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

dojo.provide("geomajas.map.editing.command.RemoveCoordinateCommand");
dojo.require("geomajas.map.editing.command.EditCommand");

dojo.declare("RemoveCoordinateCommand", EditCommand, {

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
		this.coordinate = null;

		/** @private */
		this.editor = new GeometryEditor();
	},

	/**
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	execute : function (features) {
		if (features != null && features.length != 0) {
			var geometry = features[this.index[0]].getGeometry();
			this.coordinate = geometry.getGeometryN(this.index[1]).clone();
			this.editor.edit(geometry, new RemoveCoordinateOperation(this.index[1]));
		}
	},

	/**
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	undo : function (features) {
		if (features != null && features.length != 0) {
			var geometry = features[this.index[0]].getGeometry();
			this.editor.edit(geometry, new InsertCoordinateOperation(this.index[1], this.coordinate));
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
