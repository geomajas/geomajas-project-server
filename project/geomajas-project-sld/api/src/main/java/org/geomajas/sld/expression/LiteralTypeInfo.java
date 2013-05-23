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
package org.geomajas.sld.expression;

import java.io.Serializable;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="LiteralType">
 *   &lt;xs:complexContent mixed="true">
 *     &lt;xs:extension base="ns:ExpressionType">
 *       &lt;xs:sequence>
 *         &lt;xs:any minOccurs="0"/>
 *       &lt;/xs:sequence>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class LiteralTypeInfo extends ExpressionInfo implements Serializable {

	private static final long serialVersionUID = 100;

	/**
	 * Construct an empty literal type (GWT).
	 */
	public LiteralTypeInfo() {
	}

	/**
	 * Construct a literal type with the specified literal value.
	 * 
	 * @param value
	 */
	public LiteralTypeInfo(String value) {
		setValue(value);
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "LiteralTypeInfo()";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LiteralTypeInfo)) {
			return false;
		}
		final LiteralTypeInfo other = (LiteralTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
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
		return other instanceof LiteralTypeInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		return result;
	}
}