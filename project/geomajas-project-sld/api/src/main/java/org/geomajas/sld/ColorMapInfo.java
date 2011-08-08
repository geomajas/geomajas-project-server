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
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotations.Api;

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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ColorMapInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
