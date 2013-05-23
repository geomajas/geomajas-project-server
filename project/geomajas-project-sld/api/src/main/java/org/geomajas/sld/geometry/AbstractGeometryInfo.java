/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.geometry;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:anyType" abstract="true" name="_Geometry"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public abstract class AbstractGeometryInfo implements Serializable {

	private static final long serialVersionUID = 100;

	protected AbstractGeometryInfo() {
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "AbstractGeometryInfo()";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AbstractGeometryInfo)) {
			return false;
		}
		final AbstractGeometryInfo other = (AbstractGeometryInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		return true;
	}

	/**
	 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
	 *
	 * @param other other object
	 * @return true when other is an instance of this type
	 */
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof AbstractGeometryInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		int result = 1;
		return result;
	}
}