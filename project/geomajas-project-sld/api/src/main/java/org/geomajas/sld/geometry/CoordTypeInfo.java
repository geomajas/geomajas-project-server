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
package org.geomajas.sld.geometry;

import java.io.Serializable;
import java.math.BigDecimal;

import org.geomajas.annotation.Api;

/**
 * 
 Represents a coordinate tuple in one, two, or three dimensions.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="CoordType">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:decimal" name="X"/>
 *     &lt;xs:element type="xs:decimal" name="Y" minOccurs="0"/>
 *     &lt;xs:element type="xs:decimal" name="Z" minOccurs="0"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class CoordTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private BigDecimal x;

	private BigDecimal y;

	private BigDecimal z;

	/**
	 * Get the 'X' element value.
	 * 
	 * @return value
	 */
	public BigDecimal getX() {
		return x;
	}

	/**
	 * Set the 'X' element value.
	 * 
	 * @param x
	 */
	public void setX(BigDecimal x) {
		this.x = x;
	}

	/**
	 * Get the 'Y' element value.
	 * 
	 * @return value
	 */
	public BigDecimal getY() {
		return y;
	}

	/**
	 * Set the 'Y' element value.
	 * 
	 * @param y
	 */
	public void setY(BigDecimal y) {
		this.y = y;
	}

	/**
	 * Get the 'Z' element value.
	 * 
	 * @return value
	 */
	public BigDecimal getZ() {
		return z;
	}

	/**
	 * Set the 'Z' element value.
	 * 
	 * @param z
	 */
	public void setZ(BigDecimal z) {
		this.z = z;
	}
}
