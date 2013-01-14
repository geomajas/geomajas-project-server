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
package org.geomajas.sld;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * 
 An Extent gives feature/coverage/raster/matrix dimension extent.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Extent">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Name"/>
 *       &lt;xs:element ref="ns:Value"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ExtentInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String name;

	private ValueInfo value;

	/**
	 * Get the 'Name' element value.
	 * 
	 * @return value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the 'Name' element value.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the 'Value' element value.
	 * 
	 * @return value
	 */
	public ValueInfo getValue() {
		return value;
	}

	/**
	 * Set the 'Value' element value.
	 * 
	 * @param value
	 */
	public void setValue(ValueInfo value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ExtentInfo(name=" + this.getName() + ", value=" + this.getValue() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ExtentInfo)) {
			return false;
		}
		final ExtentInfo other = (ExtentInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
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
		return other instanceof ExtentInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		result = result * prime + (this.getValue() == null ? 0 : this.getValue().hashCode());
		return result;
	}
}