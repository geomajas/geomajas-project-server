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
