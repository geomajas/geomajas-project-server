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
package org.geomajas.sld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;
import org.geomajas.sld.expression.ExpressionInfo;

/**
 * 
 The "ParameterValueType" uses WFS-Filter expressions to give values for SLD graphic parameters. A "mixed"
 * element-content model is used with textual substitution for values.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:ns1="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" mixed="true" name="ParameterValueType">
 *   &lt;xs:sequence>
 *     &lt;xs:element ref="ns:expression" minOccurs="0" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ParameterValueTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private List<ExpressionInfo> expressionList = new ArrayList<ExpressionInfo>();

	private String value;

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

	/**
	 * Get the textual expression value.
	 * 
	 * @return the textual value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the textual expression value.
	 * 
	 * @param value the value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ParameterValueTypeInfo(expressionList=" + this.getExpressionList() + ", value=" + this.getValue() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ParameterValueTypeInfo)) {
			return false;
		}
		final ParameterValueTypeInfo other = (ParameterValueTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getExpressionList() == null ? other.getExpressionList() != null : !this.getExpressionList().equals(
				(java.lang.Object) other.getExpressionList())) {
			return false;
		}
		if (this.getValue() == null ? other.getValue() != null : !this.getValue().equals(
				(java.lang.Object) other.getValue())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ParameterValueTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getExpressionList() == null ? 0 : this.getExpressionList().hashCode());
		result = result * prime + (this.getValue() == null ? 0 : this.getValue().hashCode());
		return result;
	}
}