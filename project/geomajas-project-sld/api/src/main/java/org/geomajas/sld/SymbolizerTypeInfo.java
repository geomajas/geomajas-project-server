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
 A "SymbolizerType" is an abstract type for encoding the graphical properties used to portray geographic information.
 * Concrete symbol types are derived from this base type.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" abstract="true" name="SymbolizerType">
 *   &lt;xs:attribute type="xs:string" name="uom"/>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public abstract class SymbolizerTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String uom;

	/**
	 * Get the 'uom' attribute value.
	 * 
	 * @return value
	 */
	public String getUom() {
		return uom;
	}

	/**
	 * Set the 'uom' attribute value.
	 * 
	 * @param uom
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "SymbolizerTypeInfo(uom=" + this.getUom() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SymbolizerTypeInfo)) {
			return false;
		}
		final SymbolizerTypeInfo other = (SymbolizerTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getUom() == null ? other.getUom() != null : !this.getUom().equals((java.lang.Object) other.getUom())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof SymbolizerTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getUom() == null ? 0 : this.getUom().hashCode());
		return result;
	}
}