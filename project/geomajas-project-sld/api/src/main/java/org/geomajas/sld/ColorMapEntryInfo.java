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

import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ColorMapEntry">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="color"/>
 *     &lt;xs:attribute type="xs:double" name="opacity"/>
 *     &lt;xs:attribute type="xs:double" name="quantity"/>
 *     &lt;xs:attribute type="xs:string" name="label"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ColorMapEntryInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String color;

	private Double opacity;

	private Double quantity;

	private String label;

	/**
	 * Get the 'color' attribute value.
	 * 
	 * @return value
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Set the 'color' attribute value.
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * Get the 'opacity' attribute value.
	 * 
	 * @return value
	 */
	public Double getOpacity() {
		return opacity;
	}

	/**
	 * Set the 'opacity' attribute value.
	 * 
	 * @param opacity
	 */
	public void setOpacity(Double opacity) {
		this.opacity = opacity;
	}

	/**
	 * Get the 'quantity' attribute value.
	 * 
	 * @return value
	 */
	public Double getQuantity() {
		return quantity;
	}

	/**
	 * Set the 'quantity' attribute value.
	 * 
	 * @param quantity
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	/**
	 * Get the 'label' attribute value.
	 * 
	 * @return value
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the 'label' attribute value.
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}
