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
package org.geomajas.configuration;

import org.geomajas.global.Api;

/**
 * List of possible primitive types.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public enum PrimitiveType {

	BOOLEAN("boolean"),
	SHORT("short"),
	INTEGER("integer"),
	LONG("long"),
	FLOAT("float"),
	DOUBLE("double"),
	CURRENCY("currency"),
	STRING("string"),
	DATE("date"),
	URL("url"),
	IMGURL("imgurl");

	private final String value;

	/**
	 * Create primitive type.
	 *
	 * @param v value
	 */
	PrimitiveType(String v) {
		value = v;
	}

	/**
	 * Get enum value from.
	 *
	 * @param value string representation for enum
	 * @return enum value
	 */
	public static PrimitiveType fromValue(String value) {
		for (PrimitiveType c : PrimitiveType.values()) {
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
