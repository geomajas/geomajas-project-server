/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.gwt.client.gfx.paintable;

import org.geomajas.geometry.Coordinate;
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
public class Circle extends AbstractWorldPaintable {

	/** Radius of the circle. */
	private float radius;

	private ShapeStyle style;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	/**
	 * constructor setting the id.
	 */
	public Circle(String id) {
		super(id);
	}

	// -------------------------------------------------------------------------
	// WorldPaintable implementation:
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
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public Coordinate getPosition() {
		return (Coordinate) getLocation();
	}

	public void setPosition(Coordinate position) {
		setOriginalLocation(position);
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