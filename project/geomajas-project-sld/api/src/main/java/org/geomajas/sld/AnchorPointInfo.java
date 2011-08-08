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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class AnchorPointInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
