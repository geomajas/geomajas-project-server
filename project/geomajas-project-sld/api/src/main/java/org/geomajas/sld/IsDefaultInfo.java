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
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:boolean" name="IsDefault"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class IsDefaultInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private boolean isDefault;

	/**
	 * Get the 'IsDefault' element value.
	 * 
	 * @return value
	 */
	public boolean isIsDefault() {
		return isDefault;
	}

	/**
	 * Set the 'IsDefault' element value.
	 * 
	 * @param isDefault
	 */
	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "IsDefaultInfo(isDefault=" + this.isIsDefault() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof IsDefaultInfo)) {
			return false;
		}
		final IsDefaultInfo other = (IsDefaultInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.isIsDefault() != other.isIsDefault()) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof IsDefaultInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.isIsDefault() ? 1231 : 1237);
		return result;
	}
}