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

package org.geomajas.gwt2.client.map.layer;

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
	 * Get the URL by which the legend image for this layer can be retrieved. This method will return the default legend
	 * image URL. It is up to implementing classes to specify what such a default would look like.
	 * 
	 * @return Returns the default URL to the legend image.
	 */
	String getLegendImageUrl();

	/**
	 * Get the URL by which the legend image for this layer can be retrieved. This method provides an extra
	 * configuration object through which the legend can be adjusted. Not all implementations will support all options,
	 * so it is up to them to specify which options are supported.
	 * 
	 * @param config
	 *            Configuration object that specifies what the legend should look like.
	 * @return Returns the URL to the legend image.
	 */
	String getLegendImageUrl(LegendConfig config);
}