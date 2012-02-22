/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.client;

/**
 * GeometryTypes enumeration.
 * 
 * @author An Buyle
 * 
 */
public enum GeometryType {
	/**
	 * Unspecified geometry.
	 */
	UNSPECIFIED("unspecified"),
	/**
	 * Point-type geometry.
	 */
	POINT("point"),
	/**
	 * Line-type geometry.
	 */
	LINE("line"),
	/**
	 * Polygon-type geometry.
	 */
	POLYGON("polygon");

	private final String value;

	GeometryType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static GeometryType fromValue(String v) {
		for (GeometryType c : GeometryType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v.toString());
	}

}
