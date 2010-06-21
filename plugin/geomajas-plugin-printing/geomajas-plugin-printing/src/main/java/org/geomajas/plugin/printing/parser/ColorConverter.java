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