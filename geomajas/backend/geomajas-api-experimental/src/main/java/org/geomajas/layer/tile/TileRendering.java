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

package org.geomajas.layer.tile;

/**
 * <p>
 * Definition of the actual rendering object of a tile. Depending on the rendering method, this object will contain
 * either a string (SVG / VML) or a URL (to an image).
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface TileRendering {

	/**
	 * <p>
	 * The rendering method used for a specific tile. Geomajas supports different rendering strategies with different
	 * ways of rendering a tile. This enumeration defines the supported rendering methods.
	 * </p>
	 * 
	 * @author Pieter De Graef
	 */
	enum TileRenderMethod {
		/**
		 * Rendering method that contains an entire tile in a string format, such as SVG or VML. On the client side,
		 * this string will be plugged directly into the HTML DOM tree.
		 */
		STRING_RENDERING,

		/** Rendering method that contains an URL to an image that contains the actual rendering of a tile. */
		IMAGE_RENDERING
	};

	/**
	 * Return the rendering method used.
	 */
	TileRenderMethod getTileRenderMethod();

	/**
	 * @return Return the string format. This will return the actual rendering of a tile in a string format (such as SVG
	 *         or VML), but only if the rendering method used is STRING_RENDERING. Returns null otherwise.
	 */
	String getFeatureString();

	void setFeatureString(String featureString);

	String getLabelString();

	void setLabelString(String labelString);
}
