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
package org.geomajas.sld.filter;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ogc="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" 
 * type="ogc:BinarySpatialOpType" substitutionGroup="ogc:spatialOps" name="Overlaps"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class OverlapsInfo extends BinarySpatialOpTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	@Override
	public java.lang.String toString() {
		return "OverlapsInfo()";
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof OverlapsInfo)) {
			return false;
		}
		final OverlapsInfo other = (OverlapsInfo) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		return true;
	}

	/**
	 * Check whether the object can be compared with this.
	 * 
	 * @param other other object
	 * @return true when object can be compared
	 */
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof OverlapsInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		return result;
	}
}