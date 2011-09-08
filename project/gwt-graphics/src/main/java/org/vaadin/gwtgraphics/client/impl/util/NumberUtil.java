package org.vaadin.gwtgraphics.client.impl.util;

import com.google.gwt.dom.client.Element;

/**
 * This util class contains some static helpers for number parsing.
 * 
 * @author Henri Kerola / IT Mill Ltd
 * 
 */
public abstract class NumberUtil {

	public static int parseIntValue(Element element, String attr, int defaultVal) {
		return parseIntValue(element.getAttribute(attr), defaultVal);
	}

	public static int parseIntValue(String value, int defaultVal) {
		if (value == null) {
			return defaultVal;
		}
		if (value.endsWith("px")) {
			value = value.substring(0, value.length() - 2);
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}

	/**
	 * Parses a double value from a given String. If this String is null or
	 * does't contain a parsable double, defaultVal is returned.
	 * 
	 * @param value
	 *            String to be parsed.
	 * @param defaultVal
	 * @return
	 */
	public static double parseDoubleValue(String value, double defaultVal) {
		if (value == null) {
			return defaultVal;
		}
		if (value.endsWith("px")) {
			value = value.substring(0, value.length() - 2);
		}
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}
}
