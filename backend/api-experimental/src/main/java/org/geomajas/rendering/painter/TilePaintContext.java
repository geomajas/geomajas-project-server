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
