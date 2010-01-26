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