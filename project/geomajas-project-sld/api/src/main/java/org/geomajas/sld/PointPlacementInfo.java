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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class PointPlacementInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
