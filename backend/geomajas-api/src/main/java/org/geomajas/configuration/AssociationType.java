/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.configuration;

/**
 * Association type.
 *
 * @author Joachim Van der Auwera
 */
public enum AssociationType {

	MANY_TO_ONE("many-to-one"),
	ONE_TO_MANY("one-to-many");

	private final String value;

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
