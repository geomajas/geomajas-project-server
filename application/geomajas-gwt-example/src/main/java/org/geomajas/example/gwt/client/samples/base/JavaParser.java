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

package org.geomajas.example.gwt.client.samples.base;

/**
 * <p>
 * Simple Java parser, that adds colors to the Java code, so it looks good.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class JavaParser {

	private JavaParser() {
	}

	public static String parseJava(String java) {
		String temp = java.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		temp = temp.replaceAll("public", "<span style='color:#800080;'>public</span>");
		temp = temp.replaceAll("private", "<span style='color:#800080;'>private</span>");
		temp = temp.replaceAll("return", "<span style='color:#800080;'>return</span>");
		temp = temp.replaceAll("int", "<span style='color:#800080;'>int</span>");
		temp = temp.replaceAll("short", "<span style='color:#800080;'>short</span>");
		temp = temp.replaceAll("long", "<span style='color:#800080;'>long</span>");
		temp = temp.replaceAll("float", "<span style='color:#800080;'>float</span>");
		temp = temp.replaceAll("double", "<span style='color:#800080;'>double</span>");
		temp = temp.replaceAll("char", "<span style='color:#800080;'>char</span>");
		temp = temp.replaceAll("void", "<span style='color:#800080;'>void</span>");
		temp = temp.replaceAll("class", "<span style='color:#800080;'>class</span>");
		temp = temp.replaceAll("static", "<span style='color:#800080;'>static</span>");
		temp = temp.replaceAll("final", "<span style='color:#800080;'>final</span>");
		temp = temp.replaceAll("try", "<span style='color:#800080;'>try</span>");
		temp = temp.replaceAll("catch", "<span style='color:#800080;'>catch</span>");
		temp = temp.replaceAll("throw", "<span style='color:#800080;'>throw</span>");
		temp = temp.replaceAll("new", "<span style='color:#800080;'>new</span>");
		temp = temp.replaceAll("instanceof", "<span style='color:#800080;'>instanceof</span>");
		temp = temp.replaceAll("byte", "<span style='color:#800080;'>byte</span>");
		temp = temp.replaceAll("if", "<span style='color:#800080;'>if</span>");
		temp = temp.replaceAll("for", "<span style='color:#800080;'>for</span>");
		temp = temp.replaceAll("while", "<span style='color:#800080;'>while</span>");
		temp = temp.replaceAll("switch", "<span style='color:#800080;'>switch</span>");
		temp = temp.replaceAll("true", "<span style='color:#800080;'>true</span>");
		temp = temp.replaceAll("false", "<span style='color:#800080;'>false</span>");

		String result = "";
		boolean busy = true;
		while (busy) {
			char c = temp.charAt(0);
			if (c == '"') {
				temp = temp.substring(1);
				int position = temp.indexOf('"') + 1;
				result += "<span style='color:#000099;'>\"" + temp.substring(0, position) + "</span>";
				temp = temp.substring(position);
			} else if (c == '\'') {
				temp = temp.substring(1);
				int position = temp.indexOf('\'') + 1;
				result += "'" + temp.substring(0, position);
				temp = temp.substring(position);
			} else if (isSingleLineComment(temp)) {
				int position = temp.indexOf("\n");
				String commentString = temp.substring(0, position);
				commentString = commentString.replaceAll("<span style='color:#800080;'>", "");
				commentString = commentString.replaceAll("</span>", "");
				result += "<span style='color:#009900;'>" + commentString + "</span>";
				temp = temp.substring(position);
			} else if (isMultiLineComment(temp)) {
				int position = temp.indexOf("*/") + 2;
				String commentString = temp.substring(0, position);
				commentString = commentString.replaceAll("<span style='color:#800080;'>", "");
				commentString = commentString.replaceAll("</span>", "");
				result += "<span style='color:#3F5FBF;'>" + commentString + "</span>";
				temp = temp.substring(position);
			} else {
				result += c;
				temp = temp.substring(1);
			}
			if (temp.length() == 0) {
				busy = false;
			}
		}

		return result;
	}

	private static boolean isSingleLineComment(String content) {
		return content.startsWith("//");
	}
	
	private static boolean isMultiLineComment(String content) {
		return content.startsWith("/*");
	}
}
