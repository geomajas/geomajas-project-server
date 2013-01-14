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
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" mixed="true" name="DistanceType">
 *   &lt;xs:attribute type="xs:string" use="required" name="units"/>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class DistanceTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String units;

	private String value;

	/**
	 * Get the 'units' attribute value.
	 * 
	 * @return value
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * Set the 'units' attribute value.
	 * 
	 * @param units
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	/**
	 * Get the value of the distance.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value of the distance.
	 * 
	 * @param value the value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "DistanceTypeInfo(units=" + this.getUnits() + ", value=" + this.getValue() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof DistanceTypeInfo)) {
			return false;
		}
		final DistanceTypeInfo other = (DistanceTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getUnits() == null ? other.getUnits() != null : !this.getUnits().equals(
				(java.lang.Object) other.getUnits())) {
			return false;
		}
		if (this.getValue() == null ? other.getValue() != null : !this.getValue().equals(
				(java.lang.Object) other.getValue())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof DistanceTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getUnits() == null ? 0 : this.getUnits().hashCode());
		result = result * prime + (this.getValue() == null ? 0 : this.getValue().hashCode());
		return result;
	}
}