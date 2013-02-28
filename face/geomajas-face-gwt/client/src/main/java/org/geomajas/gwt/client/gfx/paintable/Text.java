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

package org.geomajas.gwt.client.gfx.paintable;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * <p>
 * Paintable definition for drawing text onto a GraphicsWidget.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Text extends AbstractWorldPaintable {

	private String content;

	private FontStyle style;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public Text(String id) {
		super(id);
	}

	public Text(String id, String content, Coordinate position, FontStyle style) {
		super(id);
		this.content = content;
		this.original = position;
		this.style = style;
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	public void accept(PainterVisitor visitor, Object group, Bbox bounds, boolean recursive) {
		visitor.visit(this, group);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setStyle(FontStyle style) {
		this.style = style;
	}

	public FontStyle getStyle() {
		return style;
	}

	/**
	 * Get the screen space upper-left corner position of the text.
	 * 
	 * @return upper-left corner coordinate in screen space
	 */
	public Coordinate getPosition() {
		return (Coordinate) getLocation();
	}

	/**
	 * Set the upper-left corner position of the text.
	 * 
	 * @param position upper-left corner coordinate in world space
	 */
	public void setPosition(Coordinate position) {
		setOriginalLocation(position);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
