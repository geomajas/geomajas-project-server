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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.paintable.Text;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * <p>
 * Map add-on that displays a "powered by geomajas" text on the bottom right of the map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Watermark extends MapAddon {

	private Text text1;

	private Text text2;

	private Text text3;
	
	// Constructor:

	public Watermark(String id) {
		super(id, 122, 12);
		text1 = new Text(id + ".text1", "powered by", getUpperLeftCorner(), new FontStyle("#000000", 10, "Arial",
				"normal", "normal"));
		text2 = new Text(id + ".text2", "geo", getUpperLeftCorner(), new FontStyle("#000000", 10, "Arial", "bold",
				"normal"));
		text3 = new Text(id + ".text3", "majas", getUpperLeftCorner(), new FontStyle("#259447", 10, "Arial", "bold",
				"normal"));
	}
	
	// MapAddon implementation:

	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		text1.setPosition(getUpperLeftCorner());
		text2.setPosition(new Coordinate(getUpperLeftCorner().getX() + 62, getUpperLeftCorner().getY()));
		text3.setPosition(new Coordinate(getUpperLeftCorner().getX() + 83, getUpperLeftCorner().getY()));

		visitor.visit(text1);
		visitor.visit(text2);
		visitor.visit(text3);
	}

	public void onDraw() {
	}

	public void onRemove() {
	}
}
