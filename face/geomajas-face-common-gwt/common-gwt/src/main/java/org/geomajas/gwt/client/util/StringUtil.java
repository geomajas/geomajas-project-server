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
package org.geomajas.gwt.client.util;

import java.util.Iterator;
import java.util.List;

/**
 * Utility for string operations.
 * 
 * @author Jan De Moerloose
 * @since 1.9.0
 */
public final class StringUtil {

	private StringUtil() {
		// do not allow instantiation.
	}

	/**
	 * Joins a list of strings using the specified separator.
	 * 
	 * @param parts string parts
	 * @param separator separator
	 * @return concatenated string
	 */
	public static String join(List<String> parts, String separator) {
		Iterator<String> it = parts.iterator();
		StringBuilder builder = new StringBuilder();
		while (it.hasNext()) {
			String part = it.next();
			builder.append(part);
			if (it.hasNext()) {
				builder.append(separator);
			}
		}
		return builder.toString();
	}

	/**
	 * Extract the extension part of a file name (e.g. 'some.txt' -> 'txt')
	 * @param fileName the file name
	 * @return the extension (without '.')
	 */
	public static String getExtension(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index >= 0) {
			return fileName.substring(index + 1);
		} else {
			return null;
		}
	}
}
