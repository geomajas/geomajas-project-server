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
 A "LinePlacement" specifies how a text label should be rendered relative to a linear geometry.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld" 
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="LinePlacement">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:PerpendicularOffset" minOccurs="0"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class LinePlacementInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private PerpendicularOffsetInfo perpendicularOffset;

	/**
	 * Get the 'PerpendicularOffset' element value.
	 * 
	 * @return value
	 */
	public PerpendicularOffsetInfo getPerpendicularOffset() {
		return perpendicularOffset;
	}

	/**
	 * Set the 'PerpendicularOffset' element value.
	 * 
	 * @param perpendicularOffset
	 */
	public void setPerpendicularOffset(PerpendicularOffsetInfo perpendicularOffset) {
		this.perpendicularOffset = perpendicularOffset;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "LinePlacementInfo(perpendicularOffset=" + this.getPerpendicularOffset() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LinePlacementInfo)) {
			return false;
		}
		final LinePlacementInfo other = (LinePlacementInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getPerpendicularOffset() == null ? other.getPerpendicularOffset() != null : !this
				.getPerpendicularOffset().equals((java.lang.Object) other.getPerpendicularOffset())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof LinePlacementInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime
				+ (this.getPerpendicularOffset() == null ? 0 : this.getPerpendicularOffset().hashCode());
		return result;
	}
}