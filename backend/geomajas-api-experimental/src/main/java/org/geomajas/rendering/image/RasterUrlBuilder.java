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
