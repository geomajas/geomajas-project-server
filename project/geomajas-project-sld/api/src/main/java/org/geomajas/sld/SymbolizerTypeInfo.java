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
 A "SymbolizerType" is an abstract type for encoding the graphical properties used to portray geographic information.
 * Concrete symbol types are derived from this base type.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" abstract="true" name="SymbolizerType">
 *   &lt;xs:attribute type="xs:string" name="uom"/>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public abstract class SymbolizerTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String uom;

	/**
	 * Get the 'uom' attribute value.
	 * 
	 * @return value
	 */
	public String getUom() {
		return uom;
	}

	/**
	 * Set the 'uom' attribute value.
	 * 
	 * @param uom
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}
}
