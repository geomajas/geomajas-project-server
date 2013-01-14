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
import org.geomajas.annotation.Api;

/**
 * 
 A "CssParameter" refers to an SVG/CSS graphical-formatting parameter. The parameter is identified using the "name"
 * attribute and the content of the element gives the SVG/CSS-coded value.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="CssParameter">
 *   &lt;xs:complexType mixed="true">
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="ns:ParameterValueType">
 *         &lt;xs:attribute type="xs:string" use="required" name="name"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class CssParameterInfo extends ParameterValueTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	/**
	 * Construct an empty css parameter (GWT).
	 */
	public CssParameterInfo() {
	}

	/**
	 * Construct a css parameter with the specified name and value.
	 * 
	 * @param name the name
	 * @param value the value
	 */
	public CssParameterInfo(String name, String value) {
		setName(name);
		setValue(value);
	}

	private String name;

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

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "CssParameterInfo(name=" + this.getName() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CssParameterInfo)) {
			return false;
		}
		final CssParameterInfo other = (CssParameterInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof CssParameterInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		return result;
	}
}