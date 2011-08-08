/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld;

import java.io.Serializable;

import org.geomajas.annotations.Api;

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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class LabelPlacementInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
