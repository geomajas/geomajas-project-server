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
import org.geomajas.sld.expression.LiteralTypeInfo;
import org.geomajas.sld.expression.PropertyNameInfo;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="PropertyIsLikeType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:ComparisonOpsType">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:PropertyName"/>
 *         &lt;xs:element ref="ns:Literal"/>
 *       &lt;/xs:sequence>
 *       &lt;xs:attribute type="xs:string" use="required" name="wildCard"/>
 *       &lt;xs:attribute type="xs:string" use="required" name="singleChar"/>
 *       &lt;xs:attribute type="xs:string" use="required" name="escape"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class PropertyIsLikeTypeInfo extends ComparisonOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private PropertyNameInfo propertyName;

	private LiteralTypeInfo literal;

	private String wildCard;

	private String singleChar;

	private String escape;

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
		this.propertyName = propertyName;
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
		this.literal = literal;
	}

	/**
	 * Get the 'wildCard' attribute value.
	 * 
	 * @return value
	 */
	public String getWildCard() {
		return wildCard;
	}

	/**
	 * Set the 'wildCard' attribute value.
	 * 
	 * @param wildCard
	 */
	public void setWildCard(String wildCard) {
		this.wildCard = wildCard;
	}

	/**
	 * Get the 'singleChar' attribute value.
	 * 
	 * @return value
	 */
	public String getSingleChar() {
		return singleChar;
	}

	/**
	 * Set the 'singleChar' attribute value.
	 * 
	 * @param singleChar
	 */
	public void setSingleChar(String singleChar) {
		this.singleChar = singleChar;
	}

	/**
	 * Get the 'escape' attribute value.
	 * 
	 * @return value
	 */
	public String getEscape() {
		return escape;
	}

	/**
	 * Set the 'escape' attribute value.
	 * 
	 * @param escape
	 */
	public void setEscape(String escape) {
		this.escape = escape;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PropertyIsLikeTypeInfo(propertyName=" + this.getPropertyName() + ", literal=" + this.getLiteral()
				+ ", wildCard=" + this.getWildCard() + ", singleChar=" + this.getSingleChar() + ", escape="
				+ this.getEscape() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PropertyIsLikeTypeInfo)) {
			return false;
		}
		final PropertyIsLikeTypeInfo other = (PropertyIsLikeTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
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
		if (this.getWildCard() == null ? other.getWildCard() != null : !this.getWildCard().equals(
				(java.lang.Object) other.getWildCard())) {
			return false;
		}
		if (this.getSingleChar() == null ? other.getSingleChar() != null : !this.getSingleChar().equals(
				(java.lang.Object) other.getSingleChar())) {
			return false;
		}
		if (this.getEscape() == null ? other.getEscape() != null : !this.getEscape().equals(
				(java.lang.Object) other.getEscape())) {
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
		return other instanceof PropertyIsLikeTypeInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getPropertyName() == null ? 0 : this.getPropertyName().hashCode());
		result = result * prime + (this.getLiteral() == null ? 0 : this.getLiteral().hashCode());
		result = result * prime + (this.getWildCard() == null ? 0 : this.getWildCard().hashCode());
		result = result * prime + (this.getSingleChar() == null ? 0 : this.getSingleChar().hashCode());
		result = result * prime + (this.getEscape() == null ? 0 : this.getEscape().hashCode());
		return result;
	}
}