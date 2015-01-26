/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.parser;

import java.awt.Font;
import java.util.StringTokenizer;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Adapter for converting a font to XML and back.
 * 
 * @author Jan De Moerloose
 */
public class FontConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(Font.class);
	}

	@Override
	public Object fromString(String str) {
		StringTokenizer st = new StringTokenizer(str, ",");
		if (st.countTokens() < 2) {
			throw new IllegalArgumentException("Not enough tokens (<3) in font " + str);
		}
		int count = st.countTokens();

		String name = st.nextToken();
		int styleIndex = Font.PLAIN;
		if (count > 2) {
			String style = st.nextToken();
			if ("bold".equalsIgnoreCase(style)) {
				styleIndex = Font.BOLD;
			} else if ("italic".equalsIgnoreCase("style")) {
				styleIndex = Font.ITALIC;
			}
		}
		int size = Integer.parseInt(st.nextToken());
		return new Font(name, styleIndex, size);
	}

	@Override
	public String toString(Object obj) {
		Font font = (Font) obj;
		if (obj == null) {
			return null;
		}
		String style = null;
		if (font.getStyle() == Font.BOLD) {
			style = "bold";
		} else if (font.getStyle() == Font.ITALIC) {
			style = "italic";
		}
		return font.getName() + (style == null ? "" : "," + style) + "," + font.getSize();
	}

}