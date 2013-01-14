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
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="BinaryOperatorType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:ExpressionType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:expression" minOccurs="2" maxOccurs="2"/>
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
public class BinaryOperatorTypeInfo extends ExpressionInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private List<ExpressionInfo> expressionList = new ArrayList<ExpressionInfo>();

	/**
	 * Get the list of 'expression' element items.
	 * 
	 * @return list
	 */
	public List<ExpressionInfo> getExpressionList() {
		return expressionList;
	}

	/**
	 * Set the list of 'expression' element items.
	 * 
	 * @param list
	 */
	public void setExpressionList(List<ExpressionInfo> list) {
		expressionList = list;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "BinaryOperatorTypeInfo(expressionList=" + this.getExpressionList() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BinaryOperatorTypeInfo)) {
			return false;
		}
		final BinaryOperatorTypeInfo other = (BinaryOperatorTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.getExpressionList() == null ? other.getExpressionList() != null : !this.getExpressionList().equals(
				(java.lang.Object) other.getExpressionList())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof BinaryOperatorTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getExpressionList() == null ? 0 : this.getExpressionList().hashCode());
		return result;
	}
}