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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class PropertyIsNullTypeInfo extends ComparisonOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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
}
