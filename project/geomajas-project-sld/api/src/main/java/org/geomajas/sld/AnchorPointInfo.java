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
 An "AnchorPoint" identifies the location inside of a text label to use an an 'anchor' for positioning it relative to
 * a point geometry.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="AnchorPoint">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:AnchorPointX"/>
 *       &lt;xs:element ref="ns:AnchorPointY"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class AnchorPointInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private AnchorPointXInfo anchorPointX;

	private AnchorPointYInfo anchorPointY;

	/**
	 * Get the 'AnchorPointX' element value.
	 * 
	 * @return value
	 */
	public AnchorPointXInfo getAnchorPointX() {
		return anchorPointX;
	}

	/**
	 * Set the 'AnchorPointX' element value.
	 * 
	 * @param anchorPointX
	 */
	public void setAnchorPointX(AnchorPointXInfo anchorPointX) {
		this.anchorPointX = anchorPointX;
	}

	/**
	 * Get the 'AnchorPointY' element value.
	 * 
	 * @return value
	 */
	public AnchorPointYInfo getAnchorPointY() {
		return anchorPointY;
	}

	/**
	 * Set the 'AnchorPointY' element value.
	 * 
	 * @param anchorPointY
	 */
	public void setAnchorPointY(AnchorPointYInfo anchorPointY) {
		this.anchorPointY = anchorPointY;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "AnchorPointInfo(anchorPointX=" + this.getAnchorPointX() + ", anchorPointY=" + this.getAnchorPointY()
				+ ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AnchorPointInfo)) {
			return false;
		}
		final AnchorPointInfo other = (AnchorPointInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getAnchorPointX() == null ? other.getAnchorPointX() != null : !this.getAnchorPointX().equals(
				(java.lang.Object) other.getAnchorPointX())) {
			return false;
		}
		if (this.getAnchorPointY() == null ? other.getAnchorPointY() != null : !this.getAnchorPointY().equals(
				(java.lang.Object) other.getAnchorPointY())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof AnchorPointInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getAnchorPointX() == null ? 0 : this.getAnchorPointX().hashCode());
		result = result * prime + (this.getAnchorPointY() == null ? 0 : this.getAnchorPointY().hashCode());
		return result;
	}
}