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
package org.geomajas.sld.geometry;

import java.io.Serializable;
import java.math.BigDecimal;
import org.geomajas.annotation.Api;

/**
 * 
 Represents a coordinate tuple in one, two, or three dimensions.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="CoordType">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:decimal" name="X"/>
 *     &lt;xs:element type="xs:decimal" name="Y" minOccurs="0"/>
 *     &lt;xs:element type="xs:decimal" name="Z" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class CoordTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private BigDecimal x;

	private BigDecimal y;

	private BigDecimal z;

	/**
	 * Get the 'X' element value.
	 * 
	 * @return value
	 */
	public BigDecimal getX() {
		return x;
	}

	/**
	 * Set the 'X' element value.
	 * 
	 * @param x
	 */
	public void setX(BigDecimal x) {
		this.x = x;
	}

	/**
	 * Get the 'Y' element value.
	 * 
	 * @return value
	 */
	public BigDecimal getY() {
		return y;
	}

	/**
	 * Set the 'Y' element value.
	 * 
	 * @param y
	 */
	public void setY(BigDecimal y) {
		this.y = y;
	}

	/**
	 * Get the 'Z' element value.
	 * 
	 * @return value
	 */
	public BigDecimal getZ() {
		return z;
	}

	/**
	 * Set the 'Z' element value.
	 * 
	 * @param z
	 */
	public void setZ(BigDecimal z) {
		this.z = z;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "CoordTypeInfo(x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CoordTypeInfo)) {
			return false;
		}
		final CoordTypeInfo other = (CoordTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getX() == null ? other.getX() != null : !this.getX().equals((java.lang.Object) other.getX())) {
			return false;
		}
		if (this.getY() == null ? other.getY() != null : !this.getY().equals((java.lang.Object) other.getY())) {
			return false;
		}
		if (this.getZ() == null ? other.getZ() != null : !this.getZ().equals((java.lang.Object) other.getZ())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof CoordTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getX() == null ? 0 : this.getX().hashCode());
		result = result * prime + (this.getY() == null ? 0 : this.getY().hashCode());
		result = result * prime + (this.getZ() == null ? 0 : this.getZ().hashCode());
		return result;
	}
}