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
package org.geomajas.sld;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="VendorOption">
 *   &lt;xs:complexType mixed="true">
 *     &lt;xs:simpleContent>
 *       &lt;xs:extension base="xs:string">
 *         &lt;xs:attribute type="xs:string" name="name"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:simpleContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class VendorOptionInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String string;

	private String name;

	/**
	 * Get the extension value.
	 * 
	 * @return value
	 */
	public String getString() {
		return string;
	}

	/**
	 * Set the extension value.
	 * 
	 * @param string
	 */
	public void setString(String string) {
		this.string = string;
	}

	/**
	 * Get the 'name' attribute value.
	 * 
	 * @return value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the 'name' attribute value.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "VendorOptionInfo(string=" + this.getString() + ", name=" + this.getName() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof VendorOptionInfo)) {
			return false;
		}
		final VendorOptionInfo other = (VendorOptionInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getString() == null ? other.getString() != null : !this.getString().equals(
				(java.lang.Object) other.getString())) {
			return false;
		}
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof VendorOptionInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getString() == null ? 0 : this.getString().hashCode());
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		return result;
	}
}