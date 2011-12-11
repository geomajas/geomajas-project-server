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

package org.geomajas.layer;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * <p>
 * Listing of all types of vector layers.
 * </p>
 *
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public enum LayerType implements Serializable {

	/** Raster layer. */
	RASTER(1),
	/** Vector layer with point features. */
	POINT(2),
	/** Vector layer with linestring features. */
	LINESTRING(3),
	/** Vector layer with polygon features. */
	POLYGON(4),
	/** Vector layer with multi-point features. */
	MULTIPOINT(5),
	/** Vector layer with multi-linestring features. */
	MULTILINESTRING(6),
	/** Vector layer with multi-polygon features. */
	MULTIPOLYGON(7),
	/** Vector layer without limitation on the geometry type. */
	GEOMETRY(8);

	private int code;

	/**
	 * Create layer type.
	 *
	 * @param code code to apply
	 */
	private LayerType(int code) {
		this.code = code;
	}

	/**
	 * Convert to string.
	 *
	 * @return string representation of layer type
	 */
	public String toString() {
		return Integer.toString(code);
	}
}