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

import org.geomajas.annotation.Api;

/**
 * 
 Coordinates can be included in a single string, but there is no facility for validating string content. The value of
 * the 'cs' attribute is the separator for coordinate values, and the value of the 'ts' attribute gives the tuple
 * separator (a single space by default); the default values may be changed to reflect local usage.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/gml"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="CoordinatesType">
 *   &lt;xs:simpleContent>
 *     &lt;xs:extension base="xs:string">
 *       &lt;xs:attribute type="xs:string" use="optional" default="." name="decimal"/>
 *       &lt;xs:attribute type="xs:string" use="optional" default="," name="cs"/>
 *       &lt;xs:attribute type="xs:string" use="optional" default=" " name="ts"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:simpleContent>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class CoordinatesTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String string;

	private String decimal;

	private String cs;

	private String ts;

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
	 * Get the 'decimal' attribute value.
	 * 
	 * @return value
	 */
	public String getDecimal() {
		return decimal;
	}

	/**
	 * Set the 'decimal' attribute value.
	 * 
	 * @param decimal
	 */
	public void setDecimal(String decimal) {
		this.decimal = decimal;
	}

	/**
	 * Get the 'cs' attribute value.
	 * 
	 * @return value
	 */
	public String getCs() {
		return cs;
	}

	/**
	 * Set the 'cs' attribute value.
	 * 
	 * @param cs
	 */
	public void setCs(String cs) {
		this.cs = cs;
	}

	/**
	 * Get the 'ts' attribute value.
	 * 
	 * @return value
	 */
	public String getTs() {
		return ts;
	}

	/**
	 * Set the 'ts' attribute value.
	 * 
	 * @param ts
	 */
	public void setTs(String ts) {
		this.ts = ts;
	}
}
