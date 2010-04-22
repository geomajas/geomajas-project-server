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

package org.geomajas.internal.rendering.strategy;

import org.geomajas.layer.tile.TileCode;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * Internal utility class containing methods for tile calculations.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class TileUtil {

	private TileUtil() {
	}

	/**
	 * Calculates the tiles width and height.
	 * 
	 * @param code
	 *            The unique tile code. Determines what tile we're talking about.
	 * @param maxExtent
	 *            The maximum extent of the grid to which this tile belongs.
	 * @param scale
	 *            The current client side scale.
	 * @return Returns an array of double values where the first value is the tile width and the second value is the
	 *         tile height.
	 */
	public static double[] getTileLayerSize(TileCode code, Envelope maxExtent, double scale) {
		double div = Math.pow(2, code.getTileLevel());
		double tileWidth = Math.ceil((scale * maxExtent.getWidth()) / div) / scale;
		double tileHeight = Math.ceil((scale * maxExtent.getHeight()) / div) / scale;
		return new double[] { tileWidth, tileHeight };
	}

	/**
	 * Calculate the screen size of a tile. Normally the screen size is expressed in pixels and should therefore be
	 * integers, but for the sake of accuracy we try to keep a double value as long as possible.
	 * 
	 * @param worldSize
	 *            The width and height of a tile in the layer's world coordinate system.
	 * @param scale
	 *            The current client side scale.
	 * @return Returns an array of double values where the first value is the tile screen width and the second value is
	 *         the tile screen height.
	 */
	public static int[] getTileScreenSize(double[] worldSize, double scale) {
		int screenWidth = (int) Math.round(scale * worldSize[0]);
		int screenHeight = (int) Math.round(scale * worldSize[1]);
		return new int[] { screenWidth, screenHeight };
	}

	/**
	 * Get the bounding box for a certain tile.
	 * 
	 * @param code
	 *            The unique tile code. Determines what tile we're talking about.
	 * @param maxExtent
	 *            The maximum extent of the grid to which this tile belongs.
	 * @param scale
	 *            The current client side scale.
	 * @return Returns the bounding box for the tile, expressed in the layer's coordinate system.
	 */
	public static Envelope getTileBounds(TileCode code, Envelope maxExtent, double scale) {
		double[] layerSize = getTileLayerSize(code, maxExtent, scale);
		if (layerSize[0] == 0) {
			return null;
		}
		double cX = maxExtent.getMinX() + code.getX() * layerSize[0];
		double cY = maxExtent.getMinY() + code.getY() * layerSize[1];
		return new Envelope(cX, cX + layerSize[0], cY, cY + layerSize[1]);
	}

}
