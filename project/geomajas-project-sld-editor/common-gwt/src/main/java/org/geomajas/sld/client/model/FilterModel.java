package org.geomajas.sld.client.model;

import org.geomajas.sld.filter.FilterTypeInfo;

public interface FilterModel {

	public enum OperatorType {
		PROPERTY_IS_EQUAL_TO, PROPERTY_IS_GREATER_THAN, PROPERTY_IS_GREATER_THAN_OR_EQUAL, PROPERTY_IS_LESS_THAN, PROPERTY_IS_LESS_THAN_OR_EQUAL, PROPERTY_IS_NOT_EQUAL, PROPERTY_IS_BETWEEN, PROPERTY_IS_LIKE, PROPERTY_IS_NULL, PROPERTY_IS_NOT_BETWEEN, PROPERTY_IS_NOT_LIKE, PROPERTY_IS_NOT_NULL,
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

	public abstract boolean isValid();
}
