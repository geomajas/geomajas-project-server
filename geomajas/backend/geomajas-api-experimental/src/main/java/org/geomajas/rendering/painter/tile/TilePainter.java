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

package org.geomajas.rendering.painter.tile;

import org.geomajas.layer.tile.InternalTile;
import org.geomajas.rendering.RenderException;

/**
 * <p>
 * Generic interface for rendering tiles. Implementations of this interface are mainly used in the different rendering
 * strategies. Some may choose to create an image such as a PNG from a tile, while other will create SVG, or perhaps
 * even VML.
 * </p>
 * <p>
 * Anyway, what's important here is the timing! You may notice that this interface has only one function. This function
 * takes in a <code>RenderedTile</code> as parameter and also returns one. The idea here is that implementations of this
 * interface add a rendered result to this tile. This result can be either SVG, VML or an image.<br/>
 * Now back to the timing thing: the tile you get as parameters here, should already contain
 * <code>InternalFeature</code> objects. Usually it will come from a <code>TiledFeaturePainter</code>, which is used in
 * almost all rendering strategies as feature painter.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface TilePainter {

	/**
	 * This function will take a tile, and render it. In the tile, there should already be rendered features which can
	 * be used in actually creating the rendering output format.
	 *
	 * @param tile
	 * @return
	 * @throws RenderException
	 */
	InternalTile paint(InternalTile tile) throws RenderException;

	/**
	 * Enables or disabled the use of painter that paint the geometries of the
	 * features in the tile.
	 *
	 * @param paintGeometries true or false.
	 */
	void setPaintGeometries(boolean paintGeometries);

	/**
	 * Enables or disabled the use of painter that paint the labels of the
	 * features in the tile.
	 *
	 * @param paintLabels true or false.
	 */
	void setPaintLabels(boolean paintLabels);
}
