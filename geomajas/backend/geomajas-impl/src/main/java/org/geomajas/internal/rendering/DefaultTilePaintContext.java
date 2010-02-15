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

package org.geomajas.internal.rendering;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.rendering.painter.StyledLayer;
import org.geomajas.rendering.painter.TilePaintContext;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * The tile context is a general meta data object that is vital to the rendering process of creating images for tiles.
 * </p>
 * 
 * @see org.geomajas.internal.rendering.image.TileImageCreatorImpl
 * @author Pieter De Graef
 */
public class DefaultTilePaintContext implements TilePaintContext {

	private List<StyledLayer> layers = new ArrayList<StyledLayer>();

	private CoordinateReferenceSystem crs;

	private Envelope areaOfInterest;

	private double scale;

	private RenderingHints java2dHints;

	// -------------------------------------------------------------------------
	// Constructor
	// -------------------------------------------------------------------------

	/**
	 * Get the full list of layers that must be painted.
	 * 
	 * @return list of layers
	 */
	public List<StyledLayer> getLayers() {
		return layers;
	}

	/**
	 * Set a new area of interest. Normally this would be the tile's bounding box in world space.
	 * 
	 * @param areaOfInterest
	 *            The new area of interest.
	 */
	public void setAreaOfInterest(Envelope areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}

	/**
	 * Get the current area of interest. Normally this would be the tile's bounding box in world space.
	 * 
	 * @return Current area of interest
	 * 
	 */
	public Envelope getAreaOfInterest() {
		return areaOfInterest;
	}

	/**
	 * Get the current coordinate system.
	 * 
	 * @return the coordinate system of this box.
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return crs;
	}

	/**
	 * Set the <code>CoordinateReferenceSystem</code> for this map context.
	 * 
	 * @param crs
	 */
	public void setCrs(CoordinateReferenceSystem crs) {
		this.crs = crs;
	}

	/**
	 * Return the current client-side scale.
	 * 
	 * @return
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Set the current client-side scale.
	 * 
	 * @param scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * Retrieve the rendering hints.
	 * 
	 * @return
	 */
	public RenderingHints getRenderingHints() {
		return java2dHints;
	}

	/**
	 * Set new Java rendering hints, such as the of anti-aliasing etc.
	 * 
	 * @param hints
	 *            The new hints with which to create images.
	 */
	public void setRenderingHints(RenderingHints hints) {
		java2dHints = hints;
	}

}