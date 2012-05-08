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
 A "Halo" fills an extended area outside the glyphs of a rendered text label to make the label easier to read over a
 * background.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Halo">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Radius" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Fill" minOccurs="0"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class HaloInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private RadiusInfo radius;

	private FillInfo fill;

	/**
	 * Get the 'Radius' element value.
	 * 
	 * @return value
	 */
	public RadiusInfo getRadius() {
		return radius;
	}

	/**
	 * Set the 'Radius' element value.
	 * 
	 * @param radius
	 */
	public void setRadius(RadiusInfo radius) {
		this.radius = radius;
	}

	/**
	 * Get the 'Fill' element value.
	 * 
	 * @return value
	 */
	public FillInfo getFill() {
		return fill;
	}

	/**
	 * Set the 'Fill' element value.
	 * 
	 * @param fill
	 */
	public void setFill(FillInfo fill) {
		this.fill = fill;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "HaloInfo(radius=" + this.getRadius() + ", fill=" + this.getFill() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof HaloInfo)) {
			return false;
		}
		final HaloInfo other = (HaloInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getRadius() == null ? other.getRadius() != null : !this.getRadius().equals(
				(java.lang.Object) other.getRadius())) {
			return false;
		}
		if (this.getFill() == null ? other.getFill() != null : !this.getFill().equals(
				(java.lang.Object) other.getFill())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof HaloInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getRadius() == null ? 0 : this.getRadius().hashCode());
		result = result * prime + (this.getFill() == null ? 0 : this.getFill().hashCode());
		return result;
	}
}