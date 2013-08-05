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
package org.geomajas.smartgwt.client.map.cache.tile;

import org.geomajas.smartgwt.client.spatial.Bbox;
import org.geomajas.layer.tile.TileCode;

/**
 * A tile represents a rectangular partitioning of a layer's data. All layers are tile-based, which enables reproducible
 * and cacheable requests to the server. A tile is a part of a larger spatial structure with a unique spatial code to
 * identify it and it's location.
 *
 * @author Jan De Moerloose
 */
public interface Tile {

	/**
	 * Return the unique {@link TileCode} for this tile.
	 *
	 * @return tile code 
	 */
	TileCode getCode();

	/**
	 * Return this tile's bounding box.
	 *
	 * @return bounds
	 */
	Bbox getBounds();
}
