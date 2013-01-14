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
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" 
 * type="xs:string" name="Abstract"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class AbstractInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String aAbstract;

	/**
	 * Get the 'Abstract' element value.
	 * 
	 * @return abstract value
	 */
	public String getAbstract() {
		return aAbstract;
	}

	/**
	 * Set the 'Abstract' element value.
	 * 
	 * @param aAbstract abstract element
	 */
	public void setAbstract(String aAbstract) {
		this.aAbstract = aAbstract;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "AbstractInfo(aAbstract=" + this.aAbstract + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AbstractInfo)) {
			return false;
		}
		final AbstractInfo other = (AbstractInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.aAbstract == null ? other.aAbstract != null : !this.aAbstract
				.equals((java.lang.Object) other.aAbstract)) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof AbstractInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.aAbstract == null ? 0 : this.aAbstract.hashCode());
		return result;
	}
}