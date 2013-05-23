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

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "AbstractInfo(aAbstract=" + this.aAbstract + ")";
	}

	@Override
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AbstractInfo)) {
			return false;
		}
		final AbstractInfo other = (AbstractInfo) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.aAbstract == null ? other.aAbstract != null : !this.aAbstract.equals(other.aAbstract)) {
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
		return other instanceof AbstractInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.aAbstract == null ? 0 : this.aAbstract.hashCode());
		return result;
	}
}