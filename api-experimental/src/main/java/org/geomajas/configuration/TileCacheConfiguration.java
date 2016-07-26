/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.configuration;

/**
 * Container class which contains configuration parameters for tile caching.
 *
 * @author Joachim Van der Auwera
 */
public class TileCacheConfiguration {

	private String tileCacheDirectory;
	private int tileCacheMaximumSize;
	private boolean tileCacheEnabled;

	/**
	 * Get the directory where the tile cache should be stored.
	 *
	 * @return tile cache location
	 */
	public String getTileCacheDirectory() {
		return tileCacheDirectory;
	}

	/**
	 * Set the directory where the tile cache should be stored.
	 *
	 * @param tileCacheDirectory tile cache location
	 */
	public void setTileCacheDirectory(String tileCacheDirectory) {
		this.tileCacheDirectory = tileCacheDirectory;
	}

	/**
	 * Get maximum number of cached tiles for the tile cache.
	 *
	 * @return maximum number of tiles which are cached
	 */
	public int getTileCacheMaximumSize() {
		return tileCacheMaximumSize;
	}

	/**
	 * Set the maximum number of tiles which may be cached.
	 *
	 * @param tileCacheMaximumSize maximum number of tiles which are cached
	 */
	public void setTileCacheMaximumSize(int tileCacheMaximumSize) {
		this.tileCacheMaximumSize = tileCacheMaximumSize;
	}

	/**
	 * Check whether the tile cache is enabled.
	 *
	 * @return true when tile cache should be used
	 */
	public boolean isTileCacheEnabled() {
		return tileCacheEnabled;
	}

	/**
	 * Set whether the tile cache should be used or not.
	 *
	 * @param tileCacheEnabled new status
	 */
	public void setTileCacheEnabled(boolean tileCacheEnabled) {
		this.tileCacheEnabled = tileCacheEnabled;
	}
}
