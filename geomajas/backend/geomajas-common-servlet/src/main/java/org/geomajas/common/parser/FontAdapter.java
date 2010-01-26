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
package org.geomajas.common.parser;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.awt.Font;
import java.util.StringTokenizer;

/**
 * ???
 *
 * @author check subversion
 */
public class FontAdapter extends XmlAdapter<String, Font> {

	public Font unmarshal(String s) {
		StringTokenizer st = new StringTokenizer(s, ",");
		if (st.countTokens() < 2) {
			throw new IllegalArgumentException("Not enough tokens (<3) in font " + s);
		}
		int count = st.countTokens();

		String name = st.nextToken();
		int styleIndex = Font.PLAIN;
		if (count > 2) {
			String style = st.nextToken();
			if (style.equalsIgnoreCase("bold")) {
				styleIndex = Font.BOLD;
			} else if (style.equalsIgnoreCase("italic")) {
				styleIndex = Font.ITALIC;
			}
		}
		int size = Integer.parseInt(st.nextToken());
		return new Font(name, styleIndex, size);
	}

	public String marshal(Font font) {
		String style = null;
		if (font.getStyle() == Font.BOLD) {
			style = "bold";
		} else if (font.getStyle() == Font.ITALIC) {
			style = "italic";
		}
		return font.getName() + (style == null ? "" : "," + style) + "," + font.getSize();
	}
}