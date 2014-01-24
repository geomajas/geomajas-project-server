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
	 * @param tile tile which needs to be painter
	 * @return the tile which needs to be painted (same as parameter)
	 * @throws RenderException painting failed
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
