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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="Value"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ValueInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String value;

	/**
	 * Get the 'Value' element value.
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the 'Value' element value.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ValueInfo(value=" + this.getValue() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ValueInfo)) {
			return false;
		}
		final ValueInfo other = (ValueInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getValue() == null ? other.getValue() != null : !this.getValue().equals(
				(java.lang.Object) other.getValue())) {
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
		return other instanceof ValueInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getValue() == null ? 0 : this.getValue().hashCode());
		return result;
	}
}