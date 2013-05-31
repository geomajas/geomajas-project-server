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

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.annotation.Api;

/**
 * Interface meant for layers that are able to provide a URL to their legend, such as WMS layers. The URL refers to an
 * image containing all legend icons and their labels for the currently active style of this layer.
 * 
 * @author An Buyle
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface LegendUrlSupported {

	/**
	 * Get the URL by which the legend image for this layer can be retrieved.
	 * 
	 * @return Returns the default URL to the legend image.
	 */
	String getLegendImageUrl();

	/**
	 * Get the URL by which the legend image for the concerning layer can be retrieved.
	 * 
	 * @param width
	 *            width in pixels of the icon for a legend symbol (if 0, the default is used)
	 * @param height
	 *            height in pixels of the icon for a legend symbol (if 0, the default is used)
	 * @param fontFamily
	 *            hint for the font family (not all implementations take this parameter into account); e.g. "Dialog" (if
	 *            null, the default is used)
	 * @param fontSize
	 *            hint for font size in points (not all implementations take this parameter into account); (if 0, the
	 *            default is used)
	 * 
	 * @return URL for the legend image e.g. "http://www.myserver.org/geoserver/wms?service=WMS&layer=countries
	 *         :population&request=GetLegendGraphic&format=image/png&width=20&height =20&transparent=true"
	 */
	String getLegendImageUrl(int width, int height, String fontFamily, int fontSize);
}