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
 The "LabelPlacement" specifies where and how a text label should be rendered relative to a geometry. The present
 * mechanism is poorly aligned with CSS/SVG.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld" 
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="LabelPlacement">
 *   &lt;xs:complexType>
 *     &lt;xs:choice>
 *       &lt;xs:element ref="ns:PointPlacement"/>
 *       &lt;xs:element ref="ns:LinePlacement"/>
 *     &lt;/xs:choice>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class LabelPlacementInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private int labelPlacementSelect = -1;

	private static final int POINT_PLACEMENT_CHOICE = 0;

	private static final int LINE_PLACEMENT_CHOICE = 1;

	private PointPlacementInfo pointPlacement;

	private LinePlacementInfo linePlacement;

	private void setLabelPlacementSelect(int choice) {
		if (labelPlacementSelect == -1) {
			labelPlacementSelect = choice;
		} else if (labelPlacementSelect != choice) {
			throw new IllegalStateException("Need to call clearLabelPlacementSelect() before changing existing choice");
		}
	}

	/**
	 * Clear the choice selection.
	 */
	public void clearLabelPlacementSelect() {
		labelPlacementSelect = -1;
	}

	/**
	 * Check if PointPlacement is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifPointPlacement() {
		return labelPlacementSelect == POINT_PLACEMENT_CHOICE;
	}

	/**
	 * Get the 'PointPlacement' element value.
	 * 
	 * @return value
	 */
	public PointPlacementInfo getPointPlacement() {
		return pointPlacement;
	}

	/**
	 * Set the 'PointPlacement' element value.
	 * 
	 * @param pointPlacement
	 */
	public void setPointPlacement(PointPlacementInfo pointPlacement) {
		setLabelPlacementSelect(POINT_PLACEMENT_CHOICE);
		this.pointPlacement = pointPlacement;
	}

	/**
	 * Check if LinePlacement is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifLinePlacement() {
		return labelPlacementSelect == LINE_PLACEMENT_CHOICE;
	}

	/**
	 * Get the 'LinePlacement' element value.
	 * 
	 * @return value
	 */
	public LinePlacementInfo getLinePlacement() {
		return linePlacement;
	}

	/**
	 * Set the 'LinePlacement' element value.
	 * 
	 * @param linePlacement
	 */
	public void setLinePlacement(LinePlacementInfo linePlacement) {
		setLabelPlacementSelect(LINE_PLACEMENT_CHOICE);
		this.linePlacement = linePlacement;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "LabelPlacementInfo(labelPlacementSelect=" + this.labelPlacementSelect + ", pointPlacement="
				+ this.getPointPlacement() + ", linePlacement=" + this.getLinePlacement() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LabelPlacementInfo)) {
			return false;
		}
		final LabelPlacementInfo other = (LabelPlacementInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.labelPlacementSelect != other.labelPlacementSelect) {
			return false;
		}
		if (this.getPointPlacement() == null ? other.getPointPlacement() != null : !this.getPointPlacement().equals(
				(java.lang.Object) other.getPointPlacement())) {
			return false;
		}
		if (this.getLinePlacement() == null ? other.getLinePlacement() != null : !this.getLinePlacement().equals(
				(java.lang.Object) other.getLinePlacement())) {
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
		return other instanceof LabelPlacementInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + this.labelPlacementSelect;
		result = result * prime + (this.getPointPlacement() == null ? 0 : this.getPointPlacement().hashCode());
		result = result * prime + (this.getLinePlacement() == null ? 0 : this.getLinePlacement().hashCode());
		return result;
	}
}