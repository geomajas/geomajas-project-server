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
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.spatial.Bbox;

/**
 * ???
 *
 * @author check subversion
 */
public class Text implements Paintable {

	/**
	 * A preferably unique ID that identifies the object even after it is painted. This can later be used to update or
	 * delete it from the <code>GraphicsContext</code>.
	 */
	private String id;

	private String content;

	private Coordinate position;

	private FontStyle style;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public Text() {
	}

	public Text(String id) {
		this.id = id;
	}

	public Text(String id, String content, Coordinate position, FontStyle style) {
		this.id = id;
		this.content = content;
		this.position = position;
		this.style = style;
	}

	// -------------------------------------------------------------------------
	// Paintable implementation:
	// -------------------------------------------------------------------------

	public void accept(PainterVisitor visitor, Bbox bounds, boolean recursive) {
		visitor.visit(this);
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public FontStyle getStyle() {
		return style;
	}

	public void setStyle(FontStyle style) {
		this.style = style;
	}

	public Coordinate getPosition() {
		return position;
	}

	public void setPosition(Coordinate position) {
		this.position = position;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setId(String id) {
		this.id = id;
	}
}
