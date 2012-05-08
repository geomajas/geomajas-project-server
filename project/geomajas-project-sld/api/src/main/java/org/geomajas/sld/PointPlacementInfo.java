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
 A "PointPlacement" specifies how a text label should be rendered relative to a geometric point.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="PointPlacement">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:AnchorPoint" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Displacement" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Rotation" minOccurs="0"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class PointPlacementInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private AnchorPointInfo anchorPoint;

	private DisplacementInfo displacement;

	private RotationInfo rotation;

	/**
	 * Get the 'AnchorPoint' element value.
	 * 
	 * @return value
	 */
	public AnchorPointInfo getAnchorPoint() {
		return anchorPoint;
	}

	/**
	 * Set the 'AnchorPoint' element value.
	 * 
	 * @param anchorPoint
	 */
	public void setAnchorPoint(AnchorPointInfo anchorPoint) {
		this.anchorPoint = anchorPoint;
	}

	/**
	 * Get the 'Displacement' element value.
	 * 
	 * @return value
	 */
	public DisplacementInfo getDisplacement() {
		return displacement;
	}

	/**
	 * Set the 'Displacement' element value.
	 * 
	 * @param displacement
	 */
	public void setDisplacement(DisplacementInfo displacement) {
		this.displacement = displacement;
	}

	/**
	 * Get the 'Rotation' element value.
	 * 
	 * @return value
	 */
	public RotationInfo getRotation() {
		return rotation;
	}

	/**
	 * Set the 'Rotation' element value.
	 * 
	 * @param rotation
	 */
	public void setRotation(RotationInfo rotation) {
		this.rotation = rotation;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PointPlacementInfo(anchorPoint=" + this.getAnchorPoint() + ", displacement=" + this.getDisplacement()
				+ ", rotation=" + this.getRotation() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PointPlacementInfo)) {
			return false;
		}
		final PointPlacementInfo other = (PointPlacementInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getAnchorPoint() == null ? other.getAnchorPoint() != null : !this.getAnchorPoint().equals(
				(java.lang.Object) other.getAnchorPoint())) {
			return false;
		}
		if (this.getDisplacement() == null ? other.getDisplacement() != null : !this.getDisplacement().equals(
				(java.lang.Object) other.getDisplacement())) {
			return false;
		}
		if (this.getRotation() == null ? other.getRotation() != null : !this.getRotation().equals(
				(java.lang.Object) other.getRotation())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof PointPlacementInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getAnchorPoint() == null ? 0 : this.getAnchorPoint().hashCode());
		result = result * prime + (this.getDisplacement() == null ? 0 : this.getDisplacement().hashCode());
		result = result * prime + (this.getRotation() == null ? 0 : this.getRotation().hashCode());
		return result;
	}
}