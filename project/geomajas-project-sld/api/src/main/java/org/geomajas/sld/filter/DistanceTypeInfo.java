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
package org.geomajas.sld.filter;

import java.io.Serializable;

import org.geomajas.annotations.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" mixed="true" name="DistanceType">
 *   &lt;xs:attribute type="xs:string" use="required" name="units"/>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class DistanceTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String units;
	
	private String value;

	/**
	 * Get the 'units' attribute value.
	 * 
	 * @return value
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * Set the 'units' attribute value.
	 * 
	 * @param units
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	
	public String getValue() {
		return value;
	}

	
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
