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

package org.geomajas.plugin.caching.step;

import org.geomajas.layer.tile.InternalTile;

/**
 * Container for the objects which need to be stored in the tile cache.
 *
 * @author Joachim Van der Auwera
 */
public class TileCacheContainer extends CacheContainer {

	private static final long serialVersionUID = 100L;

	private final InternalTile tile;

	/**
	 * Create for a specific tile.
	 *
	 * @param tile tile
	 */
	public TileCacheContainer(InternalTile tile) {
		super();
		this.tile = tile;
	}

	/**
	 * Get the cached tile.
	 *
	 * @return tile
	 */
	public InternalTile getTile() {
		return tile;
	}
}
