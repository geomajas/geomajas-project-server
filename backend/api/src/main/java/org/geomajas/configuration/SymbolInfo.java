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
package org.geomajas.configuration;

import org.geomajas.global.Api;

import java.io.Serializable;

/**
 * Symbol configuration information. Choose one the options (either circle, rectangle or image).
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class SymbolInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	private CircleInfo circle;

	private RectInfo rect;

	private ImageInfo image;

	/**
	 * Get the circle for the symbol.
	 * 
	 * @return circle
	 */
	public CircleInfo getCircle() {
		return circle;
	}

	/**
	 * Set circle for the symbol.
	 * 
	 * @param circle
	 *            circle
	 */
	public void setCircle(CircleInfo circle) {
		this.circle = circle;
	}

	/**
	 * Get the rectangle for this symbol.
	 * 
	 * @return rectangle
	 */
	public RectInfo getRect() {
		return rect;
	}

	/**
	 * Set the rectangle for this symbol.
	 * 
	 * @param rect
	 *            rectangle
	 */
	public void setRect(RectInfo rect) {
		this.rect = rect;
	}

	/**
	 * @return  Get the image for this symbol.
	 */
	public ImageInfo getImage() {
		return image;
	}

	/**
	 * Set the image for this symbol.
	 * 
	 * @param image
	 *            the image symbol.
	 */
	public void setImage(ImageInfo image) {
		this.image = image;
	}
}
