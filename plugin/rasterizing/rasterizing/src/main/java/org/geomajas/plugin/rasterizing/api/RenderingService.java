/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.api;

import java.awt.Graphics2D;
import java.awt.image.RenderedImage;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geotools.map.MapContext;

/**
 * This service renders a map context to a graphics context. The general purpose is to create a thread-safe wrapper for
 * the Geotools renderer.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
// @extract-start RenderingService, Rendering service interface definition
public interface RenderingService {

	/**
	 * Renders the legend for the specified map context.
	 * 
	 * @param context map context
	 * @return the image
	 */
	RenderedImage paintLegend(MapContext context);

	/**
	 * Renders the map context to the specified Java 2D graphics.
	 * 
	 * @param context map context
	 * @param graphics graphics object
	 */
	void paintMap(MapContext context, Graphics2D graphics);

	/**
	 * Renders the map context to the specified Java 2D graphics using some extra renderer hints.
	 * 
	 * @param context map context
	 * @param graphics graphics object
	 * @param rendererHints map of renderer hints (see
	 *        {@link org.geotools.renderer.lite.StreamingRenderer#setRendererHints(Map)}
	 * @since 1.1.0
	 */
	void paintMap(MapContext context, Graphics2D graphics, Map<Object, Object> rendererHints);
}
// @extract-end
