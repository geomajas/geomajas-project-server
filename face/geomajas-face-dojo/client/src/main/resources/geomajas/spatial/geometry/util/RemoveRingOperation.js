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