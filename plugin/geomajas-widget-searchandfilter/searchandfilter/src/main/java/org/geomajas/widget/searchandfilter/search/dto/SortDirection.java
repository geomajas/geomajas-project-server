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

package org.geomajas.widget.searchandfilter.search.dto;

/**
 * @author An Buyle
 */

import java.io.Serializable;


/**
 * Enumeration class to specify the direction of a sort.
 * @author BuyleA
 *
 */
public enum SortDirection implements Serializable {
	/**
	 * Sort in ascending order (eg: A-Z, larger items later in the list).
	 */
	ASCENDING("ascending"),
	/**
	 * Sort in descending order (eg: Z-A, larger items earlier in the list).
	 */
	DESCENDING("descending");
	private String value;

	// ----------------------------------------------------------
	SortDirection(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	/**
	 * Convert from string to enum value.
	 * 
	 * @param value
	 *            string representation
	 * @return enum value
	 */
	public static SortDirection fromValue(String value) {
		for (SortDirection c : SortDirection.values()) {
			if (c.value.equals(value)) {
				return c;
			}
		}
		throw new IllegalArgumentException(value);
	}
}
