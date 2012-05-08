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
import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.expression.PropertyNameInfo;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="PropertyIsNullType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:ComparisonOpsType">
 *       &lt;xs:choice>
 *         &lt;xs:element ref="ns:PropertyName"/>
 *         &lt;xs:element ref="ns:Literal"/>
 *       &lt;/xs:choice>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class PropertyIsNullTypeInfo extends ComparisonOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private int choiceSelect = -1;

	private static final int PROPERTY_NAME_CHOICE = 0;

	private static final int LITERAL_CHOICE = 1;

	private PropertyNameInfo propertyName;

	private LiteralTypeInfo literal;

	private void setChoiceSelect(int choice) {
		if (choiceSelect == -1) {
			choiceSelect = choice;
		} else if (choiceSelect != choice) {
			throw new IllegalStateException("Need to call clearChoiceSelect() before changing existing choice");
		}
	}

	/**
	 * Clear the choice selection.
	 */
	public void clearChoiceSelect() {
		choiceSelect = -1;
	}

	/**
	 * Check if PropertyName is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifPropertyName() {
		return choiceSelect == PROPERTY_NAME_CHOICE;
	}

	/**
	 * Get the 'PropertyName' element value.
	 * 
	 * @return value
	 */
	public PropertyNameInfo getPropertyName() {
		return propertyName;
	}

	/**
	 * Set the 'PropertyName' element value.
	 * 
	 * @param propertyName
	 */
	public void setPropertyName(PropertyNameInfo propertyName) {
		setChoiceSelect(PROPERTY_NAME_CHOICE);
		this.propertyName = propertyName;
	}

	/**
	 * Check if Literal is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifLiteral() {
		return choiceSelect == LITERAL_CHOICE;
	}

	/**
	 * Get the 'Literal' element value.
	 * 
	 * @return value
	 */
	public LiteralTypeInfo getLiteral() {
		return literal;
	}

	/**
	 * Set the 'Literal' element value.
	 * 
	 * @param literal
	 */
	public void setLiteral(LiteralTypeInfo literal) {
		setChoiceSelect(LITERAL_CHOICE);
		this.literal = literal;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PropertyIsNullTypeInfo(choiceSelect=" + this.choiceSelect + ", propertyName=" + this.getPropertyName()
				+ ", literal=" + this.getLiteral() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PropertyIsNullTypeInfo)) {
			return false;
		}
		final PropertyIsNullTypeInfo other = (PropertyIsNullTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.choiceSelect != other.choiceSelect) {
			return false;
		}
		if (this.getPropertyName() == null ? other.getPropertyName() != null : !this.getPropertyName().equals(
				(java.lang.Object) other.getPropertyName())) {
			return false;
		}
		if (this.getLiteral() == null ? other.getLiteral() != null : !this.getLiteral().equals(
				(java.lang.Object) other.getLiteral())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof PropertyIsNullTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + this.choiceSelect;
		result = result * prime + (this.getPropertyName() == null ? 0 : this.getPropertyName().hashCode());
		result = result * prime + (this.getLiteral() == null ? 0 : this.getLiteral().hashCode());
		return result;
	}
}