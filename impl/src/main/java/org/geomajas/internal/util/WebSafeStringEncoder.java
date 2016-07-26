/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.util;

/**
 * Utility to assure strings are web/url safe.
 *
 * @author Pieter De Graef
 */
public final class WebSafeStringEncoder {

	private WebSafeStringEncoder() {
	}

	public static String escapeHTML(String s) {
		StringBuilder buf = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9') {
				buf.append(c);
//			} else if ((int)c == 32) {
//				buf.append("&nbsp;");
//			} else if ((int)c == 34) {
//				buf.append("&quot;");
//			} else if ((int)c == 38) {
//				buf.append("&amp;");
//			} else if ((int)c == 60) {
//				buf.append("&lt;");
//			} else if ((int)c == 62) {
//				buf.append("&gt;");
			} else {
				buf.append("&#").append((int) c).append(";");
			}
		}
		return buf.toString();
	}
}