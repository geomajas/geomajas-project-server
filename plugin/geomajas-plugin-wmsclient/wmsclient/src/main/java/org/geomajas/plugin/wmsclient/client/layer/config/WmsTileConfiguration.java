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

package org.geomajas.plugin.wmsclient.client.layer.config;

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.geometry.Coordinate;

/**
 * Basic configuration for a tile based layer.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class WmsTileConfiguration implements Serializable {

	private static final long serialVersionUID = 100L;

	private int tileWidth;

	private int tileHeight;

	private Coordinate tileOrigin;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/** Create a new instance. */
	public WmsTileConfiguration() {
	}

	/**
	 * Create a new instance using, specifying all values.
	 * 
	 * @param tileWidth
	 *            The width in pixels for image tiles.
	 * @param tileHeight
	 *            The height in pixels for image tiles.
	 * @param tileOrigin
	 *            The position in world space where tile (0,0) begins.
	 */
	public WmsTileConfiguration(int tileWidth, int tileHeight, Coordinate tileOrigin) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.tileOrigin = tileOrigin;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the tile image width in pixels.
	 * 
	 * @return The tile width.
	 */
	public int getTileWidth() {
		return tileWidth;
	}

	/**
	 * Set the tile image width in pixels.
	 * 
	 * @param tileWidth
	 *            The tile width.
	 */
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}

	/**
	 * Get the tile image height in pixels.
	 * 
	 * @return The tile height.
	 */
	public int getTileHeight() {
		return tileHeight;
	}

	/**
	 * Set the tile image height in pixels.
	 * 
	 * @param tileHeight
	 *            The tile height.
	 */
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}

	/**
	 * Get the origin in world space for tile (0,0). Usually this is the lower-left corner for your layer.
	 * 
	 * @return The tile origin in world space.
	 */
	public Coordinate getTileOrigin() {
		return tileOrigin;
	}

	/**
	 * Set the origin in world space for tile (0,0). Usually this is the lower-left corner for your layer.
	 * 
	 * @param tileOrigin
	 *            The tile origin in world space.
	 */
	public void setTileOrigin(Coordinate tileOrigin) {
		this.tileOrigin = tileOrigin;
	}
}