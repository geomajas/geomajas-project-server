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
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;

/**
 * 
 A "ColorMap" defines either the colors of a pallet-type raster source or the mapping of numeric pixel values to
 * colors.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ColorMap">
 *   &lt;xs:complexType>
 *     &lt;xs:choice>
 *       &lt;xs:element ref="ns:ColorMapEntry" minOccurs="0" maxOccurs="unbounded"/>
 *     &lt;/xs:choice>
 *     &lt;xs:attribute type="xs:string" name="type"/>
 *     &lt;xs:attribute type="xs:boolean" name="extended"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ColorMapInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private List<ColorMapEntryInfo> colorMapEntryList = new ArrayList<ColorMapEntryInfo>();

	private String type;

	private Boolean extended;

	/**
	 * Get the list of 'ColorMapEntry' element items.
	 * 
	 * @return list
	 */
	public List<ColorMapEntryInfo> getColorMapEntryList() {
		return colorMapEntryList;
	}

	/**
	 * Set the list of 'ColorMapEntry' element items.
	 * 
	 * @param list
	 */
	public void setColorMapEntryList(List<ColorMapEntryInfo> list) {
		colorMapEntryList = list;
	}

	/**
	 * Get the 'type' attribute value.
	 * 
	 * @return value
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the 'type' attribute value.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the 'extended' attribute value.
	 * 
	 * @return value
	 */
	public Boolean getExtended() {
		return extended;
	}

	/**
	 * Set the 'extended' attribute value.
	 * 
	 * @param extended
	 */
	public void setExtended(Boolean extended) {
		this.extended = extended;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ColorMapInfo(colorMapEntryList=" + this.getColorMapEntryList() + ", type=" + this.getType()
				+ ", extended=" + this.getExtended() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ColorMapInfo)) {
			return false;
		}
		final ColorMapInfo other = (ColorMapInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getColorMapEntryList() == null ? other.getColorMapEntryList() != null : !this.getColorMapEntryList()
				.equals((java.lang.Object) other.getColorMapEntryList())) {
			return false;
		}
		if (this.getType() == null ? other.getType() != null : !this.getType().equals(
				(java.lang.Object) other.getType())) {
			return false;
		}
		if (this.getExtended() == null ? other.getExtended() != null : !this.getExtended().equals(
				(java.lang.Object) other.getExtended())) {
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
		return other instanceof ColorMapInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getColorMapEntryList() == null ? 0 : this.getColorMapEntryList().hashCode());
		result = result * prime + (this.getType() == null ? 0 : this.getType().hashCode());
		result = result * prime + (this.getExtended() == null ? 0 : this.getExtended().hashCode());
		return result;
	}
}