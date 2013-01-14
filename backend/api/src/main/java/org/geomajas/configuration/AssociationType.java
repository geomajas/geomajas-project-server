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
package org.geomajas.configuration;

import org.geomajas.annotation.Api;

/**
 * Association type.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public enum AssociationType {

	/** Many to one association. */
	MANY_TO_ONE("many-to-one"),
	/** One to meny association. */
	ONE_TO_MANY("one-to-many");

	private final String value;

	/**
	 * Create an {@link AssociationType}.
	 *
	 * @param v value
	 */
	AssociationType(String v) {
		value = v;
	}

	/**
	 * Convert from string to enum value.
	 *
	 * @param value string representation
	 * @return enum value
	 */
	public static AssociationType fromValue(String value) {
		for (AssociationType c : AssociationType.values()) {
			if (c.value.equals(value)) {
				return c;
			}
		}
		throw new IllegalArgumentException(value);
	}

	/**
	 * Convert to string representation.
	 *
	 * @return string value
	 */
	@Override
	public String toString() {
		return value;
	}

}
