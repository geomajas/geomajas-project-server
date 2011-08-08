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
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="Value"/>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ValueInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String value;

	/**
	 * Get the 'Value' element value.
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the 'Value' element value.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
