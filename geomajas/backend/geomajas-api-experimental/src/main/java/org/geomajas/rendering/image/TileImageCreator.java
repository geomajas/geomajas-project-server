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

package org.geomajas.rendering.image;

import org.geomajas.rendering.painter.TilePaintContext;
import org.geomajas.rendering.painter.image.FeatureImagePainter;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;

/**
 * <p>
 * This is the central image creation class. When an image has to be created for a tile, this is the class that will do
 * it.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface TileImageCreator {

	/**
	 * Key for turning anti-aliasing on and off. Value must be 'true' or 'false'.
	 */
	String ANTIALIAS = "ANTIALIAS";

	/**
	 * Key for turning anti-aliasing for text on and off. Value must be 'true' or 'false'.
	 */
	String TEXT_ANTIALIAS = "TEXT_ANTIALIAS";

	/**
	 * Key for choosing which interpolation method to use. Available choices are:
	 * <ul>
	 * <li>nearest_neighbour</li>
	 * <li>bilinear</li>
	 * <li>bicubic</li>
	 * </ul>
	 */
	String INTERPOLATION = "INTERPOLATION";

	/**
	 * Key for choosing what quality to use when calculating alpha interpolation. Available choices are:
	 * <ul>
	 * <li>speed</li>
	 * <li>default</li>
	 * <li>quality</li>
	 * </ul>
	 */
	String ALPHA_INTERPOLATION = "ALPHA_INTERPOLATION";

	/**
	 * Key for choosing what quality to use when executing the general rendering. Available choices are:
	 * <ul>
	 * <li>speed</li>
	 * <li>default</li>
	 * <li>quality</li>
	 * </ul>
	 */
	String RENDER_QUALITY = "RENDER_QUALITY";

	/**
	 * Adds a painter to the list. Make sure you add them in the right order!
	 */
	void registerPainter(FeatureImagePainter painter);

	/**
	 * Paint an image, given a certain map context and bounding box. What show be painted, and how rendering should
	 * happen, is all defined in the map context object.
	 *
	 * @param paintArea
	 * @param tileContext
	 * @return
	 */
	RenderedImage paint(Rectangle paintArea, TilePaintContext tileContext);
}
