/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.editing.jsapi.client.service;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.editing.client.service.GeometryIndex;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;

/**
 * Definition of an index in a geometry. This index will point to a specific sub-part of a geometry. Depending on the
 * "type", this sub-part can be a vertex, an edge or a sub-geometry.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export("GeometryIndex")
@ExportPackage("org.geomajas.plugin.editing.service")
@Api(allMethods = true)
public class JsGeometryIndex implements ExportOverlay<GeometryIndex> {

	/**
	 * Get the type of sub-part this index points to. Can be a vertex, edge or sub-geometry.
	 * 
	 * @return The type of sub-part this index points to.
	 */
	@ExportInstanceMethod
	public static String getType(GeometryIndex instance) {
		switch (instance.getType()) {
			case TYPE_GEOMETRY:
				return "geometry";
			case TYPE_VERTEX:
				return "vertex";
			case TYPE_EDGE:
				return "edge";
			default:
				return "unknown";
		}
	}

	/**
	 * Does this index have a child index or not? If this index points to a sub-geometry, and a child may point to some
	 * part within the sub-geometry. Recursiveness rules the world.
	 * 
	 * @return true or false.
	 */
	public boolean hasChild() {
		return false;
	}

	/**
	 * Get the child index. If this index points to a sub-geometry, and a child may point to some part within the
	 * sub-geometry.
	 * 
	 * @return Returns the child index, or null if there is no child.
	 */
	public GeometryIndex getChild() {
		return null;
	}

	/**
	 * Get the index value for this index. This value tells us to exactly which vertex/edge/sub-geometry we are
	 * pointing. Vertices and geometries just point to the index in the respective arrays in a geometry, while edges
	 * point to the edge after the vertex with the same index value (edge 0 has coordinate 0 and 1).
	 * 
	 * @return The integer index value.
	 */
	public int getValue() {
		return 0;
	}

	/** {@inheritDoc} */
	public String toString() {
		return "";
	}
}