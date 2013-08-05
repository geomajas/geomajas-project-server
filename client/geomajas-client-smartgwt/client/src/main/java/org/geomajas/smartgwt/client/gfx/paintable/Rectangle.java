/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.smartgwt.client.gfx.paintable;

import org.geomajas.smartgwt.client.gfx.PainterVisitor;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.smartgwt.client.spatial.Bbox;

/**
 * <p>
 * Implementation of the <code>Paintable</code> interface for drawing rectangles. It is only usefull as a rendering
 * object, do not use this as a bounding box.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Rectangle extends AbstractWorldPaintable {

	private ShapeStyle style;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public Rectangle(String id) {
		super(id);
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
	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setStyle(ShapeStyle style) {
		this.style = style;
	}

	public ShapeStyle getStyle() {
		return style;
	}

	public Bbox getBounds() {
		return (Bbox) getLocation();
	}

	public void setBounds(Bbox bounds) {
		setOriginalLocation(bounds);
	}
}