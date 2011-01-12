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

dojo.provide("geomajas.map.editing.command.AddCoordinateCommand");
dojo.require("geomajas._base");
dojo.require("geomajas.map.editing.command.EditCommand");

dojo.declare("AddCoordinateCommand", EditCommand, {

	/**
	 * @fileoverview EditCommand adding a new coordinate to a feature.
	 * @class Adds a coordinate to a FeatureTransaction's newFeature.
	 * @author Jan De Moerloose & Pieter De Graef
	 *
	 * @constructor
	 * @extends EditCommand
	 * @param coordinate The {@link Coordinate} object to be added to the geometry.
	 */
	constructor : function (index, coordinate) {
		this.index = index;
		
		/** The coordinate to be added to a feature. */
		this.coordinate = coordinate;
	},

	/**
	 * Adds the coordinate, set on this command, to the feature's geometry.
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	execute : function (features) {
		if (features != null && features.length != 0) {
			var geometry = features[this.index[0]].getGeometry().getGeometryN(this.index[1]);
			geometry.appendCoordinate(this.coordinate);
		}
	},

	/**
	 * Removes the last coordinate from this feature's geometry.
	 * @param feature Reference to the newFeature from the FeatureTransaction.
	 */
	undo : function (features) {
		if (features != null && features.length != 0) {
			var geometry = features[this.index[0]].getGeometry().getGeometryN(this.index[1]);
			var num = geometry.getNumGeometries();
			if(geometry.getGeometryType() == geomajas.GeometryTypes.LINEARRING) {
				num = num - 2;				
			} else if (geometry.getGeometryType() == geomajas.GeometryTypes.LINESTRING || geometry.getGeometryType() == geomajas.GeometryTypes.POINT) {
				num = num - 1;
			}
			//this.index[1].push(num);
			var op = new RemoveCoordinateOperation(num);
			var ed = new GeometryEditor();
			ed.edit(geometry, op);
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