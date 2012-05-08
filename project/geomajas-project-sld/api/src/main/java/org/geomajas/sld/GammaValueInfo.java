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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:double" name="GammaValue"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class GammaValueInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private Double gammaValue;

	/**
	 * Get the 'GammaValue' element value.
	 * 
	 * @return value
	 */
	public Double getGammaValue() {
		return gammaValue;
	}

	/**
	 * Set the 'GammaValue' element value.
	 * 
	 * @param gammaValue
	 */
	public void setGammaValue(Double gammaValue) {
		this.gammaValue = gammaValue;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "GammaValueInfo(gammaValue=" + this.getGammaValue() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof GammaValueInfo)) {
			return false;
		}
		final GammaValueInfo other = (GammaValueInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getGammaValue() == null ? other.getGammaValue() != null : !this.getGammaValue().equals(
				(java.lang.Object) other.getGammaValue())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof GammaValueInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getGammaValue() == null ? 0 : this.getGammaValue().hashCode());
		return result;
	}
}