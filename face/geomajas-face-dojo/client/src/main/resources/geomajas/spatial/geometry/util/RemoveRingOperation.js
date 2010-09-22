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

dojo.provide("geomajas.spatial.geometry.util.RemoveRingOperation");
dojo.declare("RemoveRingOperation", GeometryOperation, {

	/**
	 * @fileoverview Operation for deleting a ring to a polygon or multipolygon.
	 * @class Extension of the {@link GeometryOperation}, for deleting a new ring
	 * to a {@link Polygon} or {@link MultiPolygon} object.
	 * @author Pieter De Graef
	 * 
	 * @constructor
	 * @extends GeometryOperation
	 * @param index Index to uniquely identify the polygon. Only needed if the
	 *              passed geometry is a {@link MultiPolygon}.
	 * @param ring The {@link LinearRing} object to add to the polygon.
	 */
	constructor : function (index) {
		this.index = index;
		
		/** Will store the deleted ring after execution of the {@link #edit} function */
		this.removed = null;
	},

	/**
	 * The actual deletion of the ring.
	 * @param geometry Should be of the type MULTIPLOYGON or POLYGON.
	 */
	edit : function (geometry) {
		if (geometry instanceof MultiPolygon) {
			if (this.index instanceof Array && this.index.length == 2) {
				var polygon = geometry.getGeometryN(this.index[0]);
				this.removed = polygon.getHoles().splice(this.index[1]-1, 1);
			} else if (this.index instanceof Array && this.index.length == 1) {
				var polygon = geometry.getGeometryN(0);
				this.removed = polygon.getHoles().splice(this.index[0]-1, 1);
			} else {
				var polygon = geometry.getGeometryN(0);
				this.removed = polygon.getHoles().splice(this.index-1, 1);
			}
		} else if (geometry instanceof Polygon) {
			if (this.index instanceof Array && this.index.length == 1) {
				this.removed = geometry.getHoles().splice(this.index[0]-1, 1);
			} else {
				this.removed = geometry.getHoles().splice(this.index-1, 1);
			}
		}
	},

	/**
	 * Can return the deleted ring after the {@link #edit} function has been
	 * called.
	 * @return A {@link LinearRing} object.
	 */
	getRemovedRing : function () {
		if (this.removed instanceof Array) {
			return this.removed[0];
		}
		return null;
	}
});