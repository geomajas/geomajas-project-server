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

dojo.provide("geomajas.spatial.geometry.util.AddRingOperation");
dojo.require("geomajas._base");

dojo.declare("AddRingOperation", GeometryOperation, {

	/**
	 * @fileoverview Operation for adding a ring to a polygon or multipolygon.
	 * @class Extension of the {@link GeometryOperation}, for adding a new ring
	 * to a {@link Polygon} or {@link MultiPolygon} object.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends GeometryOperation
	 * @param index Index to uniquely identify the polygon. Only needed if the
	 *              passed geometry is a {@link MultiPolygon}.
	 * @param ring The {@link LinearRing} object to add to the polygon.
	 */
	constructor : function (index, ring) {
		this.index = index;
		this.ring = ring;
	},

	/**
	 * The actual adding of the ring.
	 * @param geometry Should be of the type MULTIPLOYGON or POLYGON.
	 */
	edit : function (geometry) {
		var type = geometry.getGeometryType();
		if (type == geomajas.GeometryTypes.MULTIPOLYGON) {
			if (this.index instanceof Array && this.index.length > 0) {
				var polygon = geometry.getGeometryN(this.index[0]);
				polygon.getHoles().push(this.ring);
			} else {
				if (this.index == null) {
					this.index = 0;
				}
				var polygon = geometry.getGeometryN(this.index);
				polygon.getHoles().push(this.ring);
			}
		} else if (type == geomajas.GeometryTypes.POLYGON) {
			geometry.getHoles().push(this.ring);
		}
	}
});