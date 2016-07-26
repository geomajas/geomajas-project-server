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

package org.geomajas.rendering.image;

/**
 * Interface for creating image URL's. This is used in the raster rendering strategies.
 *
 * @author Pieter De Graef
 */
public interface RasterUrlBuilder {

	/**
	 * Return the URL of a tile's image. This can for example point to an external WMS server, or our internal
	 * {@link RetrieveImageServlet}.
	 *
	 * @return
	 */
	String getImageUrl();

	/**
	 * Enables or disabled the use of painter that paint the geometries of the
	 * features in the tile.
	 *
	 * @param paintGeometries true or false.
	 */
	void paintGeometries(boolean paintGeometries);

	/**
	 * Enables or disabled the use of painter that paint the labels of the
	 * features in the tile.
	 *
	 * @param paintLabels true or false.
	 */
	void paintLabels(boolean paintLabels);
}
