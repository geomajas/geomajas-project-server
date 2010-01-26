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

import org.geomajas.gwt.client.gfx.Paintable;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.spatial.geometry.Geometry;

/**
 * <p>
 * A {@link Geometry} that can be drawn onto a {@link org.geomajas.gwt.client.gfx.GraphicsContext}. It therefore
 *  the {@link org.geomajas.gwt.client.gfx.Paintable} interface.
 * </p>
 *
 * @author Pieter De Graef
 */
public class GfxGeometry implements Paintable {

	/**
	 * A preferably unique ID that identifies the object even after it is painted. This can later be used to update or
	 * delete it from the <code>GraphicsContext</code>.
	 */
	private String id;

	private Geometry geometry;

	private ShapeStyle style;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public GfxGeometry() {
	}

	public GfxGeometry(String id) {
		this.id = id;
	}

	public GfxGeometry(String id, Geometry geometry, ShapeStyle style) {
		this.id = id;
		this.geometry = geometry;
		this.style = style;
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	/**
	 * Everything that can be drawn on the map, must be accessible by a PainterVisitor!
	 *
	 * @param visitor
	 *            A PainterVisitor object.
	 */
	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		visitor.visit(this);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public ShapeStyle getStyle() {
		return style;
	}

	public void setStyle(ShapeStyle style) {
		this.style = style;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
}
