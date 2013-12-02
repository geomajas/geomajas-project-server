/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.feature;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * A single criterion DTO that can be used to set up filters and search for features by their alpha numerical
 * attributes.
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SearchCriterion implements Serializable {

	private static final long serialVersionUID = 151L;

	private String attributeName;

	private String operator;

	private String value;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * Default constructor - does nothing.
	 */
	public SearchCriterion() {
	}

	/**
	 * Constructor that initializes all fields with their values.
	 * 
	 * @param attributeName
	 *            Set the name of the alpha numerical attribute on which this criterion operates. This attribute name is
	 *            one of the attributes from the vector layer configuration.
	 * @param operator
	 *            The operator used in the criterion such as '=', '>', ...
	 * @param value
	 *            The value for this criterion as a formatted string.
	 */
	public SearchCriterion(String attributeName, String operator, String value) {
		this.attributeName = attributeName;
		this.operator = operator;
		this.value = value;
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Convert to human readable form.
	 * 
	 * @return human readable form
	 */
	public String toString() {
		return "(" + attributeName + " " + operator + " " + value + ")";
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	/**
	 * Return the operator used in the criterion such as '=', '>', ...
	 * 
	 * @return operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Set the operator used in the criterion such as '=', '>', ...
	 * 
	 * @param operator
	 *            The criterion operator.
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Get the name of the alpha numerical attribute on which this criterion operates. This attribute name is one of the
	 * attributes from the vector layer configuration.
	 * 
	 * @return attribute name
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * Set the name of the alpha numerical attribute on which this criterion operates. This attribute name is one of the
	 * attributes from the vector layer configuration.
	 * 
	 * @param attributeName
	 *            The name of the alpha numerical attribute.
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * Get the value for this criterion as a formatted string.
	 * 
	 * @return value for comparison
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the value for this criterion as a formatted string.
	 * 
	 * @param value
	 *            The value for the attribute.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}