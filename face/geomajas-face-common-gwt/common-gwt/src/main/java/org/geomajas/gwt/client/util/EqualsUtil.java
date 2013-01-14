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
package org.geomajas.gwt.client.util;

import org.geomajas.annotation.Api;

/**
 * Utility for equality checking.
 * 
 * @author Jan De Moerloose
 * @since 0.0.0
 */
@Api(allMethods = true)
public final class EqualsUtil {

	private EqualsUtil() {
		// hide constructor
	}

	/**
	 * Checks whether 2 objects are equal. Null-safe, 2 null objects are considered equal.
	 * 
	 * @param o1 first object to compare
	 * @param o2 second object to compare
	 * @return true if object are equal, false otherwise
	 */
	public static boolean isEqual(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}
}
