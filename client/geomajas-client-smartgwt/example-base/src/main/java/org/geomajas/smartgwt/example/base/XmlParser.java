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

package org.geomajas.smartgwt.example.base;

import com.google.gwt.core.client.GWT;

/**
 * <p>
 * Simple XML parser, that adds colors to the tags and attributes, so it looks good.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class XmlParser {

	private XmlParser() {
	}

	public static String parseXML(String xml) {
		String temp = xml;

		// remove copyright at start
		int pos1 = xml.indexOf("www.geosparc.com");
		int pos2 = xml.indexOf("-->");
		if (pos1 > 0 && pos1 < pos2) {
			pos2 += 3;
			// while (Character.isWhitespace(temp.charAt(pos2))) {
			while (("" + temp.charAt(pos2)).matches("\\s")) {
				pos2++;
			}
			temp = temp.substring(pos2);
		}

		// base replacements for proper html
		temp = temp.replaceAll("<!--.*@extract-start.*-->", "");
		temp = temp.replaceAll("<!--.*@extract-skip.*-->", "");
		temp = temp.replaceAll("<!--.*@extract-end.*-->", "");
		temp = temp.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		temp = temp.replaceAll("\\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

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
			if (temp.length() == 0) {
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
