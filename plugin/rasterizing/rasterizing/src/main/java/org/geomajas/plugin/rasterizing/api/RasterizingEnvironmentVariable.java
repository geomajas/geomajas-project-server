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

import org.geomajas.annotation.Api;

/**
 * Geotools environment variable names that can be used in SLD environment functions. Example:
 * 
 * <pre>
 * &lt;ogc:Function name="env">
 *   &lt;ogc:Literal>screen_width&lt;/ogc:Literal>
 * &lt;/ogc:Function>
 * </pre>
 * 
 * When adding a variable, make sure it is injected before the SLD is applied (e.g in AddLayersStep).
 * 
 * @author Jan De Moerloose
 * @since 1.3.0
 */
@Api(allMethods = true)
public interface RasterizingEnvironmentVariable {

	/**
	 * Envelope of map context viewport.
	 */
	String BBOX = "bbox";

	/**
	 * Screen width of map context viewport screen area.
	 */
	String SCREEN_WIDTH = "screen_width";

	/**
	 * Screen height of map context viewport screen area.
	 */
	String SCREEN_HEIGHT = "screen_height";

}
