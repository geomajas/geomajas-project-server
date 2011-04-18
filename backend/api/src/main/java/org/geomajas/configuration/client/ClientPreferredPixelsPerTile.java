/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.client;

import java.io.Serializable;

import org.geomajas.global.Api;


/**
 * Configuration of the preferred pixels per tile on the map.
 * 
 * @author Oliver May
 * @since 1.9.0
 */
@Api(allMethods = true)
public class ClientPreferredPixelsPerTile implements Serializable {

	private static final long serialVersionUID = 1L;

	private PreferredPixelsPerTileType preferredPixelsPerTileType = PreferredPixelsPerTileType.CONFIGURED;

	private int width = 256;

	private int height = 256;

	/**
	 * @param preferredPixelsPerTileType the preferredPixelsPerTileType to set
	 */
	public void setPreferredPixelsPerTileType(PreferredPixelsPerTileType preferredPixelsPerTileType) {
		this.preferredPixelsPerTileType = preferredPixelsPerTileType;
	}

	/**
	 * @return the preferredPixelsPerTileType
	 */
	public PreferredPixelsPerTileType getPreferredPixelsPerTileType() {
		return preferredPixelsPerTileType;
	}

	/**
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
