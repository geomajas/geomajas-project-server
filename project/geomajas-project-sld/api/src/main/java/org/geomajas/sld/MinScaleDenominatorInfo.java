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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:double" name="MinScaleDenominator"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class MinScaleDenominatorInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private Double minScaleDenominator;

	/**
	 * Get the 'MinScaleDenominator' element value.
	 * 
	 * @return value
	 */
	public Double getMinScaleDenominator() {
		return minScaleDenominator;
	}

	/**
	 * Set the 'MinScaleDenominator' element value.
	 * 
	 * @param minScaleDenominator
	 */
	public void setMinScaleDenominator(Double minScaleDenominator) {
		this.minScaleDenominator = minScaleDenominator;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "MinScaleDenominatorInfo(minScaleDenominator=" + this.getMinScaleDenominator() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof MinScaleDenominatorInfo)) {
			return false;
		}
		final MinScaleDenominatorInfo other = (MinScaleDenominatorInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getMinScaleDenominator() == null ? other.getMinScaleDenominator() != null : !this
				.getMinScaleDenominator().equals((java.lang.Object) other.getMinScaleDenominator())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof MinScaleDenominatorInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime
				+ (this.getMinScaleDenominator() == null ? 0 : this.getMinScaleDenominator().hashCode());
		return result;
	}
}