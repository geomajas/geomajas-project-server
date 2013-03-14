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
package org.geomajas.plugin.wmsclient.printing.server.dto;

import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.tile.RasterTile;

/**
 * {@link ClientLayerInfo} for client-side WMS layers.
 * 
 * @author Jan De Moerloose
 * 
 */
public class WmsClientLayerInfo extends ClientLayerInfo {

	private static final long serialVersionUID = 100L;

	private List<RasterTile> tiles;

	private int tileWidth;

	private int tileHeight;

	private Coordinate tileOrigin;

	private double scale;

	/**
	 * @see #setTiles(List)
	 * @return
	 */
	public List<RasterTile> getTiles() {
		return tiles;
	}

	/**
	 * Set the list of tiles to print (optional, only for client layers).
	 * 
	 * @param tiles list of tiles
	 */
	public void setTiles(List<RasterTile> tiles) {
		this.tiles = tiles;
	}

	/**
	 * @see #setTileWidth(int)
	 * @return
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Set the tile width in pixels (optional, only for client layers).
	 * 
	 * @param tileWidth
	 */
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	/**
	 * @see #setTileHeight(int)
	 * @return
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Set the tile height in pixels (optional, only for client layers).
	 * 
	 * @param tileHeight
	 */
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	/**
	 * @see #setTileOrigin(tileOrigin)
	 * @return
	 */
	public Coordinate getTileOrigin() {
		return tileOrigin;
	}

	/**
	 * Set the tile origin in world coordinates.
	 * 
	 * @param tileHeight
	 */
	public void setTileOrigin(Coordinate tileOrigin) {
		this.tileOrigin = tileOrigin;
	}

	/**
	 * @see #setScale(scale)
	 * @return
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Set the scale of the tiles.
	 * 
	 * @param scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

}
