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
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ColorMapEntryInfo implements Serializable {

	private static final long serialVersionUID = 100;

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

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ColorMapEntryInfo(color=" + this.getColor() + ", opacity=" + this.getOpacity() + ", quantity="
				+ this.getQuantity() + ", label=" + this.getLabel() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ColorMapEntryInfo)) {
			return false;
		}
		final ColorMapEntryInfo other = (ColorMapEntryInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getColor() == null ? other.getColor() != null : !this.getColor().equals(
				(java.lang.Object) other.getColor())) {
			return false;
		}
		if (this.getOpacity() == null ? other.getOpacity() != null : !this.getOpacity().equals(
				(java.lang.Object) other.getOpacity())) {
			return false;
		}
		if (this.getQuantity() == null ? other.getQuantity() != null : !this.getQuantity().equals(
				(java.lang.Object) other.getQuantity())) {
			return false;
		}
		if (this.getLabel() == null ? other.getLabel() != null : !this.getLabel().equals(
				(java.lang.Object) other.getLabel())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ColorMapEntryInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getColor() == null ? 0 : this.getColor().hashCode());
		result = result * prime + (this.getOpacity() == null ? 0 : this.getOpacity().hashCode());
		result = result * prime + (this.getQuantity() == null ? 0 : this.getQuantity().hashCode());
		result = result * prime + (this.getLabel() == null ? 0 : this.getLabel().hashCode());
		return result;
	}
}