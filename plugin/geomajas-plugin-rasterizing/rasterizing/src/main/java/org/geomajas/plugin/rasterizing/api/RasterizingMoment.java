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

package org.geomajas.plugin.rasterizing.api;

import org.geomajas.global.Api;

/**
 * Possible rasterizing behaviour, when should the tile be initially rasterized.
 *
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public enum RasterizingMoment {

	TILE_REQUEST("TILE"),
	URL_REQUEST("URL");

	private final String value;

	/**
	 * Create primitive type.
	 *
	 * @param v value
	 */
	RasterizingMoment(String v) {
		value = v;
	}

	/**
	 * Get enum value from.
	 *
	 * @param value string representation for enum
	 * @return enum value
	 */
	public static RasterizingMoment fromValue(String value) {
		for (RasterizingMoment c : RasterizingMoment.values()) {
			if (c.value.equals(value)) {
				return c;
			}
		}
		throw new IllegalArgumentException(value);
	}

	/**
	 * Get string representation of enum.
	 *
	 * @return string representation
	 */
	@Override
	public String toString() {
		return value;
	}

}
