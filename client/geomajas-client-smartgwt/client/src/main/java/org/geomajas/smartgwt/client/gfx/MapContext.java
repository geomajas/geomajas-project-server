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
package org.geomajas.smartgwt.client.gfx;

import org.geomajas.annotation.Api;
import org.geomajas.smartgwt.client.widget.event.HasGraphicsReadyHandlers;

/**
 * Abstraction of the map for painting rasters and vectors.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public interface MapContext extends HasGraphicsReadyHandlers {

	/**
	 * Get the context for rendering vector data.
	 * 
	 * @return a graphics context
	 */
	GraphicsContext getVectorContext();

	/**
	 * Get the context for rendering raster images.
	 * 
	 * @return an image context
	 */
	ImageContext getRasterContext();

	/**
	 * Get the right mouse menu context.
	 * 
	 * @return a menu context
	 */
	MenuContext getMenuContext();

	/**
	 * Is this context ready for drawing ? A context must be attached to the document's body to allow drawing.
	 * 
	 * @return true if ready for drawing
	 */
	boolean isReady();
}
