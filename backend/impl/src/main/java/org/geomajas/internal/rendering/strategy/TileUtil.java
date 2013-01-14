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
