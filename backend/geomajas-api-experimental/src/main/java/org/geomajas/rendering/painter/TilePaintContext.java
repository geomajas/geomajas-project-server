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

package org.geomajas.rendering.painter;

import java.awt.RenderingHints;
import java.util.List;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * The map context is a general meta data object that is vital to the rendering process of creating images for tiles.
 * </p>
 * 
 * @author Pieter De Graef
 * @author Joachim Van der Auwera
 */
public interface TilePaintContext {

	/**
	 * Get the full list of layers that must be painted.
	 * 
	 * @return list of layers (using the info object)
	 */
	List<StyledLayer> getLayers();

	/**
	 * Get the current area of interest. Normally this would be the tile's bounding box in world space.
	 * 
	 * @return Current area of interest
	 * 
	 */
	Envelope getAreaOfInterest();

	/**
	 * Get the current coordinate system.
	 * 
	 * @return the coordinate system of this box.
	 */
	CoordinateReferenceSystem getCoordinateReferenceSystem();

	/**
	 * Return the current client-side scale.
	 * 
	 * @return scale
	 */
	double getScale();

	/**
	 * Retrieve the rendering hints.
	 * 
	 * @return rendering hints
	 */
	RenderingHints getRenderingHints();

}
