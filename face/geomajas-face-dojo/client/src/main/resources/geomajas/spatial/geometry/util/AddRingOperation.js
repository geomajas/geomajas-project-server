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