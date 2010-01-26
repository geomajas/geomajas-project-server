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
package org.geomajas.internal.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ???
 *
 * @author check subversion
 */
public final class StringUtil {

	private static final Pattern SUBSTITUTE_PATTERN = Pattern.compile("(?<!\\\\)\\$\\{.+?\\}");

	private static final Pattern FORMULA_LEFT_PATTERN = Pattern.compile("\\$left:\\d+\\{.*?\\}");

	private StringUtil() {
	}

	/**
	 * Parses ${paramName} like expressions.
	 *
	 * @param expression expression to replace keys in
	 * @param parameters key/value pairs to use for replacement
	 * @return expression with keys replaced by the values from parameters
	 * @throws IllegalArgumentException when key not found
	 */
	public static String substituteParameters(String expression, Map<String, String> parameters)
			throws IllegalArgumentException {
		if (expression == null || "".equals(expression)) {
			return "";
		}
		if (parameters == null) {
			throw new IllegalArgumentException("please provide parameter map");
		}
		int pos = 0;
		int max = expression.length();
		StringBuilder sb = new StringBuilder(max);
		Matcher m = SUBSTITUTE_PATTERN.matcher(expression);
		String value = "";
		while (m.find()) {
			value = parameters.get(expression.substring(m.start() + 2, m.end() - 1));
			if (value == null) {
				throw new IllegalArgumentException(expression.substring(m.start() + 2, m.end() - 1));
			} else {
				sb.append(expression.substring(pos, m.start()));
				sb.append(value);
				pos = m.end();
			}
		}
		if (pos == 0) {
			return expression;
		} // don't bother copying if nothing found
		sb.append(expression.substring(pos, max));
		return sb.toString();
	}

	/**
	 * Substitute formulas in the string.
	 * Only one formula so far:<br>
	 * <code>$left:2{value}</code>
	 *
	 * @param expression
	 * @return
	 */
	public static String substituteFormulas(String expression) {
		if (expression == null || "".equals(expression)) {
			return "";
		}
		int pos = 0;
		int max = expression.length();
		StringBuilder sb = new StringBuilder(max);
		Matcher m = FORMULA_LEFT_PATTERN.matcher(expression);
		String rawValue = "";
		int len = 0;
		String value;
		int delimPos;
		while (m.find()) {
			rawValue = expression.substring(m.start() + 6, m.end() - 1);
			if (rawValue == null) {
				throw new IllegalArgumentException(expression.substring(m.start() + 6, m.end() - 1));
			} else {
				delimPos = rawValue.indexOf("{");
				len = Integer.valueOf(rawValue.substring(0, delimPos));
				value = rawValue.substring(delimPos + 1);
				if (value.length() > len) {
					value = value.substring(0, len);
				}
				sb.append(expression.substring(pos, m.start()));
				sb.append(value);
				pos = m.end();
			}
		}
		if (pos == 0) {
			return expression;
		} // don't bother copying if nothing found
		sb.append(expression.substring(pos, max));
		return sb.toString();
	}

	public static String leftPad(String str, int length, char ch) {
		return (length > str.length()) ? createPadding(ch, length - str.length()).concat(str) : str;
	}

	public static String rightPad(String str, int length, char ch) {
		return (length > str.length()) ? str.concat(createPadding(ch, length - str.length())) : str;
	}

	public static String createPadding(char ch, int length) {
		char[] padding = new char[length];
		for (int i = 0; i < length; ++i) {
			padding[i] = ch;
		}
		return new String(padding).intern();
	}

}
