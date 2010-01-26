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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * Provides some functionality for using a non-properties map as propertiesmap
 * (allows you to use UTF-8, and ordering (with LinkedHashMap)).
 * <p>
 * key=value parsing is very rudimentary, no escaping (for /n!!!), multiple "="
 * will be part of value.
 *
 * @author check subversion
 */
public final class CollectionUtil {

	private CollectionUtil() {
	}

	// -- Map
	// -------------------------------------------------------

	/**
	 * parses string to map<String, String>, mapdata can be null.
	 */
	public static Map<String, String> stringToMap(String mapData) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (mapData != null && !"".equals(mapData)) {
			BufferedReader r = new BufferedReader(new StringReader(mapData));
			String line = null;
			String[] parts;
			try {
				while (true) {
					line = r.readLine();
					if (line == null) {
						break;
					}
					if (!"".equals(line) && !line.startsWith("#")) {
						parts = line.split("=", 2);
						if (parts.length != 2) {
							throw new IllegalArgumentException("Not a valid key=value pair: [" + line + "]");
						} else {
							map.put(parts[0], parts[1]);
						}
					}
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("Couldn't create map: " + e.getMessage());
			} finally {
				try {
					r.close();
				} catch (Exception e) {
					// ignore
				}
			}
		}
		return map;
	}

	/**
	 * Put all entries in a simple string.
	 *
	 * @return
	 */
	public static String mapToString(Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder(map.size() * 20); // just a crude
		// initializer
		for (Entry<String, String> entry : map.entrySet()) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("\n");
		}
		return sb.toString();
	}

	// -- Properties map (or Map<String, String>)
	// ------------------------------------------------------------------

	/**
	 * Note that "" is a valid value, only <code>null</code> (not in map) will
	 * return the default value.
	 *
	 * @param key key for finding value
	 * @param defaultValue default value when none found ini map
	 * @param map of values
	 * @return value from map or default when no (convertible) value in map
	 */
	public static String getValue(String key, String defaultValue, Map<String, String> map) {
		String tmp = map.get(key);
		if (tmp == null) {
			map.put(key, defaultValue);
			return defaultValue;
		} else {
			return tmp;
		}
	}

	/**
	 * Note that "" != 0, "" also returns the default value (it doesn't parse to
	 * 0, so would throw exception otherwise).
	 *
	 * @param key key for finding value
	 * @param defaultValue default value when none found ini map
	 * @param map of values
	 * @return value from map or default when no (convertible) value in map
	 */
	public static int getIntValue(String key, int defaultValue, Map<String, String> map) {
		String tmp = map.get(key);
		if (tmp == null || "".equals(tmp)) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(tmp);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
	}

	/**
	 * Note that "" != 0, "" also returns the default value (it doesn't parse to
	 * 0, so would throw exception otherwise).
	 *
	 * @param key key for finding value
	 * @param defaultValue default value when none found ini map
	 * @param map of values
	 * @return value from map or default when no (convertible) value in map
	 */
	public static long getLongValue(String key, long defaultValue, Map<String, String> map) {
		String tmp = map.get(key);
		if (tmp == null || "".equals(tmp)) {
			return defaultValue;
		} else {
			try {
				return Long.parseLong(tmp);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
	}

	/**
	 * Note that "" != 0, "" also returns the default value (it doesn't parse to
	 * 0, so would throw exception otherwise).
	 *
	 * @param key key for finding value
	 * @param defaultValue default value when none found ini map
	 * @param map of values
	 * @return value from map or default when no (convertible) value in map
	 */
	public static double getDoubleValue(String key, double defaultValue, Map<String, String> map) {
		String tmp = map.get(key);
		if (tmp == null || "".equals(tmp)) {
			return defaultValue;
		} else {
			try {
				return Double.parseDouble(tmp);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
	}

	/**
	 * Note that "" != false, "" also returns the default value (no tristate
	 * possible iow. (just parsing would give false).
	 *
	 * @param key key for finding value
	 * @param defaultValue default value when none found ini map
	 * @param map of values
	 * @return value from map or default when no (convertible) value in map
	 */
	public static boolean getBooleanValue(String key, boolean defaultValue, Map<String, String> map)
			throws NumberFormatException {
		String tmp = map.get(key);
		if (tmp == null || "".equals(tmp)) {
			return defaultValue;
		} else {
			try {
				return Boolean.parseBoolean(tmp);
			} catch (NumberFormatException nfe) {
				return defaultValue;
			}
		}
	}

	/**
	 * Comma-delimited list.
	 * <p> empty elements are ignored
	 * <p> extra space is removed (trim)
	 * @param str
	 * @return
	 */
	public static List<String> stringToList(String str) {
		if (str == null) {
			throw new IllegalArgumentException("please provide a string");
		}
		List<String> res = new ArrayList<String>();
		if ("".equals(str)) {
			return res;
		}

		String[] vals = str.split(",");
		String tmp;
		for (int i = 0; i < vals.length; i++) {
			tmp = vals[i].trim();
			if (!"".equals(tmp)) {
				res.add(tmp);
			}
		}
		return res;
	}

	/**
	 * Warning! no escaping (eg. values should not contain strings with commas).
	 * <p> empty & null elements are ignored
	 * <p> extra space is removed (trim)
	 * @param values
	 * @return
	 */
	public static String listToString(List<String> values) {
		if (values == null) {
			return null;
		}
		if (values.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String s : values) {
			sb.append(s.trim());
			sb.append(",");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}
}
