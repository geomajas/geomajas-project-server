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
package org.geomajas.extension.printing.parser;

import com.lowagie.text.Rectangle;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.StringTokenizer;

/**
 * Adapter for converting an iText Rectangle to XML and back.
 *
 * @author Jan De Moerloose
 */
public class RectangleAdapter extends XmlAdapter<String, Rectangle> {

	@Override
	public Rectangle unmarshal(String s) throws Exception {
		StringTokenizer st = new StringTokenizer(s, ",");
		if (st.countTokens() < 4) {
			throw new IllegalArgumentException("Not enough tokens (<4) in rectangle " + s);
		}
		float left = Float.parseFloat(st.nextToken());
		float bottom = Float.parseFloat(st.nextToken());
		float right = Float.parseFloat(st.nextToken());
		float top = Float.parseFloat(st.nextToken());
		return new Rectangle(left, bottom, right, top);
	}

	@Override
	public String marshal(Rectangle rectangle) throws Exception {
		return rectangle.getLeft() + "," + rectangle.getBottom() + "," + rectangle.getRight() + ","
				+ rectangle.getTop();
	}

}
