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

import java.awt.Color;
import java.util.StringTokenizer;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Adapter for marshalling and unmarshalling colours.
 * 
 * @author Jan De Moerloose
 */
public class ColorConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		return type.equals(Color.class);
	}

	@Override
	public Object fromString(String str) {
		StringTokenizer st = new StringTokenizer(str, "rgba,=");
		int r = 255;
		int g = 255;
		int b = 255;
		int a = 255;
		if (st.hasMoreTokens()) {
			r = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			g = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			b = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			a = Integer.parseInt(st.nextToken());
		}
		return new Color(r, g, b, a);
	}

	@Override
	public String toString(Object obj) {
		Color c = (Color) obj;
		if (obj == null) {
			return null;
		}
		return "r=" + c.getRed() + ",g=" + c.getGreen() + ",b=" + c.getBlue() + ",a=" + c.getAlpha();
	}

}