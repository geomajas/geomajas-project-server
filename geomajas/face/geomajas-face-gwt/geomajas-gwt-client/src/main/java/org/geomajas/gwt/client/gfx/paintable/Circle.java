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

package org.geomajas.gwt.client.gfx.paintable;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * <p>
 * A circle that can be drawn onto a <code>GraphicsContext</code>.
 * </p>
 *
 * @author Pieter De Graef
 */
public class Circle implements Paintable {

	/**
	 * A preferably unique ID that identifies the object even after it is painted. This can later be used to update or
	 * delete it from the <code>GraphicsContext</code>.
	 */
	private String id;

	/**
	 * A circle needs to be drawn at a certain location.
	 */
	private Coordinate position;

	/**
	 * Radius of the circle.
	 */
	private float radius;

	/**
	 * The styling object.
	 */
	private ShapeStyle style;

	// Constructors:

	/**
	 * constructor setting the id.
	 */
	public Circle(String id) {
		this.id = id;
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	/**
	 * Everything that can be drawn on the map, must be accessible by a PainterVisitor!
	 *
	 * @param visitor
	 *            A PainterVisitor object. Comes from a MapWidget.
	 * @param bounds
	 *            Not used here.
	 * @param recursive
	 *            Not used here.
	 */
	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		visitor.visit(this);
	}

	// Getters and setters:

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public ShapeStyle getStyle() {
		return style;
	}

	public void setStyle(ShapeStyle style) {
		this.style = style;
	}
}