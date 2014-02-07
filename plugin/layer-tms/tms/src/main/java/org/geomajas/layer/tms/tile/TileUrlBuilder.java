/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.tms.tile;

import org.geomajas.layer.tile.TileCode;

/**
 * Builds a URL for a specific tile within the raster layer.
 * 
 * @author Pieter De Graef
 */
public interface TileUrlBuilder {

	/**
	 * Get the unique URL for a tile through it's tile-code.
	 * 
	 * @param tileCode
	 *            The tile-code that points to a unique tile within the raster layer.
	 * @param baseTmsUrl
	 *            The base url.
	 * @return The URL where to retrieve the actual image.
	 */
	String buildUrl(TileCode tileCode, String baseTmsUrl);
}