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
 * 
 A "Displacement" gives X and Y offset displacements to use for rendering a text label near a point.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Displacement">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:DisplacementX"/>
 *       &lt;xs:element ref="ns:DisplacementY"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class DisplacementInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private DisplacementXInfo displacementX;

	private DisplacementYInfo displacementY;

	/**
	 * Get the 'DisplacementX' element value.
	 * 
	 * @return value
	 */
	public DisplacementXInfo getDisplacementX() {
		return displacementX;
	}

	/**
	 * Set the 'DisplacementX' element value.
	 * 
	 * @param displacementX
	 */
	public void setDisplacementX(DisplacementXInfo displacementX) {
		this.displacementX = displacementX;
	}

	/**
	 * Get the 'DisplacementY' element value.
	 * 
	 * @return value
	 */
	public DisplacementYInfo getDisplacementY() {
		return displacementY;
	}

	/**
	 * Set the 'DisplacementY' element value.
	 * 
	 * @param displacementY
	 */
	public void setDisplacementY(DisplacementYInfo displacementY) {
		this.displacementY = displacementY;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "DisplacementInfo(displacementX=" + this.getDisplacementX() + ", displacementY="
				+ this.getDisplacementY() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof DisplacementInfo)) {
			return false;
		}
		final DisplacementInfo other = (DisplacementInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getDisplacementX() == null ? other.getDisplacementX() != null : !this.getDisplacementX().equals(
				(java.lang.Object) other.getDisplacementX())) {
			return false;
		}
		if (this.getDisplacementY() == null ? other.getDisplacementY() != null : !this.getDisplacementY().equals(
				(java.lang.Object) other.getDisplacementY())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof DisplacementInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getDisplacementX() == null ? 0 : this.getDisplacementX().hashCode());
		result = result * prime + (this.getDisplacementY() == null ? 0 : this.getDisplacementY().hashCode());
		return result;
	}
}