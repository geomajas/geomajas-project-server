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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="VendorOption">
 *   &lt;xs:complexType mixed="true">
 *     &lt;xs:simpleContent>
 *       &lt;xs:extension base="xs:string">
 *         &lt;xs:attribute type="xs:string" name="name"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:simpleContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class VendorOptionInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String string;

	private String name;

	/**
	 * Get the extension value.
	 * 
	 * @return value
	 */
	public String getString() {
		return string;
	}

	/**
	 * Set the extension value.
	 * 
	 * @param string
	 */
	public void setString(String string) {
		this.string = string;
	}

	/**
	 * Get the 'name' attribute value.
	 * 
	 * @return value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the 'name' attribute value.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
