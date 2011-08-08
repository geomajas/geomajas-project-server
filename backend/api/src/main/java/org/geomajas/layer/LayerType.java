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

	RASTER(1),
	POINT(2),
	LINESTRING(3),
	POLYGON(4),
	MULTIPOINT(5),
	MULTILINESTRING(6),
	MULTIPOLYGON(7),
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