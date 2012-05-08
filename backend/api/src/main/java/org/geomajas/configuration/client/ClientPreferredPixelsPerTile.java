/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.client;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;


/**
 * Configuration of the preferred pixels per tile on the map.
 * 
 * @author Oliver May
 * @since 1.9.0
 */
@Api(allMethods = true)
public class ClientPreferredPixelsPerTile implements IsInfo {

	private static final long serialVersionUID = 1L;

	private PreferredPixelsPerTileType preferredPixelsPerTileType = PreferredPixelsPerTileType.CONFIGURED;

	private int width = 256;

	private int height = 256;

	/**
	 * Set the preferred pixels per tile.
	 *
	 * @param preferredPixelsPerTileType the preferredPixelsPerTileType to set
	 */
	public void setPreferredPixelsPerTileType(PreferredPixelsPerTileType preferredPixelsPerTileType) {
		this.preferredPixelsPerTileType = preferredPixelsPerTileType;
	}

	/**
	 * Get the preferred pixels per tile.
	 *
	 * @return the preferredPixelsPerTileType
	 */
	public PreferredPixelsPerTileType getPreferredPixelsPerTileType() {
		return preferredPixelsPerTileType;
	}

	/**
	 * Get width of the preferred tile size in pixels.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width of the preferred tile size in pixels.
	 * 
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get height of the preferred tile size in pixels.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the height of the preferred tile size in pixels.
	 * 
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

}
