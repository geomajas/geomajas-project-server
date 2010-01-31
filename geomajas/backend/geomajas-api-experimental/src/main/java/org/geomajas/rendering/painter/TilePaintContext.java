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

import org.geomajas.geometry.Bbox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.awt.RenderingHints;
import java.util.List;

/**
 * <p>
 * The map context is a general meta data object that is vital to the rendering process of creating images for tiles.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface TilePaintContext {

	/**
	 * Remove a layer's paint context from the list.
	 *
	 * @param layerPaintContext context to delete
	 * @return true when object was removed
	 */
	boolean remove(LayerPaintContext layerPaintContext);

	/**
	 * Add a layer's paint context to the list. When the tile images are created, using this context it
	 * will paint all layers in this list.
	 *
	 * @param layerPaintContext context to add
	 * @return true when object was added
	 */
	boolean add(LayerPaintContext layerPaintContext);

	/**
	 * Return the number of layers in this map context.
	 *
	 * @return number of layers
	 */
	int getLayerCount();

	/**
	 * Get the full list of layers that must be painted.
	 *
	 * @return list of layer paint contexts
	 */
	List<LayerPaintContext> getLayerPaintContexts();

	/**
	 * Set a new area of interest. Normally this would be the tile's bounding box in world space.
	 *
	 * @param areaOfInterest
	 *            The new area of interest.
	 */
	void setAreaOfInterest(Bbox areaOfInterest);

	/**
	 * Get the current area of interest. Normally this would be the tile's bounding box in world space.
	 *
	 * @return Current area of interest
	 *
	 */
	Bbox getAreaOfInterest();

	/**
	 * Get the current coordinate system.
	 *
	 * @return the coordinate system of this box.
	 */
	CoordinateReferenceSystem getCoordinateReferenceSystem();

	/**
	 * Set the <code>CoordinateReferenceSystem</code> for this map context.
	 *
	 * @param crs coordinate reference system
	 */
	void setCrs(CoordinateReferenceSystem crs);

	/**
	 * Return the current client-side scale.
	 *
	 * @return scale
	 */
	double getScale();

	/**
	 * Set the current client-side scale.
	 *
	 * @param scale scale
	 */
	void setScale(double scale);

	/**
	 * Retrieve the rendering hints.
	 *
	 * @return rendering hints
	 */
	RenderingHints getRenderingHints();

	/**
	 * Set new Java rendering hints, such as the of anti-aliasing etc.
	 *
	 * @param hints The new hints with which to create images.
	 */
	void setRenderingHints(RenderingHints hints);
}
