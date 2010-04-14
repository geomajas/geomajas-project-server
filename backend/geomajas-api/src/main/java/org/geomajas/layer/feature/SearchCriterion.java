/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.layer.feature;

import org.geomajas.global.Api;

import java.io.Serializable;

/**
 * A single criterion DTO that can be used to set up filters and search for features by their alpha numerical
 * attributes.
 * 
 * @author Pieter De Graef
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
		if ("contains".equals(operator)) {
			return "(" + attributeName + " like '%" + value + "%')";
		}
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