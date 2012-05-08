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
package org.geomajas.sld.filter;

import java.io.Serializable;
import org.geomajas.annotation.Api;
import org.geomajas.sld.expression.ExpressionInfo;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="UpperBoundaryType">
 *   &lt;xs:sequence>
 *     &lt;xs:element ref="ns:expression"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class UpperBoundaryTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private ExpressionInfo expression;

	/**
	 * Get the 'expression' element value.
	 * 
	 * @return value
	 */
	public ExpressionInfo getExpression() {
		return expression;
	}

	/**
	 * Set the 'expression' element value.
	 * 
	 * @param expression
	 */
	public void setExpression(ExpressionInfo expression) {
		this.expression = expression;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "UpperBoundaryTypeInfo(expression=" + this.getExpression() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof UpperBoundaryTypeInfo)) {
			return false;
		}
		final UpperBoundaryTypeInfo other = (UpperBoundaryTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getExpression() == null ? other.getExpression() != null : !this.getExpression().equals(
				(java.lang.Object) other.getExpression())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof UpperBoundaryTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getExpression() == null ? 0 : this.getExpression().hashCode());
		return result;
	}
}