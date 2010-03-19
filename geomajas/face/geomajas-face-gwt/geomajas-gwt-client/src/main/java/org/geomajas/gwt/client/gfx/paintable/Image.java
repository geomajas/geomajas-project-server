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

import org.geomajas.gwt.client.gfx.PainterVisitor;
import org.geomajas.gwt.client.gfx.style.PictureStyle;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.layer.tile.RasterTile;

/**
 * <p>
 * Implementation of the <code>Paintable</code> interface for drawing images.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class Image extends AbstractWorldPaintable {

	/**
	 * Location of the actual image.
	 */
	private String href;

	/**
	 * Bounding box storing position, width and height of the image.
	 */
	private Bbox bounds;

	// -------------------------------------------------------------------------
	// Constructors:
	// -------------------------------------------------------------------------

	public Image(String id) {
		super(id);
	}

	public Image(RasterTile raster) {
		super(raster.getId());
		href = raster.getUrl();
		bounds = new Bbox(raster.getBounds());
		setOriginalStyle(new PictureStyle(1));
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

	public void setStyle(PictureStyle style) {
		setOriginalStyle(style);
	}

	public Bbox getBounds() {
		return bounds;
	}

	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}