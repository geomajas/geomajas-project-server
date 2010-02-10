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

package org.geomajas.gwt.client.samples.base;

import com.google.gwt.core.client.GWT;

/**
 * <p>
 * Simple XML parser, that adds colors to the tags and attributes, so it looks good.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class XmlParser {

	private XmlParser() {
	}

	public static String parseXML(String xml) {
		String temp = xml.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		temp = temp.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

		String result = "";
		boolean busy = true;
		boolean inTag = false;
		while (busy) {
			if (isComment(temp)) {
				int endCommentPos = temp.indexOf("--&gt;");
				result += "<span style='color:#0000D0'>" + temp.substring(0, endCommentPos + 6) + "</span>";
				temp = temp.substring(endCommentPos + 6);
			} else if (!inTag && isTagBegin(temp)) {
				result += "<span style='color:#008080'>&lt;";
				temp = temp.substring(4);
				inTag = true;
			} else if (inTag && isTagEnd(temp)) {
				int position = temp.indexOf(';') + 1;
				result += temp.substring(0, position) + "</span>";
				temp = temp.substring(position);
				inTag = false;
			} else if (inTag) {
				if (isWhiteSpace(temp) && temp.indexOf("=") > 0) {
					int equalPosition = temp.indexOf("=");
					result += "<span style='color:#800080'>" + temp.substring(0, equalPosition)
							+ "</span><span style='color:#000000'>=</span>";
					temp = temp.substring(equalPosition + 1);
					int quotePosition = temp.indexOf('"') + 1;
					result += "<span style='color:#000099'>" + temp.substring(0, quotePosition);
					temp = temp.substring(quotePosition);
					quotePosition = temp.indexOf('"') + 1;
					result += temp.substring(0, quotePosition) + "</span>";
					temp = temp.substring(quotePosition);
				} else {
					result += temp.charAt(0);
					temp = temp.substring(1);
				}
			} else {
				result += temp.charAt(0);
				temp = temp.substring(1);
			}
			if (temp.isEmpty()) {
				busy = false;
			}
		}

		GWT.log(result, null);
		return result;
	}

	private static boolean isWhiteSpace(String xml) {
		if (xml.charAt(0) == ' ') {
			return true;
		} else if (xml.indexOf("&nbsp;") == 0) {
			return true;
		} else if (xml.charAt(0) == '\n') {
			return true;
		}
		return false;
	}

	private static boolean isComment(String xml) {
		if (xml.indexOf("&lt;!--") == 0) {
			return true;
		}
		return false;
	}

	private static boolean isTagBegin(String xml) {
		if (xml.indexOf("&lt;") == 0) {
			return true;
		}
		return false;
	}

	private static boolean isTagEnd(String xml) {
		String temp = xml;
		while (isWhiteSpace(temp)) {
			temp = temp.substring(1);
		}
		if (temp.indexOf("&gt;") == 0) {
			return true;
		}
		if (temp.indexOf("/&gt;") == 0) {
			return true;
		}
		return false;
	}
}
