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

/**
 * ???
 *
 * @author check subversion
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