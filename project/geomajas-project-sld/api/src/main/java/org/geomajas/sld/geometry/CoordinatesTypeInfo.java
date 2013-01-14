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
 * 
 Coordinates can be included in a single string, but there is no facility for validating string content. The value of
 * the 'cs' attribute is the separator for coordinate values, and the value of the 'ts' attribute gives the tuple
 * separator (a single space by default); the default values may be changed to reflect local usage.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="CoordinatesType">
 *   &lt;xs:simpleContent>
 *     &lt;xs:extension base="xs:string">
 *       &lt;xs:attribute type="xs:string" use="optional" default="." name="decimal"/>
 *       &lt;xs:attribute type="xs:string" use="optional" default="," name="cs"/>
 *       &lt;xs:attribute type="xs:string" use="optional" default=" " name="ts"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:simpleContent>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class CoordinatesTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String string;

	private String decimal;

	private String cs;

	private String ts;

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
	 * Get the 'decimal' attribute value.
	 * 
	 * @return value
	 */
	public String getDecimal() {
		return decimal;
	}

	/**
	 * Set the 'decimal' attribute value.
	 * 
	 * @param decimal
	 */
	public void setDecimal(String decimal) {
		this.decimal = decimal;
	}

	/**
	 * Get the 'cs' attribute value.
	 * 
	 * @return value
	 */
	public String getCs() {
		return cs;
	}

	/**
	 * Set the 'cs' attribute value.
	 * 
	 * @param cs
	 */
	public void setCs(String cs) {
		this.cs = cs;
	}

	/**
	 * Get the 'ts' attribute value.
	 * 
	 * @return value
	 */
	public String getTs() {
		return ts;
	}

	/**
	 * Set the 'ts' attribute value.
	 * 
	 * @param ts
	 */
	public void setTs(String ts) {
		this.ts = ts;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "CoordinatesTypeInfo(string=" + this.getString() + ", decimal=" + this.getDecimal() + ", cs="
				+ this.getCs() + ", ts=" + this.getTs() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CoordinatesTypeInfo)) {
			return false;
		}
		final CoordinatesTypeInfo other = (CoordinatesTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getString() == null ? other.getString() != null : !this.getString().equals(
				(java.lang.Object) other.getString())) {
			return false;
		}
		if (this.getDecimal() == null ? other.getDecimal() != null : !this.getDecimal().equals(
				(java.lang.Object) other.getDecimal())) {
			return false;
		}
		if (this.getCs() == null ? other.getCs() != null : !this.getCs().equals((java.lang.Object) other.getCs())) {
			return false;
		}
		if (this.getTs() == null ? other.getTs() != null : !this.getTs().equals((java.lang.Object) other.getTs())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof CoordinatesTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getString() == null ? 0 : this.getString().hashCode());
		result = result * prime + (this.getDecimal() == null ? 0 : this.getDecimal().hashCode());
		result = result * prime + (this.getCs() == null ? 0 : this.getCs().hashCode());
		result = result * prime + (this.getTs() == null ? 0 : this.getTs().hashCode());
		return result;
	}
}