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
package org.geomajas.sld.editor.common.client.model;

import org.geomajas.sld.filter.FilterTypeInfo;

/**
 * Model for SLD rule filter.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface FilterModel {

	/**
	 * Supported operator types.
	 * 
	 * @author Jan De Moerloose
	 *
	 */
	public enum OperatorType {
		PROPERTY_IS_EQUAL_TO("PROPERTY_IS_EQUAL_TO"), 
		PROPERTY_IS_GREATER_THAN("PROPERTY_IS_GREATER_THAN"),
		PROPERTY_IS_GREATER_THAN_OR_EQUAL("PROPERTY_IS_GREATER_THAN_OR_EQUAL"),
		PROPERTY_IS_LESS_THAN("PROPERTY_IS_LESS_THAN"),
		PROPERTY_IS_LESS_THAN_OR_EQUAL("PROPERTY_IS_LESS_THAN_OR_EQUAL"),
		PROPERTY_IS_NOT_EQUAL_TO("PROPERTY_IS_NOT_EQUAL_TO"),
		PROPERTY_IS_BETWEEN("PROPERTY_IS_BETWEEN"),
		PROPERTY_IS_LIKE("PROPERTY_IS_LIKE"),
		PROPERTY_IS_NULL("PROPERTY_IS_NULL"),
		PROPERTY_IS_NOT_BETWEEN("PROPERTY_IS_NOT_BETWEEN"),
		PROPERTY_IS_NOT_LIKE("PROPERTY_IS_NOT_LIKE"),
		PROPERTY_IS_NOT_NULL("PROPERTY_IS_NOT_NULL");

		private final String value;

		OperatorType(String v) {
			value = v;
		}

		public String value() {
			return value;
		}

		public static OperatorType fromValue(String v) {
			for (OperatorType c : OperatorType.values()) {
				if (c.value.equals(v)) {
					return c;
				}
			}
			throw new IllegalArgumentException(v.toString());
		}
	}

	/**
	 * @author An Buyle
	 * @author Jan De Moerloose
	 * 
	 */
	public enum FilterModelState {
		INCOMPLETE, // missing some data
		COMPLETE, // all data complete (dirty flag is kept at SldModel level)
		UNSUPPORTED // not a supported filter
	}

	FilterTypeInfo getFilterTypeInfo();

	OperatorType getOperatorType();

	String getPropertyName();

	void setPropertyName(String propertyName);

	String getPropertyValue();

	void setPropertyValue(String propertyValue);

	String getLowerValue();

	void setLowerValue(String lowerValue);

	String getUpperValue();

	void setUpperValue(String upperValue);

	void setOperatorType(OperatorType operatorType);

	String getPatternMatchingWildCard();

	void setPatternMatchingWildCard(String patternMatchingWildCard);

	String getPatternMatchingSingleChar();

	void setPatternMatchingSingleChar(String patternMatchingSingleChar);

	String getPatternMatchingEscape();

	void setPatternMatchingEscape(String patternMatchingEscape);

	FilterModelState getState();

	void synchronize();
	
	String getSupportedWarning();
}
