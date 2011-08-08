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
package org.geomajas.sld.filter;

import java.io.Serializable;

import org.geomajas.annotations.Api;
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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class PropertyIsLikeTypeInfo extends ComparisonOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
