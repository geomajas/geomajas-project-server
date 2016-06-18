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
package org.geomajas.plugin.printing.parser;

import java.util.StringTokenizer;

import com.lowagie.text.Rectangle;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Adapter for converting an iText Rectangle to XML and back.
 * 
 * @author Jan De Moerloose
 */
public class RectangleConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(Rectangle.class);
	}

	@Override
	public Object fromString(String str) {
		StringTokenizer st = new StringTokenizer(str, ",");
		if (st.countTokens() < 4) {
			throw new IllegalArgumentException("Not enough tokens (<4) in rectangle " + str);
		}
		float left = Float.parseFloat(st.nextToken());
		float bottom = Float.parseFloat(st.nextToken());
		float right = Float.parseFloat(st.nextToken());
		float top = Float.parseFloat(st.nextToken());
		return new Rectangle(left, bottom, right, top);
	}

	@Override
	public String toString(Object obj) {
		Rectangle rectangle = (Rectangle) obj;
		if (obj == null) {
			return null;
		}
		return rectangle.getLeft() + "," + rectangle.getBottom() + "," + rectangle.getRight() + ","
				+ rectangle.getTop();
	}

}