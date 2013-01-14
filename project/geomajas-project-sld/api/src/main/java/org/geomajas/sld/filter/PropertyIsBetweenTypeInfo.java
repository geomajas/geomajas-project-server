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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="PropertyIsBetweenType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:ComparisonOpsType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:expression"/>
 *         &lt;xs:element type="ns:LowerBoundaryType" name="LowerBoundary"/>
 *         &lt;xs:element type="ns:UpperBoundaryType" name="UpperBoundary"/>
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
public class PropertyIsBetweenTypeInfo extends ComparisonOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private ExpressionInfo expression;

	private LowerBoundaryTypeInfo lowerBoundary;

	private UpperBoundaryTypeInfo upperBoundary;

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

	/**
	 * Get the 'LowerBoundary' element value.
	 * 
	 * @return value
	 */
	public LowerBoundaryTypeInfo getLowerBoundary() {
		return lowerBoundary;
	}

	/**
	 * Set the 'LowerBoundary' element value.
	 * 
	 * @param lowerBoundary
	 */
	public void setLowerBoundary(LowerBoundaryTypeInfo lowerBoundary) {
		this.lowerBoundary = lowerBoundary;
	}

	/**
	 * Get the 'UpperBoundary' element value.
	 * 
	 * @return value
	 */
	public UpperBoundaryTypeInfo getUpperBoundary() {
		return upperBoundary;
	}

	/**
	 * Set the 'UpperBoundary' element value.
	 * 
	 * @param upperBoundary
	 */
	public void setUpperBoundary(UpperBoundaryTypeInfo upperBoundary) {
		this.upperBoundary = upperBoundary;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PropertyIsBetweenTypeInfo(expression=" + this.getExpression() + ", lowerBoundary="
				+ this.getLowerBoundary() + ", upperBoundary=" + this.getUpperBoundary() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PropertyIsBetweenTypeInfo)) {
			return false;
		}
		final PropertyIsBetweenTypeInfo other = (PropertyIsBetweenTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.getExpression() == null ? other.getExpression() != null : !this.getExpression().equals(
				(java.lang.Object) other.getExpression())) {
			return false;
		}
		if (this.getLowerBoundary() == null ? other.getLowerBoundary() != null : !this.getLowerBoundary().equals(
				(java.lang.Object) other.getLowerBoundary())) {
			return false;
		}
		if (this.getUpperBoundary() == null ? other.getUpperBoundary() != null : !this.getUpperBoundary().equals(
				(java.lang.Object) other.getUpperBoundary())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof PropertyIsBetweenTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getExpression() == null ? 0 : this.getExpression().hashCode());
		result = result * prime + (this.getLowerBoundary() == null ? 0 : this.getLowerBoundary().hashCode());
		result = result * prime + (this.getUpperBoundary() == null ? 0 : this.getUpperBoundary().hashCode());
		return result;
	}
}