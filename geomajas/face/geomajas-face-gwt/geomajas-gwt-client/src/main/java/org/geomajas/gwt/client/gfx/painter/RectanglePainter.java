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

package org.geomajas.gwt.client.gfx.painter;

import org.geomajas.gwt.client.gfx.GraphicsContext;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.Painter;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;

/**
 * <p>
 * Painter implementation for circles.
 * </p>
 *
 * @author Pieter De Graef
 */
public class RectanglePainter implements Painter {

	/**
	 * Empty default constructor.
	 */
	public RectanglePainter() {
	}

	/**
	 * Return the class-name of the type of object this painter can paint.
	 *
	 * @return Return the class-name as a string.
	 */
	public String getPaintableClassName() {
		return Rectangle.class.getName();
	}

	/**
	 * The actual painting function. Draws the circles with the object's id.
	 *
	 * @param paintable
	 *            A {@link Rectangle} object.
	 * @param graphics
	 *            A GraphicsContext object, responsible for actual drawing.
	 */
	public void paint(Paintable paintable, GraphicsContext graphics) {
		Rectangle rectangle = (Rectangle) paintable;
		graphics.drawRectangle(rectangle.getId(), rectangle.getBounds(), rectangle.getStyle());
	}

	/**
	 * Delete a <code>Paintable</code> object from the given <code>GraphicsContext</code>. It the object does not exist,
	 * nothing will be done.
	 *
	 * @param paintable
	 *            The object to be painted.
	 * @param graphics
	 *            The context to paint on.
	 */
	public void deleteShape(Paintable paintable, GraphicsContext graphics) {
		graphics.deleteShape(paintable.getId(), false);
	}
}
