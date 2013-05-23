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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="FunctionType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:ExpressionType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:expression" minOccurs="0" maxOccurs="unbounded"/>
 *       &lt;/xs:sequence>
 *       &lt;xs:attribute type="xs:string" use="required" name="name"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class FunctionTypeInfo extends ExpressionInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private List<ExpressionInfo> expressionList = new ArrayList<ExpressionInfo>();

	private String name;

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

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "FunctionTypeInfo(expressionList=" + this.getExpressionList() + ", name=" + this.getName() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof FunctionTypeInfo)) {
			return false;
		}
		final FunctionTypeInfo other = (FunctionTypeInfo) o;
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
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
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
		return other instanceof FunctionTypeInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getExpressionList() == null ? 0 : this.getExpressionList().hashCode());
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		return result;
	}
}