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

import org.geomajas.geometry.Bbox;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.layer.tile.TileCode;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <p>
 * Internal utility class containing methods for tile calculations.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class TileService {

	private TileService() {
	}

	/**
	 * Calculates the tiles width and height in the given layers coordinates system.
	 * 
	 * @param code
	 *            The unique tile code. Determines what tile we're talking about.
	 * @param layer
	 *            The layer for which we're calculating. The width and height are dependent on the layer's max extent.
	 * @return Returns an array of double values where the first value is the tile width and the second value is the
	 *         tile height.
	 */
	public static double[] getTileLayerSize(TileCode code, VectorLayer layer) {
		Bbox max = layer.getLayerInfo().getMaxExtent();
		double div = Math.pow(2, code.getTileLevel());
		double tileWidth = max.getWidth() / div;
		double tileHeight = max.getHeight() / div;
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
	public static double[] getTileScreenSize(double[] worldSize, double scale) {
		double screenWidth = scale * worldSize[0];
		double screenHeight = scale * worldSize[1];
		return new double[] { screenWidth, screenHeight };
	}

	/**
	 * Get the bounding box for a certain tile, expressed in the layer's own coordinate system.
	 * 
	 * @param code
	 *            The unique tile code. Determines what tile we're talking about.
	 * @param layer
	 *            The layer for which we're calculating. The width and height are dependent on the layer's max extent,
	 *            and also the starting point in the coordinate system is dependent on the bottom left coordinate of the
	 *            max extent.
	 * @return Returns the bounding box for the tile, expressed in the layer's coordinate system.
	 */
	public static Envelope getTileBounds(TileCode code, VectorLayer layer) {
		double[] layerSize = getTileLayerSize(code, layer);
		if (layerSize[0] == 0) {
			return null;
		}
		Bbox max = layer.getLayerInfo().getMaxExtent();
		double cX = max.getX() + code.getX() * layerSize[0];
		double cY = max.getY() + code.getY() * layerSize[1];
		return new Envelope(cX, cX + layerSize[0], cY, cY + layerSize[1]);
	}


	/**
	 * When the layer and map's coordinate systems differ, the tile and screen width and height should be transformed to
	 * accommodate for this.
	 * 
	 * @param tile
	 *            The actual tile.
	 * @param transform
	 *            The transformation object.
	 * @throws GeomajasException
	 *             oops
	 */
	public static void transformTileSizes(InternalTile tile, MathTransform transform) throws GeomajasException {
		try {
			Envelope size = JTS.transform(new Envelope(0, tile.getScreenWidth(), 0, tile.getScreenHeight()), transform);
			tile.setScreenWidth(Math.ceil(size.getWidth()));
			tile.setScreenHeight(Math.ceil(size.getHeight()));
			size = JTS.transform(new Envelope(0, tile.getTileWidth(), 0, tile.getTileHeight()), transform);
			tile.setTileWidth(Math.ceil(size.getWidth()));
			tile.setTileHeight(Math.ceil(size.getHeight()));
			tile.setBounds(JTS.transform(tile.getBounds(), transform));
		} catch (TransformException e) {
			throw new GeomajasException(e);
		}
	}
}
