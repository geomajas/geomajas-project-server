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
package org.geomajas.plugin.rasterizing.client.image;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.widget.MapWidget;

/**
 * Service to generated map and legend images.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 * 
 */
@Api(allMethods = true)
// @extract-start ImageUrlService, Image URL service definition
public interface ImageUrlService {

	/**
	 * Create map and legend images for the specified map.
	 * 
	 * @param map the map
	 * @param imageCallBack call back function
	 * @param makeRasterizable should the service make the map rasterizable ?
	 */
	void createImageUrl(MapWidget map, ImageUrlCallback imageCallBack, boolean makeRasterizable);

	/**
	 * Create map and legend images for the specified map. Preparing for rasterization is done by the service.
	 * 
	 * @param map the map
	 * @param imageCallBack call back function
	 */
	void createImageUrl(MapWidget map, ImageUrlCallback imageCallBack);

	/**
	 * Prepare the specified map for server-side rasterization.
	 * 
	 * @param map the map
	 */
	void makeRasterizable(MapWidget map);
}
// @extract-end
